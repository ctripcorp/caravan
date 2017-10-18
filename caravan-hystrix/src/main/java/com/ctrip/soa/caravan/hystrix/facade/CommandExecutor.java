package com.ctrip.soa.caravan.hystrix.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.soa.caravan.common.delegate.Action;
import com.ctrip.soa.caravan.common.delegate.Func;
import com.ctrip.soa.caravan.common.value.NullValues;
import com.ctrip.soa.caravan.common.value.StringValues;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.hystrix.ExecutionCommand;
import com.ctrip.soa.caravan.hystrix.ExecutionCommandManager;
import com.ctrip.soa.caravan.hystrix.ExecutionContext;
import com.ctrip.soa.caravan.hystrix.ExecutionEvent;
import com.ctrip.soa.caravan.hystrix.ValidationFailChecker;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class CommandExecutor {

    private static final Logger _logger = LoggerFactory.getLogger(CommandExecutor.class);

    public static void execute(ExecutionCommand command, Action executor) {
        execute(command, executor, null);
    }

    public static void execute(ExecutionCommand command, Action executor, ValidationFailChecker validationFailChecker) {
        NullArgumentChecker.DEFAULT.check(command, "command");
        NullArgumentChecker.DEFAULT.check(executor, "executor");

        ExecutionContext context = command.newExecutionContext();
        context.startExecution();
        try {
            executor.execute();
            context.markSuccess();
        } catch (Throwable ex) {
            if (isValidationFail(validationFailChecker, ex))
                context.markValidationFail();
            else
                context.markFail();

            throw ex;
        } finally {
            context.endExecution();
        }
    }

    public static <V> V execute(ExecutionCommand command, Func<V> executor) {
        return execute(command, executor, NullValues.<ValidationFailChecker> NULL());
    }

    public static <V> V execute(ExecutionCommand command, Func<V> executor, ValidationFailChecker validationFailChecker) {
        NullArgumentChecker.DEFAULT.check(command, "command");
        NullArgumentChecker.DEFAULT.check(executor, "executor");

        ExecutionContext context = command.newExecutionContext();
        context.startExecution();
        try {
            V result = executor.execute();
            context.markSuccess();
            return result;
        } catch (Throwable ex) {
            if (isValidationFail(validationFailChecker, ex))
                context.markValidationFail();
            else
                context.markFail();

            throw ex;
        } finally {
            context.endExecution();
        }
    }

    public static <V> V execute(ExecutionCommand command, Func<V> executor, Func<V> fallbackProvider) {
        return execute(command, executor, fallbackProvider, null);
    }

    public static <V> V execute(ExecutionCommand command, Func<V> executor, Func<V> fallbackProvider, ValidationFailChecker validationFailChecker) {
        NullArgumentChecker.DEFAULT.check(command, "command");
        NullArgumentChecker.DEFAULT.check(executor, "executor");
        NullArgumentChecker.DEFAULT.check(fallbackProvider, "fallbackProvider");

        ExecutionContext context = command.newExecutionContext();
        if (!context.tryStartExecution()) {
            if (context.executionEvent() == ExecutionEvent.SHORT_CIRCUITED)
                _logger.warn("Execution is short circuited. Use fallback instead.");
            else
                _logger.warn("Execution is isolated. Use fallback instead.");

            return fallbackProvider.execute();
        }

        try {
            V result = executor.execute();
            context.markSuccess();
            return result;
        } catch (Throwable ex) {
            if (isValidationFail(validationFailChecker, ex))
                context.markValidationFail();
            else
                context.markFail();

            _logger.warn("Execution failed. Use fallback instead.", ex);
            return fallbackProvider.execute();
        } finally {
            context.endExecution();
        }
    }

    public static boolean isValidationFail(ValidationFailChecker validationFailChecker, Throwable ex) {
        if (validationFailChecker == null)
            return false;

        try {
            return validationFailChecker.isValidationFail(ex);
        } catch (Throwable ex2) {
            _logger.error("validationFailChecker failed to check the fail: " + validationFailChecker, ex2);
            return false;
        }
    }

    private ExecutionCommandManager _manager;
    private ValidationFailChecker _validationFailChecker;

    public CommandExecutor(ExecutionCommandManager manager) {
        this(manager, null);
    }

    public CommandExecutor(ExecutionCommandManager manager, ValidationFailChecker validationFailChecker) {
        NullArgumentChecker.DEFAULT.check(manager, "manager");
        _manager = manager;
        _validationFailChecker = validationFailChecker;
    }

    public void execute(String commandId, Action executor) {
        execute(commandId, StringValues.EMPTY, executor);
    }

    public void execute(String commandId, String groupId, Action executor) {
        NullArgumentChecker.DEFAULT.check(commandId, "commandId");
        NullArgumentChecker.DEFAULT.check(groupId, "groupId");
        NullArgumentChecker.DEFAULT.check(executor, "executor");

        ExecutionCommand command = _manager.getCommand(commandId, groupId);
        execute(command, executor, _validationFailChecker);
    }

    public <V> V execute(String commandId, Func<V> executor) {
        return execute(commandId, StringValues.EMPTY, executor);
    }

    public <V> V execute(String commandId, String groupId, Func<V> executor) {
        NullArgumentChecker.DEFAULT.check(commandId, "commandId");
        NullArgumentChecker.DEFAULT.check(groupId, "groupId");
        NullArgumentChecker.DEFAULT.check(executor, "executor");

        ExecutionCommand command = _manager.getCommand(commandId, groupId);
        return execute(command, executor, _validationFailChecker);
    }

    public <V> V execute(String commandId, Func<V> executor, Func<V> fallbackProvider) {
        return execute(commandId, StringValues.EMPTY, executor, fallbackProvider);
    }

    public <V> V execute(String commandId, String groupId, Func<V> executor, Func<V> fallbackProvider) {
        NullArgumentChecker.DEFAULT.check(commandId, "commandId");
        NullArgumentChecker.DEFAULT.check(groupId, "groupId");
        NullArgumentChecker.DEFAULT.check(executor, "executor");
        NullArgumentChecker.DEFAULT.check(fallbackProvider, "fallbackProvider");

        ExecutionCommand command = _manager.getCommand(commandId, groupId);
        return execute(command, executor, fallbackProvider, _validationFailChecker);
    }

}
