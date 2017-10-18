package com.ctrip.soa.caravan.hystrix;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface ValidationFailChecker {

    boolean isValidationFail(Throwable ex);

}
