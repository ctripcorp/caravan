package com.ctrip.soa.caravan.hystrix.util;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.configuration.util.PropertyKeyGenerator;
import com.ctrip.soa.caravan.hystrix.ExecutionCommand;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public final class KeyManager {

    private KeyManager() {

    }

    public static String getCommandKey(ExecutionCommand command) {
        NullArgumentChecker.DEFAULT.check(command, "command");
        return getCommandKey(command.managerId(), command.groupId(), command.commandId());
    }

    public static String getCommandKey(String managerId, String groupId, String commandId) {
        return PropertyKeyGenerator.generateKey(managerId, groupId, commandId);
    }

    public static String getManagerLevelPropertyKey(ExecutionCommand command, String propertyKey) {
        NullArgumentChecker.DEFAULT.check(command, "command");
        return getManagerLevelPropertyKey(command.managerId(), propertyKey);
    }

    public static String getManagerLevelPropertyKey(String managerId, String propertyKey) {
        return PropertyKeyGenerator.generateKey(managerId, propertyKey);
    }

    public static String getGroupLevelPropertyKey(ExecutionCommand command, String propertyKey) {
        NullArgumentChecker.DEFAULT.check(command, "command");
        return getGroupLevelPropertyKey(command.managerId(), command.groupId(), propertyKey);
    }

    public static String getGroupLevelPropertyKey(String managerId, String groupId, String propertyKey) {
        return PropertyKeyGenerator.generateKey(managerId, groupId, propertyKey);
    }

    public static String getCommandLevelPropertyKey(ExecutionCommand command, String propertyKey) {
        NullArgumentChecker.DEFAULT.check(command, "command");
        return getCommandLevelPropertyKey(command.managerId(), command.groupId(), command.commandId(), propertyKey);
    }

    public static String getCommandLevelPropertyKey(String managerId, String groupId, String commandId, String propertyKey) {
        return PropertyKeyGenerator.generateKey(managerId, groupId, commandId, propertyKey);
    }

}
