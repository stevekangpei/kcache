package com.kp.kcache.autoconfigure.support;

import com.kp.kcache.autoconfigure.autoconfiguration.AbstractCacheInitializer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.Arrays;

import static com.kp.kcache.autoconfigure.KCacheAutoconfiguraton.cacheInitializerName;

/**
 * description: BeanDefinitionMetadataManager <br>
 * date: 2021/10/16 10:47 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class BeanDefinitionMetadataManager implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        String[] initializers = configurableListableBeanFactory
                .getBeanNamesForType(AbstractCacheInitializer.class);
        BeanDefinition definition = configurableListableBeanFactory.getBeanDefinition(cacheInitializerName);
        String[] dependsOn = definition.getDependsOn();
        if (dependsOn == null) {
            dependsOn = new String[initializers.length];
        }
        int oldLen = dependsOn.length;
        dependsOn = Arrays.copyOf(dependsOn, dependsOn.length + initializers.length);
        System.arraycopy(initializers, 0, dependsOn, oldLen, initializers.length);
        definition.setDependsOn(dependsOn);
    }
}
