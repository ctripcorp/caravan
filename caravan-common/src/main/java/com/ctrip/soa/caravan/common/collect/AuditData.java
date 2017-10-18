package com.ctrip.soa.caravan.common.collect;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class AuditData {

    private long max;
    private long min;
    private long sum;
    private int count;

    public AuditData() {

    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public long getMin() {
        return min;
    }

    public void setMin(long min) {
        this.min = min;
    }

    public long getSum() {
        return sum;
    }

    public void setSum(long sum) {
        this.sum = sum;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getAvg() {
        int c = this.count;
        if (c == 0)
            return 0;

        return Math.round((double) this.sum / c);
    }

}
