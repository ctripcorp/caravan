package com.ctrip.soa.caravan.ribbon.server;

import com.ctrip.soa.caravan.common.concurrent.collect.circularbuffer.timebucket.CounterBuffer;
import com.ctrip.soa.caravan.common.concurrent.collect.circularbuffer.timebucket.TimeBufferConfig;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.configuration.dynamic.PropertyChangeEvent;
import com.ctrip.soa.caravan.configuration.dynamic.PropertyChangeListener;
import com.ctrip.soa.caravan.configuration.facade.TypedDynamicCachedCorrectedProperties;
import com.ctrip.soa.caravan.configuration.typed.dynamic.cached.TypedDynamicCachedProperty;
import com.ctrip.soa.caravan.configuration.util.RangePropertyConfig;
import com.ctrip.soa.caravan.ribbon.ConfigurationKeys;
import com.ctrip.soa.caravan.ribbon.loadbalancer.LoadBalancerContext;
import com.ctrip.soa.caravan.ribbon.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ctrip.soa.caravan.configuration.util.PropertyKeyGenerator.generateKey;
import static com.ctrip.soa.caravan.configuration.util.PropertyValueGetter.getTypedValue;

/**
 * Created by w.jian on 2016/7/7.
 */
public class ServerStats {

    private static Logger _logger = LoggerFactory.getLogger(ServerStats.class);


    private static TimeBufferConfigParser valueParser = new TimeBufferConfigParser();
    private static TimeBufferConfigCorrector valueCorrector = new TimeBufferConfigCorrector(
            new RangePropertyConfig<>(2000L, 2000L, 20000L), //buffer
            new RangePropertyConfig<>(200L, 200L, 2000L));   //bucket

    private volatile CounterBuffer<Boolean> _counterBuffer;
    private volatile TimeBufferConfig _timeBufferConfig;

    private LoadBalancerContext _loadBalancerContext;
    private TypedDynamicCachedProperty<TimeBufferConfig> _managerLevelTimeBufferConfigProperty;
    private TypedDynamicCachedProperty<TimeBufferConfig> _timeBufferConfigProperty;

    public ServerStats(LoadBalancerContext loadBalancerContext) {
        NullArgumentChecker.DEFAULT.check(loadBalancerContext, "loadBalancerContext");
        _loadBalancerContext = loadBalancerContext;

        TypedDynamicCachedCorrectedProperties properties = loadBalancerContext.properties();
        String managerId = loadBalancerContext.managerId();
        String loadBalancerId = loadBalancerContext.loadBalancerId();

        String managerLevelPropertyKey = generateKey(managerId, ConfigurationKeys.CounterBuffer);
        _managerLevelTimeBufferConfigProperty = properties.getProperty(managerLevelPropertyKey, valueParser, valueCorrector);
        String propertyKey = generateKey(managerId, loadBalancerId, ConfigurationKeys.CounterBuffer);
        _timeBufferConfigProperty = properties.getProperty(propertyKey, valueParser, valueCorrector);

        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void onChange(PropertyChangeEvent event) {
                reset();
            }
        };
        _timeBufferConfigProperty.addChangeListener(listener);
        _managerLevelTimeBufferConfigProperty.addChangeListener(listener);
        reset();
    }

    private TimeBufferConfig getTimeBufferConfig() {
        TimeBufferConfig timeBufferConfig = getTypedValue(_managerLevelTimeBufferConfigProperty, _timeBufferConfigProperty);
        if (timeBufferConfig == null)
            timeBufferConfig = valueCorrector.DefaultValue;
        return timeBufferConfig;
    }

    public void addAvailableCount() {
        _counterBuffer.incrementCount(true);
    }

    public void addUnavailableCount() {
        _counterBuffer.incrementCount(false);
    }

    public long getAvailableCount() {
        return _counterBuffer.getCount(true);
    }

    public long getUnavailableCount() {
        return _counterBuffer.getCount(false);
    }

    public void reset() {
        if (_timeBufferConfig == null) {
            _timeBufferConfig = getTimeBufferConfig();
            _counterBuffer = new CounterBuffer<>(_timeBufferConfig);
            return;
        }

        TimeBufferConfig newConfig = getTimeBufferConfig();
        if (newConfig == null)
            return;
        if (newConfig.bufferTimeWindow() == _timeBufferConfig.bufferTimeWindow() && newConfig.bucketTimeWindow() == _timeBufferConfig.bucketTimeWindow())
            return;

        String message = String.format("Count buffer reset due to config change, new config: %s", toString(newConfig));
        LogUtil.info(_logger, message, _loadBalancerContext.additionalInfo());
        _timeBufferConfig = newConfig;
        _counterBuffer = new CounterBuffer<>(_timeBufferConfig);
    }

    private String toString(TimeBufferConfig config) {
        if (config == null)
            return "null";
        return String.format("{\"BufferTimeWindow\":%d, \"BucketTimeWindow\":%d}",
                config.bufferTimeWindow(), config.bucketTimeWindow());
    }
}
