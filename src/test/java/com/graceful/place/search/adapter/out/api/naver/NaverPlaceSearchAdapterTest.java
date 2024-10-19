package com.graceful.place.search.adapter.out.api.naver;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import lombok.extern.slf4j.Slf4j;

import com.graceful.place.search.infrastructure.external.RestTemplateHttpClient;
import com.graceful.place.search.testContext.TestContext;

@Slf4j
@Import({RestTemplateHttpClient.class})
class NaverPlaceSearchAdapterTest extends TestContext {

	@Autowired
	public RestTemplateHttpClient restTemplateHttpClient;

	@Test
	void testNaverPlaceSearchAdapter() {

		NaverPlaceSearchAdapter naverPlaceSearchAdapter = new NaverPlaceSearchAdapter(restTemplateHttpClient);
		NaverPlaceSearchApiResponse response = naverPlaceSearchAdapter.searchPlaces(NaverPlaceSearchApiRequest.of("은행", 5));

		assertNotNull(response);
		assertTrue(response.getTotal() > 0);
		assertFalse(response.getItems().isEmpty());
	}
}