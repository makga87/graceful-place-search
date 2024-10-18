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
		Integer first = keywordCachingService.incrementAndGetCount("A");
		Integer second = keywordCachingService.incrementAndGetCount("A");
		Integer third = keywordCachingService.incrementAndGetCount("A");

		assertEquals(1, first);
		assertEquals(2, second);
		assertEquals(3, third);
	}
}