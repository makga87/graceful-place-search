package com.graceful.place.search.application.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
class KeywordCachingServiceTest {

	@Autowired
	private KeywordCachingService keywordCachingService;

	@Test
	void testKeywordCaching() {
		String keyword = "A";
		Long first = keywordCachingService.cacheKeyword(keyword);
		Long second = keywordCachingService.cacheKeyword(keyword);
		Long third = keywordCachingService.cacheKeyword(keyword);

		assertEquals(1L, first);
		assertEquals(2L, second);
		assertEquals(3L, third);
	}

	@Test
	void testEvictKeywordCaching() {
		String keyword = "B";
		Long first = keywordCachingService.cacheKeyword(keyword);
		keywordCachingService.evictKeyword(keyword);
		Long second = keywordCachingService.cacheKeyword(keyword);

		assertEquals(1L, first);
		assertEquals(1L, second);
	}


}