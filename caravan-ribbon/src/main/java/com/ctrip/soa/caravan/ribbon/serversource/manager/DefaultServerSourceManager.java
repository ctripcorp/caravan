package com.ctrip.soa.caravan.ribbon.serversource.manager;

import com.ctrip.soa.caravan.common.value.StringValues;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.configuration.facade.TypedDynamicCachedCorrectedProperties;
import com.ctrip.soa.caravan.configuration.typed.dynamic.cached.corrected.TypedDynamicCachedCorrectedProperty;
import com.ctrip.soa.caravan.ribbon.ConfigurationKeys;
import com.ctrip.soa.caravan.ribbon.LoadBalancerRoute;
import com.ctrip.soa.caravan.ribbon.loadbalancer.LoadBalancerContext;
import com.ctrip.soa.caravan.ribbon.util.LogUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.ctrip.soa.caravan.configuration.util.PropertyKeyGenerator.generateKey;
import static com.ctrip.soa.caravan.configuration.util.PropertyValueGetter.getTypedValue;
import static com.ctrip.soa.caravan.ribbon.util.LoadBalancerRoutes.filterInvalidEntities;

/**
 * Created by w.jian on 2016/7/18.
 */
public class DefaultServerSourceManager implements ServerSourceManager {

    private static Logger _logger = LoggerFactory.getLogger(DefaultServerSourceManager.class);

    private LoadBalancerContext _loadBalancerContext;

    private TypedDynamicCachedCorrectedProperty<String> _managerLevelDataFolderProperty;
    private TypedDynamicCachedCorrectedProperty<String> _dataFolderProperty;

    private ObjectMapper _objectMapper;
    private CollectionType _routeListType;
    
    private List<ServerSourceRestoreListener> _listeners = new CopyOnWriteArrayList<>();

    public DefaultServerSourceManager(LoadBalancerContext loadBalancerContext) {
        NullArgumentChecker.DEFAULT.check(loadBalancerContext, "loadBalancerContext");
        _loadBalancerContext = loadBalancerContext;
        _objectMapper = new ObjectMapper();
        _routeListType = _objectMapper.getTypeFactory().constructCollectionType(List.class, LoadBalancerRoute.class);

        String managerId = loadBalancerContext.managerId();
        TypedDynamicCachedCorrectedProperties properties = loadBalancerContext.properties();
        String propertyKey = generateKey(managerId, ConfigurationKeys.DataFolder);
        _managerLevelDataFolderProperty = properties.getStringProperty(propertyKey, null);

        String loadBalancerId = loadBalancerContext.loadBalancerId();
        propertyKey = generateKey(managerId, loadBalancerId, ConfigurationKeys.DataFolder);
        _dataFolderProperty = properties.getStringProperty(propertyKey, null);
    }

    private String getDataFolder() {
        return getTypedValue(_managerLevelDataFolderProperty, _dataFolderProperty);
    }

    private String getBackupFileName(String dataFolderPath) {
        if (StringValues.isNullOrWhitespace(dataFolderPath))
            return null;

        return StringValues.concatPathParts(dataFolderPath, _loadBalancerContext.loadBalancerKey());
    }
    
    @Override
    public synchronized void backup(List<LoadBalancerRoute> route) {
        String dataFolderPath, backupFileName = null;
        try {
            dataFolderPath = getDataFolder();
            backupFileName = getBackupFileName(dataFolderPath);

            if (StringValues.isNullOrWhitespace(backupFileName)) {
                LogUtil.info(_logger, "Back up file name is null or empty! Backup canceled", _loadBalancerContext.additionalInfo());
                return;
            }

            File dataFolder = new File(dataFolderPath);
            if (!dataFolder.exists()) {
                if (!dataFolder.mkdirs()) {
                    String message = String.format("Failed to create directory! Path:%s", dataFolderPath);
                    LogUtil.info(_logger, message, _loadBalancerContext.additionalInfo());
                    return;
                }
            }

            File backupFile = new File(backupFileName);
            backupFileName = backupFile.getAbsolutePath();
            _objectMapper.writeValue(backupFile, route);
        } catch (Throwable t) {
            String message = String.format("Error occurred while backing up server source data! FileName:%s", backupFileName);
            LogUtil.info(_logger, message, t, _loadBalancerContext.additionalInfo());
        }
    }
    
    @Override
    public synchronized List<LoadBalancerRoute> restore() {
        String dataFolderPath, backupFileName = null;
        try {
            dataFolderPath = getDataFolder();
            backupFileName = getBackupFileName(dataFolderPath);

            if (StringValues.isNullOrWhitespace(backupFileName)) {
                LogUtil.info(_logger, "Back up file name is null or empty! Restore canceled",
                        _loadBalancerContext.additionalInfo());
                return new ArrayList<>();
            }

            File backupFile = new File(backupFileName);
            if (!backupFile.exists()) {
                LogUtil.info(_logger, "Back up file(" + backupFileName + ") is not exist! Restore canceled",
                        _loadBalancerContext.additionalInfo());
                return new ArrayList<>();
            }
            backupFileName = backupFile.getAbsolutePath();
            List<LoadBalancerRoute> routes = _objectMapper.readValue(backupFile, _routeListType);
            routes = filterInvalidEntities(routes, _logger, _loadBalancerContext.additionalInfo());
            ServerSourceRestoreEvent event = new ServerSourceRestoreEvent(routes);
            raisingServerSourceRestoreEvent(event);
            return event.getRoutes();
        } catch (Throwable t) {
            String message = String.format("Error occurred while restoring server source data! FileName:%s", backupFileName);
            LogUtil.info(_logger, message, t, _loadBalancerContext.additionalInfo());
            return new ArrayList<>();
        }
    }
    
    @Override
    public void addRestoreListener(ServerSourceRestoreListener listener) {
        if (listener == null)
            return;
        
        _listeners.add(listener);
    }
    
    private void raisingServerSourceRestoreEvent(ServerSourceRestoreEvent event) {
        if (_listeners.size() == 0)
            return;
    
        for (ServerSourceRestoreListener listener : _listeners) {
            try {
                listener.onServerSourceRestore(event);
            } catch (Throwable t) {
                String message = "Error occurred while raising server source restore event.";
                LogUtil.warn(_logger, message, t, _loadBalancerContext.additionalInfo());
            }
        }
    }
}
