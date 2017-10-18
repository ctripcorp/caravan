package com.ctrip.soa.caravan.ribbon.rule;

import com.ctrip.soa.caravan.common.value.CollectionValues;
import com.ctrip.soa.caravan.ribbon.LoadBalancerRoute;
import com.ctrip.soa.caravan.ribbon.ServerGroup;
import com.ctrip.soa.caravan.ribbon.util.Math;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by w.jian on 2017/2/25.
 */
public class WeightedRoundRobinRule extends RoundRobinRule {

    @Override
    public String getRuleId() {
        return DefaultLoadBalancerRuleFactoryManager.WEIGHT_ROUND_ROBIN_RULE_NAME;
    }

    @Override
    public String getDescription() {
        return DefaultLoadBalancerRuleFactoryManager.WEIGHT_ROUND_ROBIN_RULE_DESCRIPTION;
    }

    @Override
    protected List<ServerGroup> buildServerGroupContext(LoadBalancerRoute route) {
        int weightGCD = getWeightGCD(route.getServerGroups());
        List<ServerGroup> newLoadBalanceItems = new ArrayList<>();
        for (ServerGroup serverGroup : route.getServerGroups()) {
            int weight = serverGroup.getWeight() / weightGCD;
            for (int i = 0; i < weight; i++) {
                newLoadBalanceItems.add(serverGroup);
            }
        }
        shuffle(newLoadBalanceItems);
        return newLoadBalanceItems;
    }

    private <T> List<T> shuffle(List<T> items) {
        if (CollectionValues.isNullOrEmpty(items))
            return items;

        Random random = new Random();
        int n = items.size();
        while (n > 1) {
            --n;
            int k = random.nextInt(n + 1);
            T item = items.get(k);
            items.set(k, items.get(n));
            items.set(n, item);
        }

        return items;
    }

    private int getWeightGCD(List<ServerGroup> serverGroups) {
        int weightGCD = 1, index = 0;
        for (ServerGroup serverGroup : serverGroups) {
            int weight = serverGroup.getWeight();
            if (weight > 0) {
                if (index++ == 0) {
                    weightGCD = weight;
                } else {
                    weightGCD = Math.GCD(weightGCD, weight);
                }
            }
        }
        return weightGCD;
    }
}
