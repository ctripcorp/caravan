package com.ctrip.soa.caravan.hystrix;

import com.ctrip.soa.caravan.common.concurrent.collect.circularbuffer.timebucket.PercentileBufferConfig;
import com.ctrip.soa.caravan.common.concurrent.collect.circularbuffer.timebucket.TimeBufferConfig;
import com.ctrip.soa.caravan.configuration.facade.ConfigurationManagers;
import com.ctrip.soa.caravan.configuration.facade.TypedDynamicCachedCorrectedProperties;
import com.ctrip.soa.caravan.configuration.source.memory.MemoryConfigurationSource;
import com.ctrip.soa.caravan.configuration.typed.dynamic.cached.corrected.TypedDynamicCachedCorrectedConfigurationManager;
import com.ctrip.soa.caravan.hystrix.facade.CommandManagers;
import com.ctrip.soa.caravan.hystrix.util.KeyManager;
import com.ctrip.soa.caravan.hystrix.util.ManagerConfig;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public final class HystrixTestUtility {

    public static final TimeBufferConfig EVENT_COUNTER_CONFIG = new TimeBufferConfig(10 * 1000, 1 * 1000);
    public static final PercentileBufferConfig LATENCY_BUFFER_CONFIG = new PercentileBufferConfig(60 * 1000, 10 * 1000, 100);
    public static final TypedDynamicCachedCorrectedProperties PROPERTIES;
    public static final ExecutionCommandManager COMMAND_MANAGER;
    public static final ExecutionCommand TEST_COMMAND;
    public static final String TEST_MANAGER_ID = "test-manager";
    public static final String TEST_COMMAND_GROUP_ID = "test-command-group";
    public static final String TEST_COMMAND_ID = "test-command";
    private static final MemoryConfigurationSource COFNFIGURATION_SOURCE = new MemoryConfigurationSource(1, "test-memory-config");

    static {
        TypedDynamicCachedCorrectedConfigurationManager manager = ConfigurationManagers.newTypedDynamicCachedCorrectedManager(COFNFIGURATION_SOURCE);
        PROPERTIES = new TypedDynamicCachedCorrectedProperties(manager);
        COMMAND_MANAGER = CommandManagers.getManager(TEST_MANAGER_ID, new ManagerConfig(PROPERTIES, EVENT_COUNTER_CONFIG, LATENCY_BUFFER_CONFIG));
        TEST_COMMAND = COMMAND_MANAGER.getCommand(TEST_COMMAND_ID, TEST_COMMAND_GROUP_ID);
    }

    public static void config(ExecutionCommand command, String key, String value) {
        config(command.managerId(), command.groupId(), command.commandId(), key, value);
    }

    public static void config(String managerId, String groupId, String commandId, String key, String value) {
        String propertyKey = KeyManager.getCommandLevelPropertyKey(managerId, groupId, commandId, key);
        COFNFIGURATION_SOURCE.configuration().setPropertyValue(propertyKey, value);
    }

    public static void config(String managerId, String groupId, String key, String value) {
        String propertyKey = KeyManager.getGroupLevelPropertyKey(managerId, groupId, key);
        COFNFIGURATION_SOURCE.configuration().setPropertyValue(propertyKey, value);
    }

    public static void config(String managerId, String key, String value) {
        String propertyKey = KeyManager.getManagerLevelPropertyKey(managerId, key);
        COFNFIGURATION_SOURCE.configuration().setPropertyValue(propertyKey, value);
    }

    public static void clearConfig(ExecutionCommand command, String key) {
        clearConfig(command.managerId(), command.groupId(), command.commandId(), key);
    }

    public static void clearConfig(String managerId, String groupId, String commandId, String key) {
        String propertyKey = KeyManager.getCommandLevelPropertyKey(managerId, groupId, commandId, key);
        COFNFIGURATION_SOURCE.configuration().setPropertyValue(propertyKey, null);
    }

    public static void clearConfig(String managerId, String groupId, String key) {
        String propertyKey = KeyManager.getGroupLevelPropertyKey(managerId, groupId, key);
        COFNFIGURATION_SOURCE.configuration().setPropertyValue(propertyKey, null);
    }

    public static void clearConfig(String managerId, String key) {
        String propertyKey = KeyManager.getManagerLevelPropertyKey(managerId, key);
        COFNFIGURATION_SOURCE.configuration().setPropertyValue(propertyKey, null);
    }

    private HystrixTestUtility() {

    }

}
