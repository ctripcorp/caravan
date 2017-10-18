package com.ctrip.soa.caravan.ribbon.util;

import com.ctrip.soa.caravan.common.value.CollectionValues;
import com.ctrip.soa.caravan.common.value.StringValues;
import com.ctrip.soa.caravan.ribbon.LoadBalancerRoute;
import com.ctrip.soa.caravan.ribbon.Server;
import com.ctrip.soa.caravan.ribbon.ServerGroup;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by w.jian on 2017/4/27.
 */
public class LoadBalancerRoutes {
    
    public static List<LoadBalancerRoute> filterInvalidEntities(List<LoadBalancerRoute> routes, Logger logger, Map<String, String> tags) {
        if (CollectionValues.isNullOrEmpty(routes))
            return routes;
    
        List<LoadBalancerRoute> result = new ArrayList<>();
        for (LoadBalancerRoute route : routes) {
            if (route == null) {
                LogUtil.info(logger, "Route is null. Ignored.", tags);
                continue;
            }
            if (StringValues.isNullOrWhitespace(route.getRouteId())) {
                LogUtil.info(logger, "Route id is null or white space. Ignored.", tags);
                continue;
            }
        
            List<ServerGroup> serverGroups = new ArrayList<>();
            for (ServerGroup serverGroup : route.getServerGroups()) {
                if (serverGroup == null) {
                    LogUtil.info(logger, "ServerGroup is null. Ignored.", tags);
                    continue;
                }
                if (StringValues.isNullOrWhitespace(serverGroup.getGroupId())) {
                    LogUtil.info(logger, "ServerGroup id is null or white space. Ignored.", tags);
                    continue;
                }
            
                List<Server> servers = new ArrayList<>();
                for (Server server : serverGroup.getServers()) {
                    if (server == null) {
                        LogUtil.info(logger, "Server is null. Ignored.", tags);
                        continue;
                    }
                    if (StringValues.isNullOrWhitespace(server.getServerId())) {
                        LogUtil.info(logger, "Server id is null or white space. Ignored.", tags);
                        continue;
                    }
                
                    servers.add(server);
                }
            
                serverGroups.add(new ServerGroup(serverGroup.getGroupId(), serverGroup.getWeight(), servers, serverGroup.getMetadata()));
            }
        
            result.add(new LoadBalancerRoute(route.getRouteId(), serverGroups));
        }
        return result;
    }
}
