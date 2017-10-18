package com.ctrip.soa.caravan.util.safelist;

import java.util.List;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface SafeListChecker<T> {

    boolean check(List<T> list, T item);

}
