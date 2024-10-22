package com.graceful.place.search.domain;


import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import com.graceful.place.search.application.utils.LocationSimilarityChecker;
import com.graceful.place.search.application.utils.StringSimilarityChecker;

@ToString(of = {"placeName", "priorityScore", "searchApiType"})
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Place implements Comparable<Place>, Serializable {

	private static final long serialVersionUID = -5137959246669821167L;

	private String placeName;
	private String address;
	private String roadAddress;
	private String x;
	private String y;
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

		return isSimilarPlaceName(place) && (isSimilarAddress(place) || isSimilarRoadAddress(place) || isSimilarLocation(place));
	}

	private boolean isSimilarPlaceName(Place place) {
		if (StringUtils.isNotEmpty(placeName) && StringUtils.isNotEmpty(place.placeName)) {
			return StringSimilarityChecker.isSimilar(placeName, place.placeName);
		}
		return false;
	}

	private boolean isSimilarAddress(Place place) {
		if (StringUtils.isNotEmpty(address) && StringUtils.isNotEmpty(place.address)) {
			return StringSimilarityChecker.isSimilar(address, place.address);
		}
		return false;
	}

	private boolean isSimilarRoadAddress(Place place) {
		if (StringUtils.isNotEmpty(roadAddress) && StringUtils.isNotEmpty(place.roadAddress)) {
			return StringSimilarityChecker.isSimilar(roadAddress, place.roadAddress);
		}
		return false;
	}

	private boolean isSimilarLocation(Place place) {
		if (StringUtils.isNotEmpty(x) && StringUtils.isNotEmpty(y)
			&& StringUtils.isNotEmpty(place.x) && StringUtils.isNotEmpty(place.y)) {
			return LocationSimilarityChecker.isSimilarLocation(x, y, place.x, place.y);
		}
		return false;
	}
}
