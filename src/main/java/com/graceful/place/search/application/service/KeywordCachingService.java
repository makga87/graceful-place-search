package com.graceful.place.search.application.service;


import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.cache.Cache;
import javax.cache.CacheManager;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.graceful.place.search.application.port.in.KeywordCachingUseCase;
import com.graceful.place.search.domain.Keyword;

@Slf4j
@Service
public class KeywordCachingService implements KeywordCachingUseCase {

	private final PriorityQueue<Keyword> rankedKeywordsQueue;
	private final Cache<String, Long> keywordCountCache;

	public KeywordCachingService(@Qualifier("ehCacheManager") CacheManager ehCacheManager,
								 @Qualifier("rankedKeywordsQueue") PriorityQueue<Keyword> rankedKeywordsQueue) {
		this.keywordCountCache = ehCacheManager.getCache("PLACE_SEARCH_KEYWORD_COUNTER", String.class, Long.class);
		this.rankedKeywordsQueue = rankedKeywordsQueue;
	}

	@Cacheable(cacheNames = "TOP_RANKED_KEYWORD", key = "'topRankedKeyword'")
	@Override
	public List<Keyword> getTopKeywords(int limit) {

		Set<String> keys = new HashSet<>();
		keywordCountCache.forEach(entry -> {
			keys.add(entry.getKey());
		});

		List<Keyword> keywords = keywordCountCache.getAll(keys).entrySet()
												  .stream()
												  .map((entry) -> Keyword.of(entry.getKey(), entry.getValue()))
												  .collect(Collectors.toList());

		rankedKeywordsQueue.addAll(keywords);

		return Stream.generate(rankedKeywordsQueue::poll)
					 .limit(limit)
					 .collect(Collectors.toList());
	}

	@Override
	public Long incrementKeywordCache(String keyword) {
		try {
			return keywordCountCache.invoke(keyword, ((entry, objects) -> {
				Long currentCount = entry.getValue();
				Long newCount = currentCount == null ? 1 : currentCount + 1;
				entry.setValue(newCount);
				return newCount;
			}));
		} catch (Exception e) {
			log.error("캐시 업데이트 실패 keyword : {}, Exception={}", keyword, e.getStackTrace());
			return 0L;
		}
	}

	@Override
	public void evictKeyword(String cacheKey) {
		keywordCountCache.remove(cacheKey);
	}

	public List<Keyword> getKeywords() {
		Set<String> keys = new HashSet<>();
		keywordCountCache.forEach(entry -> {
			keys.add(entry.getKey());
		});
		return keywordCountCache.getAll(keys).entrySet()
								.stream()
								.map((entry) -> Keyword.of(entry.getKey(), entry.getValue()))
								.collect(Collectors.toList());
	}
}
