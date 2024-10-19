package com.graceful.place.search.application.port.in;


import java.util.List;

import com.graceful.place.search.domain.Keyword;

public interface KeywordCachingUseCase {

	List<Keyword> getTopKeywords(int limit);
	Long incrementKeywordCache(String keyword);
	void evictKeyword(String cacheKey);
}
