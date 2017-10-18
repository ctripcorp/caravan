package com.ctrip.soa.caravan.hystrix.circuitbreaker;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.hystrix.ExecutionCommand;
import com.ctrip.soa.caravan.hystrix.metrics.HealthSnapshot;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultCircuitBreaker implements CircuitBreaker {

    private AtomicBoolean _isOpen = new AtomicBoolean();

    private AtomicLong _lastTriedTime = new AtomicLong();

    private ExecutionCommand _command;

    public DefaultCircuitBreaker(ExecutionCommand command) {
        NullArgumentChecker.DEFAULT.check(command, "command");
        _command = command;
    }

    @Override
    public boolean allowExecution() {
        if (!_command.config().circuitBreakerConfig().enabled())
            return true;

        if (_command.config().circuitBreakerConfig().forceOpen())
            return false;

        if (_command.config().circuitBreakerConfig().forceClosed()) {
            isOpen();
            return true;
        }

        return !isOpen() || allowSingleTest();
    }

    @Override
    public void markSuccess() {
        if (!_isOpen.get())
            return;

        _isOpen.set(false);
        _command.metrics().reset();
    }

    @Override
    public boolean isOpen() {
        if (_isOpen.get())
            return true;

        HealthSnapshot healthSnapshot = _command.metrics().getHealthSnapshot();
        if (healthSnapshot.totalCount() < _command.config().circuitBreakerConfig().executionCountThreshold())
            return false;

        if (healthSnapshot.errorPercentage() < _command.config().circuitBreakerConfig().errorPercentageThreshold())
            return false;

        if (_isOpen.compareAndSet(false, true))
            _lastTriedTime.set(System.currentTimeMillis());

        return true;
    }

    private boolean allowSingleTest() {
        if (!_isOpen.get())
            return true;

        long lastTriedTime = _lastTriedTime.get();
        long now = System.currentTimeMillis();
        if (lastTriedTime + _command.config().circuitBreakerConfig().retryInterval() > now)
            return false;

        return _lastTriedTime.compareAndSet(lastTriedTime, now);
    }

}