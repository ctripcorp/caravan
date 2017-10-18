package com.ctrip.soa.caravan.hystrix;

import java.util.Collection;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface ExecutionCommandManager {

    String managerId();

    ExecutionCommand getCommand(String commandId);

    ExecutionCommand getCommand(String commandId, String groupId);

    Collection<ExecutionCommand> commands();

    void reset();

}
