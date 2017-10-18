package com.ctrip.soa.caravan.hystrix.facade;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import com.ctrip.soa.caravan.common.delegate.Func;
import com.ctrip.soa.caravan.common.value.ConcurrentHashMapValues;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.hystrix.DefaultExecutionCommandManager;
import com.ctrip.soa.caravan.hystrix.ExecutionCommandManager;
import com.ctrip.soa.caravan.hystrix.util.ManagerConfig;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public final class CommandManagers {

    private static ConcurrentHashMap<String, ExecutionCommandManager> _managers = new ConcurrentHashMap<>();

    private CommandManagers() {

    }

    public static Collection<ExecutionCommandManager> managers() {
        return Collections.unmodifiableCollection(_managers.values());
    }

    public static ExecutionCommandManager getManager(final String managerId, final ManagerConfig managerConfig) {
        NullArgumentChecker.DEFAULT.check(managerId, "managerId");
        NullArgumentChecker.DEFAULT.check(managerConfig, "managerConfig");

        return ConcurrentHashMapValues.getOrAdd(_managers, managerId, new Func<ExecutionCommandManager>() {
            @Override
            public ExecutionCommandManager execute() {
                return new DefaultExecutionCommandManager(managerId, managerConfig.properties(), managerConfig.eventCounterConfig(),
                        managerConfig.executionLatencyConfig());
            }
        });
    }

}
