package com.ctrip.soa.caravan.etcd.v2;

import com.ctrip.soa.caravan.common.value.StringValues;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.common.value.checker.StringArgumentChecker;
import com.ctrip.soa.caravan.util.net.apache.DynamicPoolingHttpClientProvider;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class EtcdClientConfig {

    private String _etcdServiceUrl;
    private DynamicPoolingHttpClientProvider _clientProvider;

    public EtcdClientConfig(String etcdServiceUrl, DynamicPoolingHttpClientProvider clientProvider) {
        StringArgumentChecker.DEFAULT.check(etcdServiceUrl, "etcdServiceUrl");
        NullArgumentChecker.DEFAULT.check(clientProvider, "clientProvider");
        _etcdServiceUrl = StringValues.trim(etcdServiceUrl);
        _clientProvider = clientProvider;
    }

    public String etcdServiceUrl() {
        return _etcdServiceUrl;
    }

    public DynamicPoolingHttpClientProvider clientProvider() {
        return _clientProvider;
    }

}
