package com.graceful.place.search.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


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

	@DisplayName("장소의 유사도에 따른, 같은 장소 여부를 판단한다")
	@Test
	void testEquals() {
		Place place1 = Place.builder()
							.placeName("신동궁감자탕뼈숯불구이 선릉직영점")
							.address("서울 강남구 대치동 890-44")
							.roadAddress("서울 강남구 선릉로86길 39")
							.x("127.052892633601")
							.y("37.5040848203409")
							.build();

		Place place2 = Place.builder()
							.placeName("신동궁 <b>감자탕</b> 뼈숯불구이 선릉직영점")
							.address("서울특별시 강남구 대치동 890-44 1층 신동궁감자탕 뼈숯불구이")
							.roadAddress("서울특별시 강남구 선릉로86길 39 1층 신동궁감자탕 뼈숯불구이")
							.x("1270529113")
							.y("375040863")
							.build();

		assertTrue(place1.equals(place2));
	}

}