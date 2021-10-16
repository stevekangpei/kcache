package com.kp.kcache.autoconfigure.support;

import com.kp.cache_core.cache_builder.embedded.EmbeddedCacheBuilder;
import com.kp.cache_core.cache_builder.external.ExternalCacheBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * description: CacheContainer <br>
 * date: 2021/9/26 7:43 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
@Component
public class CacheBuilderContainer {

    private Map<String, EmbeddedCacheBuilder> embeddedCacheBuilderMap = new HashMap<>();

    private Map<String, ExternalCacheBuilder> externalCacheBuilderMap = new HashMap<>();

    public void addEmbeddedContainer(String key, EmbeddedCacheBuilder embeddedCacheBuilder) {
        this.embeddedCacheBuilderMap.put(key, embeddedCacheBuilder);
    }

    public EmbeddedCacheBuilder getEmbeddedCacheBuilder(String key) {
        if (StringUtils.isEmpty(key)) return null;
        return this.embeddedCacheBuilderMap.get(key);
    }

    public Map<String, EmbeddedCacheBuilder> getAllEmbeddedBuilders() {
        return this.embeddedCacheBuilderMap;
    }

    public Map<String, ExternalCacheBuilder> getAllExternalBuilders() {
        return this.externalCacheBuilderMap;
    }

    public ExternalCacheBuilder getExternalCacheBuilder(String key) {
        if (StringUtils.isEmpty(key)) return null;
        return this.externalCacheBuilderMap.get(key);
    }

    public void addExternalContainer(String key, ExternalCacheBuilder externalCacheBuilder) {
        this.externalCacheBuilderMap.put(key, externalCacheBuilder);
    }

    public Map<String, EmbeddedCacheBuilder> getEmbeddedCacheBuilderMap() {
        return embeddedCacheBuilderMap;
    }

    public void setEmbeddedCacheBuilderMap(Map<String, EmbeddedCacheBuilder> embeddedCacheBuilderMap) {
        this.embeddedCacheBuilderMap = embeddedCacheBuilderMap;
    }

    public Map<String, ExternalCacheBuilder> getExternalCacheBuilderMap() {
        return externalCacheBuilderMap;
    }

    public void setExternalCacheBuilderMap(Map<String, ExternalCacheBuilder> externalCacheBuilderMap) {
        this.externalCacheBuilderMap = externalCacheBuilderMap;
    }
}
