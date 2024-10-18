package com.graceful.place.search.application.service;

import javax.cache.Cache;
import javax.cache.CacheManager;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class KeywordCachingService {

	private final Cache<String, Integer> keywordCountCache;

	public KeywordCachingService(@Qualifier("ehCacheManager") CacheManager cacheManager) {
		this.keywordCountCache = cacheManager.getCache("PLACE_SEARCH_KEYWORD_COUNTER", String.class, Integer.class);
	}

	public Integer incrementAndGetCount(String keyword) {
		return keywordCountCache.invoke(keyword, ((entry, objects) -> {
			Integer currentCount = (Integer) entry.getValue();
			Integer newCount = currentCount == null ? 1 : currentCount + 1;
			entry.setValue(newCount);
			return newCount;
		}));
	}
}
