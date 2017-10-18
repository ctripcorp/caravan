package com.ctrip.soa.caravan.hystrix.metrics;

import com.ctrip.soa.caravan.common.collect.AuditData;
import com.ctrip.soa.caravan.common.concurrent.collect.circularbuffer.timebucket.CounterBuffer;
import com.ctrip.soa.caravan.common.concurrent.collect.circularbuffer.timebucket.IntegerPercentileBuffer;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.hystrix.ExecutionCommand;
import com.ctrip.soa.caravan.hystrix.ExecutionEvent;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultExecutionMetrics implements ExecutionMetrics {

    private ExecutionCommand _command;

    private CounterBuffer<ExecutionEvent> _eventCounterBuffer;
    private IntegerPercentileBuffer _executionLatencyBuffer;
    private HealthSnapshot _healthSnapshot;

    public DefaultExecutionMetrics(ExecutionCommand command) {
        NullArgumentChecker.DEFAULT.check(command, "command");
        _command = command;
        reset();
    }

    @Override
    public void markEvent(ExecutionEvent event) {
        _eventCounterBuffer.incrementCount(event);
    }

    @Override
    public long getEventCount(ExecutionEvent event) {
        return _eventCounterBuffer.getCount(event);
    }

    @Override
    public HealthSnapshot getHealthSnapshot() {
        return _healthSnapshot;
    }

    @Override
    public void markExecutionLatency(long latency) {
        _executionLatencyBuffer.add(latency);
    }

    @Override
    public long getLatencyPercentile(double percent) {
        return _executionLatencyBuffer.getPercentile(percent);
    }

    @Override
    public AuditData getLatencyAuditData() {
        return _executionLatencyBuffer.getAuditData();
    }

    @Override
    public long getLatencyCountInRange(long lowerBound, long upperBound) {
        return _executionLatencyBuffer.getItemCountInRange(lowerBound, upperBound);
    }

    @Override
    public void reset() {
        _eventCounterBuffer = new CounterBuffer<>(_command.config().metricsConfig().eventCounterConfig());
        _executionLatencyBuffer = new IntegerPercentileBuffer(_command.config().metricsConfig().executionLatencyConfig());
        _healthSnapshot = new DefaultHealthSnapshot(_command);
    }

}
