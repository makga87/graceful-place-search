package com.graceful.place.search.application.port.in;


public interface KeywordCachingUseCase {

	Long cacheKeyword(String keyword);
	void evictKeyword(String cacheKey);
}
