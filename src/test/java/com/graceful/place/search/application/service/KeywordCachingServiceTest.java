package com.graceful.place.search.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;

import com.graceful.place.search.domain.Keyword;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class KeywordCachingServiceTest {

	@Autowired
	private KeywordCachingService keywordCachingService;

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

		log.debug("==>> keywords = {}", keywords);

		assertEquals(expectedKeywords, keywords);
	}

	private void loop(String keyword, Integer count) {
		for (int i = 0; i < count; i++) {
			keywordCachingService.incrementKeywordCache(keyword);
		}
	}

}