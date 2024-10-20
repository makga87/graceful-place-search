package com.graceful.place.search.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


class PlaceTest {

	@DisplayName("장소 우선순위 점수 증가 메서드 정상동작함을 확인")
	@Test
	void testPlusScore() {
		Place place = Place.builder()
						   .placeName("PlaceA")
						   .priorityScore(1)
						   .build();
		place.plusScore();

		assertEquals(2, place.getPriorityScore());
	}

	@DisplayName("장소 우선순위 비교 메서드가 정상동작함을 확인")
	@Test
	void testCompareTo() {

		Place placeA = Place.builder()
							.placeName("PlaceA")
							.priorityScore(1)
							.build();

		Place placeB = Place.builder()
							.placeName("PlaceB")
							.priorityScore(1)
							.build();

		assertEquals(0, placeA.compareTo(placeB));
		placeA.plusScore();

		assertEquals(-1, placeA.compareTo(placeB));
		assertEquals(1, placeB.compareTo(placeA));
	}

	@DisplayName("장소의 유사도에 따른, 같은장소 여부를 판단한다")
	@ParameterizedTest
	@CsvSource({
			"신동궁감자탕뼈숯불구이 선릉직영점, 신동궁 <b>감자탕</b> 뼈숯불구이 선릉직영점, 서울 강남구 대치동 890-44, 서울특별시 강남구 대치동 890-44 1층 신동궁감자탕 뼈숯불구이,서울 강남구 선릉로86길 39, 서울특별시 강남구 선릉로86길 39 1층 신동궁감자탕 뼈숯불구이, true"
	})
	void testEquals(String placeA, String placeB, String addrA, String addrB, String roadAddrA, String roadAddrB, boolean expected) {
		Place place1 = Place.builder()
							.placeName(placeA)
							.address(addrA)
							.roadAddress(roadAddrA)
							.build();

		Place place2 = Place.builder()
							.placeName(placeB)
							.address(addrB)
							.roadAddress(roadAddrB)
							.build();

		assertEquals(expected, place1.equals(place2));
	}

}