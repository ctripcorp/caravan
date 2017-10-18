package com.ctrip.soa.caravan.ribbon.util;

/**
 * Created by w.jian on 2017/2/27.
 */
public class Math {
    public static int GCD(int a, int b) {
        int c;
        while (b != 0)
        {
            c = b;
            b = a % b;
            a = c;
        }
        return a;
    }
}