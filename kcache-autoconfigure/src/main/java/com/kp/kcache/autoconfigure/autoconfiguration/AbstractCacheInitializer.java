package com.kp.kcache.autoconfigure.autoconfiguration;

import org.springframework.beans.factory.InitializingBean;

/**
 * 用于初始化Cache。
 * 在初始化bean的时候， 当条件满足的时候。
 * 即比如说满足CaffeineCondition 的时候。
 * 初始化这个Initializer。 用于从配置文件中读取配置信息。
 * 然后创建Cache。
 * description: AbstractCacheInitializer <br>
 * date: 2021/9/23 7:51 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public abstract class AbstractCacheInitializer implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
