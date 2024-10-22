package com.graceful.place.search.domain;


import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Places implements Serializable {

	private static final long serialVersionUID = 3242186197833745290L;
	private final List<Place> placeList;

	public static Places from(List<Place> places) {
		return new Places(places);
	}

	public List<Place> getList() {
		return this.placeList;
	}

	public int size() {
		return placeList.size();
	}

	@JsonIgnore
	public Stream<Place> getAsStream() {
		return placeList.stream();
	}
}
