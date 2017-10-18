package com.ctrip.soa.caravan.hystrix;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import com.ctrip.soa.caravan.common.concurrent.collect.circularbuffer.timebucket.PercentileBufferConfig;
import com.ctrip.soa.caravan.common.concurrent.collect.circularbuffer.timebucket.TimeBufferConfig;
import com.ctrip.soa.caravan.common.delegate.Func;
import com.ctrip.soa.caravan.common.value.ConcurrentHashMapValues;
import com.ctrip.soa.caravan.common.value.StringValues;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.configuration.facade.TypedDynamicCachedCorrectedProperties;
import com.ctrip.soa.caravan.hystrix.util.KeyManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultExecutionCommandManager implements ExecutionCommandManager {

    private String _managerId;
    private TypedDynamicCachedCorrectedProperties _properties;
    private TimeBufferConfig _eventCounterConfig;
    private PercentileBufferConfig _executionLatencyConfig;

    private volatile ConcurrentHashMap<String, ExecutionCommand> _commands = new ConcurrentHashMap<>();

    public DefaultExecutionCommandManager(String managerId, TypedDynamicCachedCorrectedProperties properties, TimeBufferConfig eventCounterConfig,
            PercentileBufferConfig executionLatencyConfig) {
        NullArgumentChecker.DEFAULT.check(managerId, "managerId");
        NullArgumentChecker.DEFAULT.check(properties, "properties");
        NullArgumentChecker.DEFAULT.check(eventCounterConfig, "eventCounterConfig");
        NullArgumentChecker.DEFAULT.check(executionLatencyConfig, "executionLatencyConfig");

        _managerId = StringValues.trim(managerId);
        _properties = properties;
        _eventCounterConfig = eventCounterConfig;
        _executionLatencyConfig = executionLatencyConfig;
    }

    @Override
    public String managerId() {
        return _managerId;
    }

    @Override
    public Collection<ExecutionCommand> commands() {
        return Collections.unmodifiableCollection(_commands.values());
    }

    @Override
    public ExecutionCommand getCommand(final String commandId) {
        return getCommand(commandId, StringValues.EMPTY);
    }

    @Override
    public ExecutionCommand getCommand(final String commandId, final String groupId) {
        String key = KeyManager.getCommandKey(_managerId, groupId, commandId);
        return ConcurrentHashMapValues.getOrAdd(_commands, key, new Func<ExecutionCommand>() {
            @Override
            public ExecutionCommand execute() {
                return new DefaultExecutionCommand(_managerId, groupId, commandId, _properties, _eventCounterConfig, _executionLatencyConfig);
            }
        });
    }

    @Override
    public void reset() {
        _commands = new ConcurrentHashMap<>();
    }

}
