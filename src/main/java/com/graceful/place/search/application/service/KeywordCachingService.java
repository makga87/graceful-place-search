package com.graceful.place.search.application.service;


import javax.cache.Cache;
import javax.cache.CacheManager;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.graceful.place.search.application.port.in.KeywordCachingUseCase;

@Service
public class KeywordCachingService implements KeywordCachingUseCase {

	private final Cache<String, Long> keywordCountCache;

	public KeywordCachingService(@Qualifier("ehCacheManager") CacheManager ehCacheManager) {
		this.keywordCountCache = ehCacheManager.getCache("PLACE_SEARCH_KEYWORD_COUNTER", String.class, Long.class);
	}

	@Override
	public Long cacheKeyword(String keyword) {
		return keywordCountCache.invoke(keyword, ((entry, objects) -> {
			Long currentCount = (Long) entry.getValue();
			Long newCount = currentCount == null ? 1 : currentCount + 1;
			entry.setValue(newCount);
			return newCount;
		}));
	}

	@Override
	public void evictKeyword(String cacheKey) {
		keywordCountCache.remove(cacheKey);
	}
}
