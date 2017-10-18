package com.ctrip.soa.caravan.common.concurrent.collect.circularbuffer.timebucket;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.ctrip.soa.caravan.common.collect.AuditData;
import com.ctrip.soa.caravan.common.delegate.Action1;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class IntegerPercentileBuffer extends PercentileBuffer<Long> {

    public IntegerPercentileBuffer(PercentileBufferConfig bufferConfig) {
        super(bufferConfig);
    }

    public AuditData getAuditData() {
        final AuditData auditData = new AuditData();
        auditData.setMax(Long.MIN_VALUE);
        auditData.setMin(Long.MAX_VALUE);
        visitData(new Action1<Long>() {
            @Override
            public void execute(Long param) {
                if (param == null)
                    return;

                auditData.setCount(auditData.getCount() + 1);
                long value = param.longValue();
                auditData.setSum(auditData.getSum() + value);
                if (value < auditData.getMin())
                    auditData.setMin(value);
                if (value > auditData.getMax())
                    auditData.setMax(value);
            }
        });

        if (auditData.getCount() == 0) {
            auditData.setMax(0);
            auditData.setMin(0);
        }

        return auditData;
    }

    public int getItemCountInRange(long lowerBound) {
        return getItemCountInRange(lowerBound, Long.MAX_VALUE);
    }

    public int getItemCountInRange(final long lowerBound, final long upperBound) {
        final AtomicInteger count = new AtomicInteger(0);
        visitData(new Action1<Long>() {
            @Override
            public void execute(Long param) {
                if (param == null)
                    return;

                long value = param.longValue();
                if (value >= lowerBound && value < upperBound)
                    count.incrementAndGet();
            }
        });

        return count.intValue();
    }

    public long getPercentile(double percent) {
        List<Long> snapshot = getSnapShot();
        Collections.sort(snapshot);

        if (snapshot.size() <= 0)
            return 0;

        Long dataItem = null;
        if (percent <= 0.0)
            dataItem = snapshot.get(0);
        else if (percent >= 100.0)
            dataItem = snapshot.get(snapshot.size() - 1);
        else {
            int rank = (int) (percent * (snapshot.size() - 1) / 100);
            dataItem = snapshot.get(rank);
        }

        return dataItem == null ? 0 : dataItem.longValue();
    }

}
