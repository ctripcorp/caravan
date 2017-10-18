package com.ctrip.soa.caravan.ribbon;

/**
 * Created by w.jian on 2016/6/23.
 */
public class Threads {

    public static void wait(Thread[] threads, int interval) {
        try {
            boolean isAlive = true;
            while (isAlive) {
                isAlive = false;
                for (int i = 0; i < threads.length; i++) {
                    if (threads[i].isAlive()) {
                        isAlive = true;
                        break;
                    }
                }
                Thread.sleep(interval);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
