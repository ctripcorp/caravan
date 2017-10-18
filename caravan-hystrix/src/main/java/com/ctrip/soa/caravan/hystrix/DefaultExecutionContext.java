package com.ctrip.soa.caravan.hystrix;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.hystrix.facade.ExecutionExceptons;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultExecutionContext implements ExecutionContext {

    private static final Logger _logger = LoggerFactory.getLogger(DefaultExecutionContext.class);

    private ExecutionCommand _command;
    private AtomicBoolean _started = new AtomicBoolean();
    private AtomicBoolean _markedResult = new AtomicBoolean();
    private AtomicBoolean _ended = new AtomicBoolean();
    private volatile long _startTime;
    private volatile ExecutionEvent _executionEvent;

    public DefaultExecutionContext(ExecutionCommand command) {
        NullArgumentChecker.DEFAULT.check(command, "command");
        _command = command;
    }

    @Override
    public void startExecution() {
        StartCheckResult checkResult = canStartExecution();
        if (checkResult.canStart)
            return;

        if (checkResult.failEvent == null)
            return;

        _executionEvent = checkResult.failEvent;
        _command.metrics().markEvent(checkResult.failEvent);
        if (checkResult.failEvent == ExecutionEvent.SHORT_CIRCUITED)
            _logger.error("Command execution is shorted-circuited. Command: " + _command.commandKey());
        else if (checkResult.failEvent == ExecutionEvent.REJECTED)
            _logger.error("Command execution is isolated. Command: " + _command.commandKey());

        throw ExecutionExceptons.newException(_command, checkResult.failEvent);
    }

    @Override
    public boolean tryStartExecution() {
        StartCheckResult checkResult = canStartExecution();
        if (checkResult.canStart)
            return true;

        if (checkResult.failEvent == null)
            return false;

        _executionEvent = checkResult.failEvent;
        _command.metrics().markEvent(checkResult.failEvent);
        return false;
    }

    @Override
    public void markSuccess() {
        if (!canMarkResult())
            return;

        long executionLatency = System.currentTimeMillis() - _startTime;
        _command.metrics().markExecutionLatency(executionLatency);

        if (executionLatency < _command.config().circuitBreakerConfig().executionTimeout()) {
            _executionEvent = ExecutionEvent.SUCCESS;
            _command.metrics().markEvent(ExecutionEvent.SUCCESS);
            _command.circuitBreaker().markSuccess();
            return;
        }

        String errorMessage = String.format("Command execution is too long. Command: %s, Execution Time: %s", _command.commandKey(), executionLatency);
        _logger.warn(errorMessage);
        _executionEvent = ExecutionEvent.TIMEOUT;
        _command.metrics().markEvent(ExecutionEvent.TIMEOUT);
    }

    @Override
    public void markFail() {
        if (!canMarkResult())
            return;

        _executionEvent = ExecutionEvent.FAILED;
        _command.metrics().markEvent(ExecutionEvent.FAILED);
        _command.metrics().markExecutionLatency(System.currentTimeMillis() - _startTime);
    }

    @Override
    public void markValidationFail() {
        if (!canMarkResult())
            return;

        _executionEvent = ExecutionEvent.VALIDATION_FAIL;
        _command.metrics().markEvent(ExecutionEvent.VALIDATION_FAIL);
        _command.metrics().markExecutionLatency(System.currentTimeMillis() - _startTime);
    }

    @Override
    public ExecutionEvent executionEvent() {
        return _executionEvent;
    }

    @Override
    public void endExecution() {
        if (!canEndExecution())
            return;

        _command.isolator().markComplete();
    }

    private StartCheckResult canStartExecution() {
        if (!_started.compareAndSet(false, true)) {
            String errorMessage = String.format("Command has been started or run over. Command: %s", _command.commandKey());
            _logger.warn(errorMessage);
            return new StartCheckResult(false);
        }

        _startTime = System.currentTimeMillis();

        ExecutionEvent failEvent = null;
        if (!_command.circuitBreaker().allowExecution())
            failEvent = ExecutionEvent.SHORT_CIRCUITED;
        else if (!_command.isolator().allowExecution())
            failEvent = ExecutionEvent.REJECTED;

        if (failEvent != null) {
            _ended.set(true);
            return new StartCheckResult(false, failEvent);
        }

        return new StartCheckResult(true);
    }

    private boolean canMarkResult() {
        if (!_started.get()) {
            String errorMessage = String.format("Command has not been started. Command: %s", _command.commandKey());
            _logger.warn(errorMessage);
            return false;
        }

        if (_ended.get())
            return false;

        if (!_markedResult.compareAndSet(false, true)) {
            String errorMessage = String.format("Command has been marked a result. Command: %s", _command.commandKey());
            _logger.warn(errorMessage);
            return false;
        }

        return true;
    }

    private boolean canEndExecution() {
        if (!_started.get()) {
            String errorMessage = String.format("Command has not been started. Command: %s", _command.commandKey());
            _logger.warn(errorMessage);
            return false;
        }

        if (!_ended.compareAndSet(false, true))
            return false;

        if (!_markedResult.get()) {
            String errorMessage = String.format("Command has not been marked a result. Command: %s", _command.commandKey());
            _logger.warn(errorMessage);
        }

        return true;
    }

    private static class StartCheckResult {

        public boolean canStart;
        public ExecutionEvent failEvent;

        public StartCheckResult(boolean canStart) {
            this(canStart, null);
        }

        public StartCheckResult(boolean canStart, ExecutionEvent failEvent) {
            this.canStart = canStart;
            this.failEvent = failEvent;
        }

    }

}
