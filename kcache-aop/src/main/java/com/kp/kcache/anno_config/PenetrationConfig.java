package com.kp.kcache.anno_config;

import java.util.concurrent.TimeUnit;

/**
 * description: PenetrationConfig <br>
 * date: 2021/11/2 7:23 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class PenetrationConfig {

    private int timeOut;

    private TimeUnit timeUnit;

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }
}
