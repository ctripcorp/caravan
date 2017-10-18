package com.ctrip.soa.caravan.hystrix.metrics;

import java.util.Set;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.hystrix.ExecutionCommand;
import com.ctrip.soa.caravan.hystrix.ExecutionEvent;
import com.google.common.collect.Sets;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultHealthSnapshot implements HealthSnapshot {

    private final static Set<ExecutionEvent> _healthRelatedEvents = Sets.newHashSet(ExecutionEvent.SUCCESS,
            ExecutionEvent.FAILED, ExecutionEvent.TIMEOUT);
    private final static Set<ExecutionEvent> _errorEvents = Sets.newHashSet(ExecutionEvent.FAILED,
            ExecutionEvent.TIMEOUT);

    private volatile long _lastSnapshotTime;
    private volatile long _totalCount;
    private volatile int _errorPercentage;

    private ExecutionCommand _command;

    public DefaultHealthSnapshot(ExecutionCommand command) {
        NullArgumentChecker.DEFAULT.check(command, "command");
        _command = command;
    }

    @Override
    public long totalCount() {
        tryUpdateSnapshot();
        return _totalCount;
    }

    @Override
    public int errorPercentage() {
        tryUpdateSnapshot();
        return _errorPercentage;
    }

    private void tryUpdateSnapshot() {
        long now = System.currentTimeMillis();
        if (now - _lastSnapshotTime < _command.config().metricsConfig().healthSnapshotInterval())
            return;

        long totalCount = 0;
        long errorCount = 0;
        for (ExecutionEvent event : _healthRelatedEvents) {
            long eventCount = _command.metrics().getEventCount(event);
            totalCount += eventCount;
            if (_errorEvents.contains(event))
                errorCount += eventCount;
        }

        int errorPercentage = 0;
        if (totalCount > 0)
            errorPercentage = (int) (errorCount * 100 / totalCount);

        _totalCount = totalCount;
        _errorPercentage = errorPercentage;
        _lastSnapshotTime = now;
    }

}
