package com.ctrip.soa.caravan.util.safelist;

import java.util.List;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface SafeList<T> {

    String safeListId();

    List<T> list();

    boolean check(T item);

}
