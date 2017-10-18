package com.ctrip.soa.caravan.common.concurrent.collect.circularbuffer.timebucket;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class PercentileBufferConfig extends TimeBufferConfig {

    private int _bucketCapacity;

    public PercentileBufferConfig(long bufferTimeWindow, long bucketTimeWindow, int bucketCapacity) {
        super(bufferTimeWindow, bucketTimeWindow);

        if (bucketCapacity <= 0)
            throw new IllegalArgumentException("Bucket capacity cannot be <= 0.");

        _bucketCapacity = bucketCapacity;
    }

    public int bucketCapacity() {
        return _bucketCapacity;
    }

}
