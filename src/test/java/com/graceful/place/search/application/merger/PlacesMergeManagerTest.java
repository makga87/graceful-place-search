package com.graceful.place.search.application.merger;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.graceful.place.search.domain.Place;
import com.graceful.place.search.domain.SearchApiType;

@Slf4j
class PlacesMergeManagerTest {

	private PlacesMergeStrategy placesMergeStrategy = new PlacesMergeManager();

	@DisplayName("장소명이 유사하나, 위경도 값이 다르면 다른 주소로 판단 병합한다.")
	@Test
	void testMergeSimilarPlaceNameDifferentCoordinate() {

		Place kakaoPlaceA = getPlace("곱창A", "", "", "127.01234567", "37.01234567", SearchApiType.KAKAO);
		Place naverPlaceA = getPlace("곱창B", "", "", "128.01234567", "39.01234567", SearchApiType.NAVER);

		List<Place> result = placesMergeStrategy.merge(Arrays.asList(kakaoPlaceA), Arrays.asList(naverPlaceA));


		assertEquals(2, result.size());
		assertEquals("곱창A", result.get(0).getPlaceName());
		assertEquals("곱창B", result.get(1).getPlaceName());
	}

	@DisplayName("위경도 값이 같으나, 장소명이 다르면, 다른 주소로 판단 병합한다.")
	@Test
	void testMergeSimilarCoordinateDifferentPlaceName() throws JsonProcessingException {

		Place kakaoPlaceA = getPlace("아구찜A", "", "", "127.01234567", "37.01234567", SearchApiType.KAKAO);
		Place naverPlaceA = getPlace("해물찜A", "", "", "127.01234567", "37.01234567", SearchApiType.NAVER);

		List<Place> result = placesMergeStrategy.merge(Arrays.asList(kakaoPlaceA), Arrays.asList(naverPlaceA));

		assertEquals(2, result.size());
		assertEquals("아구찜A", result.get(0).getPlaceName());
		assertEquals("해물찜A", result.get(1).getPlaceName());
	}

	@DisplayName("도로명주소 값이 유사하나, 장소명이 다르면, 다른 주소로 판단 병합한다.")
	@Test
	void testMergeSimilarRoadAddressDifferentPlaceName() throws JsonProcessingException {

		Place kakaoPlaceA = getPlace("아구찜A", "", "성남시 분당구 1 야탑로", "127.01234567", "37.01234567", SearchApiType.KAKAO);
		Place naverPlaceA = getPlace("해물찜A", "", "경기도 성남시분당구 1 야탑로", "127.01234567", "37.01234567", SearchApiType.NAVER);

		List<Place> result = placesMergeStrategy.merge(Arrays.asList(kakaoPlaceA), Arrays.asList(naverPlaceA));

		assertEquals(2, result.size());
		assertEquals("아구찜A", result.get(0).getPlaceName());
		assertEquals("해물찜A", result.get(1).getPlaceName());
	}

	@DisplayName("지번 값이 유사하나, 장소명이 다르면, 다른 주소로 판단 병합한다.")
	@Test
	void testMergeSimilarAddressDifferentPlaceName() throws JsonProcessingException {

		Place kakaoPlaceA = getPlace("아구찜A", "성남시 분당구 1", "", "127.01234567", "37.01234567", SearchApiType.KAKAO);
		Place naverPlaceA = getPlace("해물찜A", "경기도 성남시분당구 1", "", "127.01234567", "37.01234567", SearchApiType.NAVER);

		List<Place> result = placesMergeStrategy.merge(Arrays.asList(kakaoPlaceA), Arrays.asList(naverPlaceA));

		assertEquals(2, result.size());
		assertEquals("아구찜A", result.get(0).getPlaceName());
		assertEquals("해물찜A", result.get(1).getPlaceName());
	}


	@DisplayName("장소명이 유사하고, 지번 혹은 도로명 또는 위경도가 유사하면 동일한 주소로 판단 병합한다.")
	@Test
	void testMergeSimilarPlaceNameSimilarCoordinate() throws JsonProcessingException {

		Place kakaoPlaceA = getPlace("아구찜A", "", "성남시 분당구 1 야탑로", "127.01234567", "37.01234567", SearchApiType.KAKAO);
		Place naverPlaceA = getPlace("아구 A", "", "성남 분당 야탑로 1", "127.01234568", "37.01234568", SearchApiType.NAVER);

		List<Place> result = placesMergeStrategy.merge(Arrays.asList(kakaoPlaceA), Arrays.asList(naverPlaceA));

		assertEquals(1, result.size());
		assertEquals("아구찜A", result.get(0).getPlaceName());
	}


	private Place getPlace(String placeName, String address, String roadAddress, String x, String y, SearchApiType searchApiType) {
		return Place.builder()
					.placeName(placeName)
					.address(address)
					.roadAddress(roadAddress)
					.x(x)
					.y(y)
					.priorityScore(searchApiType.getAdditionalScore())
					.searchApiType(searchApiType)
					.build();
	}


}