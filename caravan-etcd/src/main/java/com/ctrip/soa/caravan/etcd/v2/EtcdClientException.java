package com.ctrip.soa.caravan.etcd.v2;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class EtcdClientException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private EtcdResult _result;

    public EtcdClientException(EtcdResult result) {
        super(toErrorMessage(result));
        _result = result;
    }

    private static String toErrorMessage(EtcdResult result) {
        NullArgumentChecker.DEFAULT.check(result, "result");
        return "Request failed. Details: \n" + result.toString();
    }

    public EtcdResult result() {
        return _result;
    }
}
