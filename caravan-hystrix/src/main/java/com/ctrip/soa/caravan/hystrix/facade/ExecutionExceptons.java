package com.ctrip.soa.caravan.hystrix.facade;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.hystrix.ExecutionCommand;
import com.ctrip.soa.caravan.hystrix.ExecutionEvent;
import com.ctrip.soa.caravan.hystrix.exception.IsolationException;
import com.ctrip.soa.caravan.hystrix.exception.ShortCircuitException;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public final class ExecutionExceptons {

    private ExecutionExceptons() {

    }

    public static RuntimeException newException(ExecutionCommand command, ExecutionEvent failEvent) {
        NullArgumentChecker.DEFAULT.check(command, "command");
        NullArgumentChecker.DEFAULT.check(failEvent, "failEvent");

        String errorMessage;
        switch (failEvent) {
            case SHORT_CIRCUITED:
                errorMessage = String.format("Execution is short-circuited by circuit-breaker. Command: %s", command.commandKey());
                return new ShortCircuitException(errorMessage);
            case REJECTED:
                errorMessage = String.format("Execution is rejected by execution isolator. Command: %s", command.commandKey());
                return new IsolationException(errorMessage);
            default:
                errorMessage = String.format("Execution failed. Command: %s, Fail event: %s", command.commandKey(), failEvent);
                return new RuntimeException(errorMessage);
        }
    }

}
