package com.ctrip.soa.caravan.common.concurrent.collect.circularbuffer.timebucket;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class TimeBufferConfig {

    private long _bufferTimeWindow;
    private long _bucketTimeWindow;
    private int _bucketCount;

    public TimeBufferConfig(long bufferTimeWindow, long bucketTimeWindow) {
        if (bufferTimeWindow <= 0)
            throw new IllegalArgumentException("Buffer time window cannot be <= 0.");

        if (bucketTimeWindow <= 0)
            throw new IllegalArgumentException("Bucket time window cannot be <= 0.");

        long bucketCount = bufferTimeWindow / bucketTimeWindow;
        if (bucketCount * bucketTimeWindow != bufferTimeWindow)
            throw new IllegalArgumentException(
                    String.format("Buffer time window %s cannot be divided by bucket time window %s.", bufferTimeWindow,
                            bucketTimeWindow));

        if (bucketCount > Integer.MAX_VALUE)
            throw new IllegalArgumentException(
                    String.format("bucket count %s cannot be > Integer.MAX_VALUE", bucketCount));

        _bufferTimeWindow = bufferTimeWindow;
        _bucketTimeWindow = bucketTimeWindow;
        _bucketCount = (int) bucketCount;
    }

    public long bufferTimeWindow() {
        return _bufferTimeWindow;
    }

    public long bucketTimeWindow() {
        return _bucketTimeWindow;
    }

    public int bucketCount() {
        return _bucketCount;
    }

}
