package com.graceful.place.search.adapter.in;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.graceful.place.search.adapter.in.api.KeywordSearchResponse;
import com.graceful.place.search.adapter.in.api.PlaceSearchResponse;
import com.graceful.place.search.application.SearchCriteria;
import com.graceful.place.search.application.port.in.KeywordCachingUseCase;
import com.graceful.place.search.application.port.in.PlaceSearchUseCase;
import com.graceful.place.search.config.PlaceSearchConfig;
import com.graceful.place.search.domain.Keyword;
import com.graceful.place.search.domain.Place;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/places")
public class PlaceSearchController {

	@Qualifier("placeSearchService")
	private final PlaceSearchUseCase placeSearchService;

	@Qualifier("keywordCachingService")
	private final KeywordCachingUseCase keywordCachingService;

	private final PlaceSearchConfig config;

	@GetMapping("/{keyword}")
	public ResponseEntity<List<PlaceSearchResponse>> search(@PathVariable("keyword") String keyword) {

		keywordCachingService.incrementKeywordCache(keyword);

		int size = config.getSize().getByApi();
		List<Place> places = placeSearchService.placeSearch(SearchCriteria.builder()
																		  .keyword(keyword)
																		  .size(size)
																		  .build());

		return ResponseEntity.ok(places.stream()
									   .map(place -> PlaceSearchResponse.from(place.getPlaceName()))
									   .collect(Collectors.toList()));
	}

	@GetMapping("/search/keywords")
	public ResponseEntity<List<KeywordSearchResponse>> searchKeywords() {

		int limit = config.getSize().getTotal();

		List<Keyword> keywords = keywordCachingService.getTopKeywords(limit);

		return ResponseEntity.ok(keywords.stream()
										 .map(keyword -> KeywordSearchResponse.of(keyword.getKeyword(), keyword.getCount()))
										 .collect(Collectors.toList()));
	}
}
