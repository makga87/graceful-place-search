package com.graceful.place.search.application.port.in;


public interface KeywordCachingUseCase {

	void cacheKeyword(String keyword);
	void evictKeyword(String cacheKey);
}
