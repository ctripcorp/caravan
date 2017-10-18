package com.ctrip.soa.caravan.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.soa.caravan.common.value.CollectionValues;
import com.ctrip.soa.caravan.configuration.util.ConfigurationSourceComparator;
import com.google.common.collect.Lists;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultConfigurationManager implements ConfigurationManager {

    private static final Logger _logger = LoggerFactory.getLogger(DefaultConfigurationManager.class);

    protected static final List<ConfigurationSource> EMPTY_CONFIGURATION_SOURCE_LIST = new ArrayList<>();

    private List<ConfigurationSource> _sources;

    public DefaultConfigurationManager(ConfigurationSource... sources) {
        init(Lists.newArrayList(sources));
    }

    protected Property newProperty(String key) {
        return new DefaultProperty(this, key);
    }

    protected void init(List<ConfigurationSource> sources) {
        if (CollectionValues.isNullOrEmpty(sources)) {
            _sources = EMPTY_CONFIGURATION_SOURCE_LIST;
            _logger.warn("sources is null or empty");
            return;
        }

        _sources = new ArrayList<ConfigurationSource>();
        for (ConfigurationSource source : sources) {
            if (source == null || source.configuration() == null)
                continue;

            _sources.add(source);
        }

        Collections.sort(_sources, ConfigurationSourceComparator.DEFAULT);

        StringBuffer sourcesInfo = new StringBuffer("The sorted sources info: { ");
        for (ConfigurationSource cs : sources()) {
            sourcesInfo.append(cs.sourceId()).append(": ").append(cs.priority()).append(", ");
        }
        sourcesInfo = new StringBuffer(sourcesInfo.substring(0, sourcesInfo.length() - 2)).append(" }");
        _logger.info(sourcesInfo.toString());
    }

    @Override
    public String getPropertyValue(String key) {
        if (key == null)
            return null;

        for (ConfigurationSource source : sources()) {
            String value = source.configuration().getPropertyValue(key);
            if (value != null) {
                _logger.info(String.format("The %s=%s in source: %s has been found.", key, value, source.sourceId()));
                return value;
            }
        }

        return null;
    }

    @Override
    public Property getProperty(String key) {
        if (key == null)
            return NullProperty.INSTANCE;

        return newProperty(key);
    }

    @Override
    public Collection<ConfigurationSource> sources() {
        return Collections.unmodifiableCollection(_sources);
    }
}