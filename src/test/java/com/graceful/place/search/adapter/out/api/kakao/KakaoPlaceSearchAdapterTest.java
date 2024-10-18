package com.graceful.place.search.adapter.out.api.kakao;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import lombok.extern.slf4j.Slf4j;

import com.graceful.place.search.infrastructure.external.RestTemplateHttpClient;

@Slf4j
@SpringBootTest
@Import({RestTemplateHttpClient.class})
class KakaoPlaceSearchAdapterTest {

	@Autowired
	public RestTemplateHttpClient restTemplateHttpClient;

	@Test
	void testKakaoPlaceSearchAdapter() {

		KakaoPlaceSearchAdapter kakaoPlaceSearchAdapter = new KakaoPlaceSearchAdapter(restTemplateHttpClient);

		KakaoPlaceSearchApiResponse response = kakaoPlaceSearchAdapter.searchPlaces(KakaoPlaceSearchApiRequest.valueOf("은행"));


		assertNotNull(response);
		assertFalse(response.getDocuments().isEmpty());
	}
}