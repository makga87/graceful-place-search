package com.graceful.place.search.domain;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Place implements Comparable<Place> {

	private String placeName;
	private int priorityScore;

	public static Place of(String placeName, int priorityScore) {
		return new Place(placeName, priorityScore);
	}

	public void plusScore() {
		this.priorityScore++;
	}

	@Override
	public int compareTo(Place o) {
		return Integer.compare(o.priorityScore, priorityScore);
	}

	@Override
	public boolean equals(Object o) {

		if (o == null) {
			return false;
		}

		Place place = (Place) o;
		return placeName.equals(place.placeName);
	}
}
