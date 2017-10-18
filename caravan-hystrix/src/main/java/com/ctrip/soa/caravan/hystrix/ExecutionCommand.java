package com.ctrip.soa.caravan.hystrix;

import com.ctrip.soa.caravan.hystrix.circuitbreaker.CircuitBreaker;
import com.ctrip.soa.caravan.hystrix.config.CommandConfig;
import com.ctrip.soa.caravan.hystrix.isolator.Isolator;
import com.ctrip.soa.caravan.hystrix.metrics.ExecutionMetrics;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface ExecutionCommand {

    String managerId();

    String groupId();

    String commandId();

    String commandKey();

    CommandConfig config();

    ExecutionMetrics metrics();

    CircuitBreaker circuitBreaker();

    Isolator isolator();

    ExecutionContext newExecutionContext();

}
