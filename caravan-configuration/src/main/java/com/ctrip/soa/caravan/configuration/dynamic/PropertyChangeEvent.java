package com.ctrip.soa.caravan.configuration.dynamic;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface PropertyChangeEvent extends ChangeEvent {

    String key();

    String oldValue();

    String newValue();

}
