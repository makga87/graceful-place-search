package com.graceful.place.search.domain;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Places {
	private final PriorityQueue<Place> sortedPlaces = new PriorityQueue<>(5);

	private Places(List<Place> places) {
		this.sortedPlaces.addAll(places);
	}

	public static Places from(List<Place> places) {
		return new Places(places);
	}

	public void merge(List<Place> places) {
		Map<String, Place> placeMap = new HashMap<>();

		// 기존 큐의 요소들을 맵으로 변환
		while (!sortedPlaces.isEmpty()) {
			Place place = sortedPlaces.poll();
			placeMap.put(place.getPlaceName(), place);
		}

		// 새로운 장소들을 처리
		for (Place newPlace : places) {
			placeMap.merge(newPlace.getPlaceName(), newPlace, (existing, added) -> {
				return Place.builder()
							.placeName(existing.getPlaceName())
							.address(existing.getAddress())
							.roadAddress(existing.getRoadAddress())
							.x(existing.getX())
							.y(existing.getY())
							.priorityScore(existing.getPriorityScore() + added.getPriorityScore())
							.build();
			});
		}

		// 결과를 다시 큐에 추가
		sortedPlaces.addAll(placeMap.values());

	}

	public List<Place> getSortedPlaces() {
		return Stream.generate(sortedPlaces::poll)
					 .limit(sortedPlaces.size())
					 .collect(Collectors.toList());
	}

}
