package com.ctrip.soa.caravan.ribbon.algorithm;

import java.util.List;

/**
 * Created by w.jian on 2017/2/25.
 */
public interface RoundRobinAlgorithm<T> {
    T choose(List<T> source);
}
