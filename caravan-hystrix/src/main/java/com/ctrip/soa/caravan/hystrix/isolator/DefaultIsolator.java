package com.ctrip.soa.caravan.hystrix.isolator;

import java.util.concurrent.atomic.AtomicInteger;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.hystrix.ExecutionCommand;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultIsolator implements Isolator {

    private ExecutionCommand _command;
    private AtomicInteger _concurrentCount = new AtomicInteger();

    public DefaultIsolator(ExecutionCommand command) {
        NullArgumentChecker.DEFAULT.check(command, "command");
        _command = command;
    }

    @Override
    public boolean allowExecution() {
        int concurrentCount = _concurrentCount.incrementAndGet();
        if (concurrentCount <= _command.config().isolatorConfig().maxConcurrentCount())
            return true;

        markComplete();
        return false;
    }

    @Override
    public void markComplete() {
        _concurrentCount.decrementAndGet();
    }

    @Override
    public long concurrentCount() {
        return _concurrentCount.get();
    }

}
