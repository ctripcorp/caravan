package com.ctrip.soa.caravan.util.safelist;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import com.ctrip.soa.caravan.common.delegate.Func;
import com.ctrip.soa.caravan.common.value.ConcurrentHashMapValues;
import com.ctrip.soa.caravan.common.value.StringValues;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.common.value.checker.StringArgumentChecker;
import com.ctrip.soa.caravan.common.value.parser.ListParser;
import com.ctrip.soa.caravan.configuration.facade.TypedDynamicCachedCorrectedProperties;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class SafeListManager<T> {

    public static SafeListManager<String> newManager(TypedDynamicCachedCorrectedProperties properties) {
        return new SafeListManager<>(StringValues.EMPTY, new SafeListManagerConfig<>(properties, ListParser.DEFAULT));
    }

    private String _managerId;
    private SafeListManagerConfig<T> _config;
    private ConcurrentHashMap<String, SafeList<T>> _listMap = new ConcurrentHashMap<>();

    public SafeListManager(String managerId, SafeListManagerConfig<T> config) {
        NullArgumentChecker.DEFAULT.check(managerId, "managerId");
        NullArgumentChecker.DEFAULT.check(config, "config");
        _managerId = managerId;
        _config = config;
    }

    public String managerId() {
        return _managerId;
    }

    public SafeListManagerConfig<T> config() {
        return _config;
    }

    public Collection<SafeList<T>> lists() {
        return Collections.unmodifiableCollection(_listMap.values());
    }

    public SafeList<T> getList(String listId, final SafeListConfig<T> config) {
        StringArgumentChecker.DEFAULT.check(listId, "listId");
        NullArgumentChecker.DEFAULT.check(config, "config");

        final String trimmedListId = StringValues.trim(listId);
        return ConcurrentHashMapValues.getOrAdd(_listMap, trimmedListId, new Func<SafeList<T>>() {
            @Override
            public SafeList<T> execute() {
                return new DefaultSafeList<>(trimmedListId, _config.valueParser(), _config.properties(), config);
            }
        });
    }

}
