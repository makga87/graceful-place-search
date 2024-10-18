package com.graceful.place.search.domain;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Place implements Comparable<Place> {

	private String placeName;
	private String address;
	private String roadAddress;
	private Double x;
	private Double y;
	private int priorityScore;
	private SearchApiType searchApiType;

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
