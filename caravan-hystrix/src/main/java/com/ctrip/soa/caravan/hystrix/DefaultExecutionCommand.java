package com.ctrip.soa.caravan.hystrix;

import com.ctrip.soa.caravan.common.concurrent.collect.circularbuffer.timebucket.PercentileBufferConfig;
import com.ctrip.soa.caravan.common.concurrent.collect.circularbuffer.timebucket.TimeBufferConfig;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.common.value.checker.StringArgumentChecker;
import com.ctrip.soa.caravan.configuration.facade.TypedDynamicCachedCorrectedProperties;
import com.ctrip.soa.caravan.hystrix.circuitbreaker.CircuitBreaker;
import com.ctrip.soa.caravan.hystrix.circuitbreaker.DefaultCircuitBreaker;
import com.ctrip.soa.caravan.hystrix.config.CommandConfig;
import com.ctrip.soa.caravan.hystrix.config.DefaultExecutionConfig;
import com.ctrip.soa.caravan.hystrix.isolator.DefaultIsolator;
import com.ctrip.soa.caravan.hystrix.isolator.Isolator;
import com.ctrip.soa.caravan.hystrix.metrics.DefaultExecutionMetrics;
import com.ctrip.soa.caravan.hystrix.metrics.ExecutionMetrics;
import com.ctrip.soa.caravan.hystrix.util.KeyManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultExecutionCommand implements ExecutionCommand {

    private String _managerId;
    private String _groupId;
    private String _commandId;
    private String _commandKey;
    private CommandConfig _config;
    private ExecutionMetrics _metrics;
    private CircuitBreaker _circuitBreaker;
    private Isolator _isolator;

    public DefaultExecutionCommand(String managerId, String groupId, String commandId, final TypedDynamicCachedCorrectedProperties properties,
            final TimeBufferConfig eventCounterConfig, final PercentileBufferConfig executionLatencyConfig) {
        NullArgumentChecker.DEFAULT.check(managerId, "managerId");
        NullArgumentChecker.DEFAULT.check(groupId, "groupId");
        StringArgumentChecker.DEFAULT.check(commandId, "commandId");
        NullArgumentChecker.DEFAULT.check(properties, "properties");
        NullArgumentChecker.DEFAULT.check(eventCounterConfig, "eventCounterConfig");
        NullArgumentChecker.DEFAULT.check(executionLatencyConfig, "executionLatencyConfig");

        _managerId = managerId.trim();
        _groupId = groupId.trim();
        _commandId = commandId.trim();
        _commandKey = KeyManager.getCommandKey(this);

        _config = new DefaultExecutionConfig(this, properties, eventCounterConfig, executionLatencyConfig);
        _metrics = new DefaultExecutionMetrics(this);
        _circuitBreaker = new DefaultCircuitBreaker(this);
        _isolator = new DefaultIsolator(this);
    }

    @Override
    public String groupId() {
        return _groupId;
    }

    @Override
    public String managerId() {
        return _managerId;
    }

    @Override
    public String commandId() {
        return _commandId;
    }

    @Override
    public String commandKey() {
        return _commandKey;
    }

    @Override
    public CommandConfig config() {
        return _config;
    }

    @Override
    public ExecutionMetrics metrics() {
        return _metrics;
    }

    @Override
    public CircuitBreaker circuitBreaker() {
        return _circuitBreaker;
    }

    @Override
    public Isolator isolator() {
        return _isolator;
    }

    @Override
    public ExecutionContext newExecutionContext() {
        return new DefaultExecutionContext(this);
    }

}
