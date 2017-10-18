package com.ctrip.soa.caravan.configuration.facade;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.soa.caravan.common.concurrent.ThreadFactories;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.common.value.corrector.DefaultValueCorrector;
import com.ctrip.soa.caravan.common.value.corrector.PipelineCorrector;
import com.ctrip.soa.caravan.common.value.corrector.RangeCorrector;
import com.ctrip.soa.caravan.common.value.corrector.ValueCorrector;
import com.ctrip.soa.caravan.common.value.parser.BooleanParser;
import com.ctrip.soa.caravan.common.value.parser.IntegerParser;
import com.ctrip.soa.caravan.common.value.parser.ListMultimapParser;
import com.ctrip.soa.caravan.common.value.parser.ListParser;
import com.ctrip.soa.caravan.common.value.parser.LongParser;
import com.ctrip.soa.caravan.common.value.parser.MapParser;
import com.ctrip.soa.caravan.common.value.parser.StringParser;
import com.ctrip.soa.caravan.common.value.parser.ValueParser;
import com.ctrip.soa.caravan.configuration.ConfigurationSource;
import com.ctrip.soa.caravan.configuration.typed.TypedProperty;
import com.ctrip.soa.caravan.configuration.typed.dynamic.cached.TypedDynamicCachedProperty;
import com.ctrip.soa.caravan.configuration.typed.dynamic.cached.corrected.TypedDynamicCachedCorrectedConfigurationManager;
import com.ctrip.soa.caravan.configuration.typed.dynamic.cached.corrected.TypedDynamicCachedCorrectedProperty;
import com.ctrip.soa.caravan.configuration.util.RangePropertyConfig;
import com.google.common.collect.ListMultimap;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class TypedDynamicCachedCorrectedProperties {

    private static final Logger _logger = LoggerFactory.getLogger(TypedDynamicCachedCorrectedProperties.class);

    private TypedDynamicCachedCorrectedConfigurationManager _manager;

    private ConcurrentHashMap<String, WeakReference<?>> _parserOnlyTypedProperties = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, WeakReference<?>> _parserAndCorrectorTypedProperties = new ConcurrentHashMap<>();

    private ScheduledExecutorService _refCleanExecutorService = Executors.newSingleThreadScheduledExecutor(ThreadFactories.DEFAULT);

    public TypedDynamicCachedCorrectedProperties(List<ConfigurationSource> sources) {
        this(sources.toArray(new ConfigurationSource[0]));
    }

    public TypedDynamicCachedCorrectedProperties(ConfigurationSource... sources) {
        this(ConfigurationManagers.newTypedDynamicCachedCorrectedManager(sources));
    }

    public TypedDynamicCachedCorrectedProperties(TypedDynamicCachedCorrectedConfigurationManager manager) {
        NullArgumentChecker.DEFAULT.check(manager, "manager");
        _manager = manager;
        _refCleanExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                cleanUselessRef();
            }
        }, 60 * 1000, TimeUnit.MILLISECONDS);
    }

    public TypedDynamicCachedCorrectedConfigurationManager manager() {
        return _manager;
    }

    public Collection<TypedProperty<?>> typedProperties() {
        List<TypedProperty<?>> typedProperties = new ArrayList<>();
        List<WeakReference<?>> refs = new ArrayList<>(_parserOnlyTypedProperties.values());
        refs.addAll(_parserAndCorrectorTypedProperties.values());
        for (WeakReference<?> ref : refs) {
            TypedProperty<?> typedProperty = (TypedProperty<?>) ref.get();
            if (typedProperty == null)
                continue;

            typedProperties.add(typedProperty);
        }

        return typedProperties;
    }

    @SuppressWarnings("unchecked")
    public <T> TypedDynamicCachedProperty<T> getProperty(final String key, final ValueParser<T> valueParser) {
        WeakReference<?> propertyRef = _parserOnlyTypedProperties.get(key);
        TypedDynamicCachedProperty<T> property = propertyRef == null ? null : (TypedDynamicCachedProperty<T>) propertyRef.get();
        if (property == null) {
            synchronized (this) {
                propertyRef = _parserOnlyTypedProperties.get(key);
                property = propertyRef == null ? null : (TypedDynamicCachedProperty<T>) propertyRef.get();
                if (property == null) {
                    property = _manager.getProperty(key, valueParser);
                    WeakReference<TypedDynamicCachedProperty<T>> ref = new WeakReference<TypedDynamicCachedProperty<T>>(property);
                    _parserOnlyTypedProperties.put(key, ref);
                }
            }
        }

        return property;
    }

    @SuppressWarnings("unchecked")
    public <T> TypedDynamicCachedCorrectedProperty<T> getProperty(final String key, final ValueParser<T> valueParser, final ValueCorrector<T> valueCorrector) {
        WeakReference<?> propertyRef = _parserAndCorrectorTypedProperties.get(key);
        TypedDynamicCachedCorrectedProperty<T> property = propertyRef == null ? null : (TypedDynamicCachedCorrectedProperty<T>) propertyRef.get();
        if (property == null) {
            synchronized (this) {
                propertyRef = _parserAndCorrectorTypedProperties.get(key);
                property = propertyRef == null ? null : (TypedDynamicCachedCorrectedProperty<T>) propertyRef.get();
                if (property == null) {
                    property = _manager.getProperty(key, valueParser, valueCorrector);
                    WeakReference<TypedDynamicCachedCorrectedProperty<T>> ref = new WeakReference<TypedDynamicCachedCorrectedProperty<T>>(property);
                    _parserAndCorrectorTypedProperties.put(key, ref);
                }
            }
        }

        return property;
    }

    public <T> TypedDynamicCachedCorrectedProperty<T> getProperty(String key, ValueParser<T> valueParser, T defaultValue) {
        return getProperty(key, valueParser, new DefaultValueCorrector<T>(defaultValue));
    }

    public <T extends Comparable<T>> TypedDynamicCachedCorrectedProperty<T> getProperty(String key, ValueParser<T> valueParser,
            RangePropertyConfig<T> propertyConfig) {
        NullArgumentChecker.DEFAULT.check(propertyConfig, "propertyConfig");
        ValueCorrector<T> valueCorrector = new PipelineCorrector<>(new DefaultValueCorrector<>(propertyConfig.defaultValue()),
                new RangeCorrector<>(propertyConfig.lowerBound(), propertyConfig.upperBound()));
        return getProperty(key, valueParser, valueCorrector);
    }

    public <T extends Comparable<T>> TypedDynamicCachedCorrectedProperty<T> getProperty(String key, ValueParser<T> valueParser, T defaultValue, T lowerBound,
            T upperBound) {
        RangePropertyConfig<T> propertyConfig = new RangePropertyConfig<T>(defaultValue, lowerBound, upperBound);
        return getProperty(key, valueParser, propertyConfig);
    }

    public TypedDynamicCachedProperty<String> getStringProperty(String key) {
        return getProperty(key, StringParser.DEFAULT);
    }

    public TypedDynamicCachedProperty<Boolean> getBooleanProperty(String key) {
        return getProperty(key, BooleanParser.DEFAULT);
    }

    public TypedDynamicCachedProperty<Integer> getIntProperty(String key) {
        return getProperty(key, IntegerParser.DEFAULT);
    }

    public TypedDynamicCachedProperty<Long> getLongProperty(String key) {
        return getProperty(key, LongParser.DEFAULT);
    }

    public TypedDynamicCachedProperty<List<String>> getListProperty(String key) {
        return getProperty(key, ListParser.DEFAULT);
    }

    public TypedDynamicCachedProperty<Map<String, String>> getMapProperty(String key) {
        return getProperty(key, MapParser.DEFAULT);
    }

    public TypedDynamicCachedProperty<ListMultimap<String, String>> getListMultimapProperty(String key) {
        return getProperty(key, ListMultimapParser.DEFAULT);
    }

    public TypedDynamicCachedCorrectedProperty<String> getStringProperty(String key, String defaultValue) {
        return getProperty(key, StringParser.DEFAULT, defaultValue);
    }

    public TypedDynamicCachedCorrectedProperty<Boolean> getBooleanProperty(String key, boolean defaultValue) {
        return getProperty(key, BooleanParser.DEFAULT, defaultValue);
    }

    public TypedDynamicCachedCorrectedProperty<Integer> getIntProperty(String key, int defaultValue) {
        return getProperty(key, IntegerParser.DEFAULT, defaultValue);
    }

    public TypedDynamicCachedCorrectedProperty<Long> getLongProperty(String key, long defaultValue) {
        return getProperty(key, LongParser.DEFAULT, defaultValue);
    }

    public TypedDynamicCachedCorrectedProperty<List<String>> getListProperty(String key, List<String> defaultValue) {
        return getProperty(key, ListParser.DEFAULT, defaultValue);
    }

    public TypedDynamicCachedCorrectedProperty<Map<String, String>> getMapProperty(String key, Map<String, String> defaultValue) {
        return getProperty(key, MapParser.DEFAULT, defaultValue);
    }

    public TypedDynamicCachedCorrectedProperty<ListMultimap<String, String>> getListMultimapProperty(String key, ListMultimap<String, String> defaultValue) {
        return getProperty(key, ListMultimapParser.DEFAULT, defaultValue);
    }

    public TypedDynamicCachedCorrectedProperty<Integer> getIntProperty(String key, Integer defaultValue, int lowerBound, int upperBound) {
        return getProperty(key, IntegerParser.DEFAULT, defaultValue, lowerBound, upperBound);
    }

    public TypedDynamicCachedCorrectedProperty<Long> getLongProperty(String key, Long defaultValue, long lowerBound, long upperBound) {
        return getProperty(key, LongParser.DEFAULT, defaultValue, lowerBound, upperBound);
    }

    public TypedDynamicCachedCorrectedProperty<Integer> getIntProperty(String key, RangePropertyConfig<Integer> propertyConfig) {
        return getProperty(key, IntegerParser.DEFAULT, propertyConfig);
    }

    public TypedDynamicCachedCorrectedProperty<Long> getLongProperty(String key, RangePropertyConfig<Long> propertyConfig) {
        return getProperty(key, LongParser.DEFAULT, propertyConfig);
    }

    private synchronized void cleanUselessRef() {
        try {
            cleanUselessRef(_parserOnlyTypedProperties);
            cleanUselessRef(_parserAndCorrectorTypedProperties);
        } catch (Throwable ex) {
            _logger.warn("Clean useless ref failed", ex);
        }
    }

    private void cleanUselessRef(ConcurrentHashMap<String, WeakReference<?>> referenceMap) {
        List<String> uselessKeys = new ArrayList<>();
        for (String key : referenceMap.keySet()) {
            Object data = referenceMap.get(key);
            if (data == null)
                uselessKeys.add(key);
        }

        if (uselessKeys.size() == 0)
            return;

        for (String key : uselessKeys)
            referenceMap.remove(key);

        _logger.info(uselessKeys.size() + " useless keys were cleaned.");
    }

}
