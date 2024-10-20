package com.graceful.place.search.adapter.in;


import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.graceful.place.search.application.SearchCriteria;
import com.graceful.place.search.application.port.in.KeywordCachingUseCase;
import com.graceful.place.search.application.port.in.PlaceSearchUseCase;
import com.graceful.place.search.domain.Keyword;
import com.graceful.place.search.domain.Places;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/place")
public class PlaceSearchController {

	@Qualifier("placeSearchService")
	private final PlaceSearchUseCase placeSearchService;

	@Qualifier("keywordCachingService")
	private final KeywordCachingUseCase keywordCachingService;

	@GetMapping
	public ResponseEntity<Places> search(@RequestParam("q") String q) {
		keywordCachingService.incrementKeywordCache(q);
		Places places = placeSearchService.placeSearch(SearchCriteria.builder()
																	 .keyword(q)
																	 .size(5)
																	 .build());

		return ResponseEntity.ok(places);
	}

	@GetMapping("/search/keywords")
	public ResponseEntity<List<Keyword>> searchKewords() {
		List<Keyword> keywords = keywordCachingService.getTopKeywords(10);

		return ResponseEntity.ok(keywords);
	}
}
