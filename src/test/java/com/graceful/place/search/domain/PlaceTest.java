package com.graceful.place.search.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}