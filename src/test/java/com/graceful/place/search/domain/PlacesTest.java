package com.graceful.place.search.domain;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class PlacesTest {

	@DisplayName("장소들 순서가 우선순위 점수에 따라 내림차순 정렬됨을 확인한다.")
	@Test
	void testSortedPlaces() {

		Place p1 = Place.builder()
						.placeName("A")
						.priorityScore(1)
						.build();

		Place p2 = Place.builder()
						.placeName("B")
						.priorityScore(4)
						.build();

		Place p3 = Place.builder()
						.placeName("C")
						.priorityScore(2)
						.build();

		Place p4 = Place.builder()
						.placeName("D")
						.priorityScore(6)
						.build();

		Places places = Places.from(Arrays.asList(p1, p2, p3, p4));

		List<Place> sortedPlaces = places.getSortedPlaces();
		List<Place> expected = Arrays.asList(p4, p2, p3, p1);

		assertArrayEquals(expected.toArray(), sortedPlaces.toArray());
	}

	@DisplayName("장소들을 병합 시, 중복 장소는 점수가 가산되어 내림차순 정렬됨을 확인한다. 동점일 경우, 장소명을 알파벳 역순으로 정렬한다.")
	@Test
	void testMergedSortedPlaces() {

		Place p1 = Place.builder()
						.placeName("A")
						.priorityScore(1)
						.build();

		Place p2 = Place.builder()
						.placeName("B")
						.priorityScore(1)
						.build();

		Place p3 = Place.builder()
						.placeName("C")
						.priorityScore(1)
						.build();

		Place p4 = Place.builder()
						.placeName("A")
						.priorityScore(2)
						.build();

		Place p5 = Place.builder()
						.placeName("D")
						.priorityScore(2)
						.build();

		Place p6 = Place.builder()
						.placeName("Z")
						.priorityScore(2)
						.build();

		Places place1 = Places.from(Arrays.asList(p1, p2, p3));
		List<Place> place2 = Arrays.asList(p4, p5, p6);
		place1.merge(place2);

		List<Place> actual = place1.getSortedPlaces();
		List<Place> expected = Arrays.asList(p1, p6, p5, p3, p2);

		assertArrayEquals(expected.toArray(), actual.toArray());
	}

}