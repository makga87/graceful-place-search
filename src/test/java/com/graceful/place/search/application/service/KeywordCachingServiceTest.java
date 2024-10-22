package com.graceful.place.search.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.cache.Cache;
import javax.cache.CacheManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import lombok.extern.slf4j.Slf4j;

import com.graceful.place.search.domain.Keyword;
import com.graceful.place.search.testContext.TestContext;

@Slf4j
class KeywordCachingServiceTest extends TestContext {

	@Autowired
	private KeywordCachingService keywordCachingService;

	@Autowired
	@Qualifier("ehCacheManager")
	private CacheManager ehCacheManager;


	@Test
	void testKeywordCaching() {
		String keyword = "A";
		Long first = keywordCachingService.incrementKeywordCache(keyword);
		Long second = keywordCachingService.incrementKeywordCache(keyword);
		Long third = keywordCachingService.incrementKeywordCache(keyword);

		assertEquals(1L, first);
		assertEquals(2L, second);
		assertEquals(3L, third);
	}

	@Test
	void testEvictKeywordCaching() {
		String keyword = "B";
		Long first = keywordCachingService.incrementKeywordCache(keyword);
		keywordCachingService.evictKeyword(keyword);
		Long second = keywordCachingService.incrementKeywordCache(keyword);

		assertEquals(1L, first);
		assertEquals(1L, second);
	}

	@Test
	void testGetTopKeywords() throws InterruptedException {
		loop("H", 5);
		loop("K", 7);
		loop("L", 10);
		loop("B", 3);
		loop("Q", 14);
		loop("W", 9);
		loop("E", 12);
		loop("R", 25);
		loop("T", 15);

		List<Keyword> keywords = keywordCachingService.getTopKeywords(5);

		List<Keyword> expectedKeywords = Arrays.asList(Keyword.of("R", 25L),
													   Keyword.of("T", 15L),
													   Keyword.of("Q", 14L),
													   Keyword.of("E", 12L),
													   Keyword.of("L", 10L));

		assertEquals(expectedKeywords, keywords);
	}

	@DisplayName("동시 다발적으로 키워드 카운팅 호출 시, 키워드 카운팅이 Thread safe하다")
	@Test
	void testKeywordSearchCountConcurrent() throws Exception {

		int threadCount = 100;
		String keyword = "은행";
		CountDownLatch latch = new CountDownLatch(threadCount);
		ExecutorService executor = Executors.newFixedThreadPool(20);

		for (int i = 0; i < threadCount; i++) {
			executor.submit(() -> {
				try {
					keywordCachingService.incrementKeywordCache(keyword);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				} finally {
					latch.countDown();
				}
			});
		}

		boolean completed = latch.await(10, TimeUnit.SECONDS);
		executor.shutdown();

		if(!completed) {
			throw new Exception("테스트 수행시간 초과, 정상적인 시스템 상태인지 점검 필요");
		}

		Cache<String, Long> keywordCountCache = ehCacheManager.getCache("PLACE_SEARCH_KEYWORD_COUNTER", String.class, Long.class);

		long actual = keywordCountCache.get(keyword);
		assertEquals(threadCount, actual);
	}

	private void loop(String keyword, Integer count) {
		for (int i = 0; i < count; i++) {
			keywordCachingService.incrementKeywordCache(keyword);
		}
	}
}