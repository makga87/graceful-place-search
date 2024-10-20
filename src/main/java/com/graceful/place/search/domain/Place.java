package com.graceful.place.search.domain;


import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import com.graceful.place.search.application.utils.StringSimilarityChecker;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Place implements Comparable<Place>, Serializable {

	private static final long serialVersionUID = -5137959246669821167L;

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

		return isSimilarPlaceName(place) && (isSimilarAddress(place) || isSimilarRoadAddress(place));
	}

	private boolean isSimilarPlaceName(Place place) {
		if (StringUtils.isNotEmpty(placeName) && StringUtils.isNotEmpty(place.placeName)) {
			return StringSimilarityChecker.isSimilar(placeName, place.placeName);
		}
		return false;
	}

	private boolean isSimilarAddress(Place place){
		if (StringUtils.isNotEmpty(roadAddress) && StringUtils.isNotEmpty(place.roadAddress)) {
			return StringSimilarityChecker.isSimilar(roadAddress, place.roadAddress);
		}
		return false;
	}

	private boolean isSimilarRoadAddress(Place place){
		if (StringUtils.isNotEmpty(placeName) && StringUtils.isNotEmpty(place.placeName)) {
			return StringSimilarityChecker.isSimilar(placeName, place.placeName);
		}
		return false;
	}
}
