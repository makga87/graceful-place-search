package com.graceful.place.search.domain;


import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Places implements Serializable {

	private static final long serialVersionUID = 3242186197833745290L;
	private final List<Place> placeList;

	public static Places from(List<Place> places) {
		return new Places(places);
	}

	public List<Place> getPlaceList() {
		return this.placeList;
	}

	public int size() {
		return placeList.size();
	}

	public Stream<Place> getAsStream() {
		return placeList.stream();
	}

	@Override
	public int hashCode() {
		return Objects.hash(new HashSet<>(placeList));
	}
}
