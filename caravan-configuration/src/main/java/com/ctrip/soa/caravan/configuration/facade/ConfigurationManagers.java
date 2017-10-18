package com.ctrip.soa.caravan.configuration.facade;

import com.ctrip.soa.caravan.configuration.ConfigurationManager;
import com.ctrip.soa.caravan.configuration.ConfigurationSource;
import com.ctrip.soa.caravan.configuration.DefaultConfigurationManager;
import com.ctrip.soa.caravan.configuration.cached.CachedConfigurationManager;
import com.ctrip.soa.caravan.configuration.cached.DefaultCachedConfigurationManager;
import com.ctrip.soa.caravan.configuration.cached.corrected.CachedCorrectedConfigurationManager;
import com.ctrip.soa.caravan.configuration.cached.corrected.DefaultCachedCorrectedConfigurationManager;
import com.ctrip.soa.caravan.configuration.corrected.CorrectedConfigurationManager;
import com.ctrip.soa.caravan.configuration.corrected.DefaultCorrectedConfigurationManager;
import com.ctrip.soa.caravan.configuration.defaultvalue.DefaultValueConfigurationManager;
import com.ctrip.soa.caravan.configuration.dynamic.DefaultDynamicConfigurationManager;
import com.ctrip.soa.caravan.configuration.dynamic.DynamicConfigurationManager;
import com.ctrip.soa.caravan.configuration.dynamic.cached.DefaultDynamicCachedConfigurationManager;
import com.ctrip.soa.caravan.configuration.dynamic.cached.DynamicCachedConfigurationManager;
import com.ctrip.soa.caravan.configuration.dynamic.cached.corrected.DefaultDynamicCachedCorrectedConfigurationManager;
import com.ctrip.soa.caravan.configuration.dynamic.cached.corrected.DynamicCachedCorrectedConfigurationManager;
import com.ctrip.soa.caravan.configuration.dynamic.corrected.DefaultDynamicCorrectedConfigurationManager;
import com.ctrip.soa.caravan.configuration.dynamic.corrected.DynamicCorrectedConfigurationManager;
import com.ctrip.soa.caravan.configuration.typed.DefaultTypedConfigurationManager;
import com.ctrip.soa.caravan.configuration.typed.TypedConfigurationManager;
import com.ctrip.soa.caravan.configuration.typed.cached.DefaultTypedCachedConfigurationManager;
import com.ctrip.soa.caravan.configuration.typed.cached.TypedCachedConfigurationManager;
import com.ctrip.soa.caravan.configuration.typed.cached.corrected.DefaultTypedCachedCorrectedConfigurationManager;
import com.ctrip.soa.caravan.configuration.typed.cached.corrected.TypedCachedCorrectedConfigurationManager;
import com.ctrip.soa.caravan.configuration.typed.corrected.DefaultTypedCorrectedConfigurationManager;
import com.ctrip.soa.caravan.configuration.typed.corrected.TypedCorrectedConfigurationManager;
import com.ctrip.soa.caravan.configuration.typed.dynamic.DefaultTypedDynamicConfigurationManager;
import com.ctrip.soa.caravan.configuration.typed.dynamic.TypedDynamicConfigurationManager;
import com.ctrip.soa.caravan.configuration.typed.dynamic.cached.DefaultTypedDynamicCachedConfigurationManager;
import com.ctrip.soa.caravan.configuration.typed.dynamic.cached.TypedDynamicCachedConfigurationManager;
import com.ctrip.soa.caravan.configuration.typed.dynamic.cached.corrected.DefaultTypedDynamicCachedCorrectedConfigurationManager;
import com.ctrip.soa.caravan.configuration.typed.dynamic.cached.corrected.TypedDynamicCachedCorrectedConfigurationManager;
import com.ctrip.soa.caravan.configuration.typed.dynamic.corrected.DefaultTypedDynamicCorrectedConfigurationManager;
import com.ctrip.soa.caravan.configuration.typed.dynamic.corrected.TypedDynamicCorrectedConfigurationManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class ConfigurationManagers {

    protected ConfigurationManagers() {
    }

    public static ConfigurationManager newManager(ConfigurationSource... sources) {
        return new DefaultConfigurationManager(sources);
    }

    public static CachedConfigurationManager newCachedManager(ConfigurationSource... sources) {
        return newCachedManager(newManager(sources));
    }

    public static CachedConfigurationManager newCachedManager(ConfigurationManager manager) {
        return new DefaultCachedConfigurationManager(manager);
    }

    public static CorrectedConfigurationManager newCorrectedManager(ConfigurationSource... sources) {
        return newCorrectedManager(newManager(sources));
    }

    public static CorrectedConfigurationManager newCorrectedManager(ConfigurationManager manager) {
        return new DefaultCorrectedConfigurationManager(manager);
    }

    public static CachedCorrectedConfigurationManager newCachedCorrectedManager(ConfigurationSource... sources) {
        return newCachedCorrectedManager(newManager(sources));
    }

    public static CachedCorrectedConfigurationManager newCachedCorrectedManager(ConfigurationManager manager) {
        return new DefaultCachedCorrectedConfigurationManager(newCorrectedManager(manager));
    }

    public static DynamicConfigurationManager newDynamicManager(ConfigurationSource... sources) {
        return newDynamicManager(newManager(sources));
    }

    public static DynamicConfigurationManager newDynamicManager(ConfigurationManager manager) {
        return new DefaultDynamicConfigurationManager(manager);
    }

    public static DynamicCorrectedConfigurationManager newDynamicCorrectedManager(ConfigurationSource... sources) {
        return newDynamicCorrectedManager(newCorrectedManager(sources));
    }

    public static DynamicCorrectedConfigurationManager newDynamicCorrectedManager(
            CorrectedConfigurationManager manager) {
        return new DefaultDynamicCorrectedConfigurationManager(manager);
    }

    public static DynamicCachedConfigurationManager newDynamicCachedManager(ConfigurationSource... sources) {
        return newDynamicCachedManager(newManager(sources));
    }

    public static DynamicCachedConfigurationManager newDynamicCachedManager(ConfigurationManager manager) {
        return new DefaultDynamicCachedConfigurationManager(manager);
    }

    public static DynamicCachedCorrectedConfigurationManager newDynamicCachedCorrectedManager(
            ConfigurationSource... sources) {
        return newDynamicCachedCorrectedManager(newManager(sources));
    }

    public static DynamicCachedCorrectedConfigurationManager newDynamicCachedCorrectedManager(
            ConfigurationManager manager) {
        return new DefaultDynamicCachedCorrectedConfigurationManager(newCorrectedManager(manager));
    }

    public static TypedConfigurationManager newTypedManager(ConfigurationSource... sources) {
        return newTypedManager(newManager(sources));
    }

    public static TypedConfigurationManager newTypedManager(ConfigurationManager manager) {
        return new DefaultTypedConfigurationManager(manager);
    }

    public static TypedCachedConfigurationManager newTypedCachedManager(ConfigurationSource... sources) {
        return newTypedCachedManager(newCachedManager(sources));
    }

    public static TypedCachedConfigurationManager newTypedCachedManager(CachedConfigurationManager manager) {
        return new DefaultTypedCachedConfigurationManager(manager);
    }

    public static TypedCorrectedConfigurationManager newTypedCorrectedManager(ConfigurationSource... sources) {
        return newTypedCorrectedManager(newCorrectedManager(sources));
    }

    public static TypedCorrectedConfigurationManager newTypedCorrectedManager(CorrectedConfigurationManager manager) {
        return new DefaultTypedCorrectedConfigurationManager(manager);
    }

    public static TypedCachedCorrectedConfigurationManager newTypedCachedCorrectedManager(
            ConfigurationSource... sources) {
        return newTypedCachedCorrectedManager(newCachedCorrectedManager(sources));
    }

    public static TypedCachedCorrectedConfigurationManager newTypedCachedCorrectedManager(
            CachedCorrectedConfigurationManager manager) {
        return new DefaultTypedCachedCorrectedConfigurationManager(manager);
    }

    public static TypedDynamicConfigurationManager newTypedDynamicManager(ConfigurationSource... sources) {
        return newTypedDynamicManager(newDynamicCachedManager(sources));
    }

    public static TypedDynamicConfigurationManager newTypedDynamicManager(DynamicConfigurationManager manager) {
        return new DefaultTypedDynamicConfigurationManager(manager);
    }

    public static TypedDynamicCorrectedConfigurationManager newTypedDynamicCorrectedCachedManager(
            ConfigurationSource... sources) {
        return newTypedDynamicCorrectedManager(newDynamicCorrectedManager(sources));
    }

    public static TypedDynamicCorrectedConfigurationManager newTypedDynamicCorrectedManager(
            DynamicCorrectedConfigurationManager manager) {
        return new DefaultTypedDynamicCorrectedConfigurationManager(manager);
    }

    public static TypedDynamicCachedConfigurationManager newTypedDynamicCachedManager(ConfigurationSource... sources) {
        return newTypedDynamicCachedManager(newDynamicCachedManager(sources));
    }

    public static TypedDynamicCachedConfigurationManager newTypedDynamicCachedManager(
            DynamicCachedConfigurationManager manager) {
        return new DefaultTypedDynamicCachedConfigurationManager(manager);
    }

    public static TypedDynamicCachedCorrectedConfigurationManager newTypedDynamicCachedCorrectedManager(
            ConfigurationSource... sources) {
        return newTypedDynamicCachedCorrectedManager(newDynamicCachedCorrectedManager(sources));
    }

    public static TypedDynamicCachedCorrectedConfigurationManager newTypedDynamicCachedCorrectedManager(
            DynamicCachedCorrectedConfigurationManager manager) {
        return new DefaultTypedDynamicCachedCorrectedConfigurationManager(manager);
    }

    public static DefaultValueConfigurationManager newDefaultValueManager(ConfigurationSource... sources) {
        return newDefaultValueManager(newManager(sources));
    }

    public static DefaultValueConfigurationManager newDefaultValueManager(ConfigurationManager manager) {
        return new DefaultValueConfigurationManager(manager);
    }
}
