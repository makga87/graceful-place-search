package com.graceful.place.search.adapter.out.api.kakao;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import lombok.extern.slf4j.Slf4j;

import com.graceful.place.search.infrastructure.external.RestTemplateHttpClient;

@Slf4j
@Import({RestTemplateHttpClient.class})
class KakaoPlaceSearchAdapterTest {

	//	@Autowired
	//	public RestTemplateHttpClient restTemplateHttpClient;

	@Test
	void testKakaoPlaceSearchAdapter() {

		//		KakaoPlaceSearchAdapter kakaoPlaceSearchAdapter = new KakaoPlaceSearchAdapter(restTemplateHttpClient);
		//		KakaoPlaceSearchApiResponse response = kakaoPlaceSearchAdapter.searchPlaces(KakaoPlaceSearchApiRequest.valueOf("은행"));
		//
		//		assertNotNull(response);
		//		assertFalse(response.getDocuments().isEmpty());
	}
}