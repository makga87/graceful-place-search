package com.graceful.place.search.application.slicer;


import java.util.List;

import org.springframework.stereotype.Component;

import com.graceful.place.search.domain.Place;

@Component
public class PlacesSliceManager implements PlacesSliceStrategy {

	@Override
	public List<Place> slice(List<Place> places, int sliceSize) {
		if (sliceSize > places.size() || places.size() - sliceSize < 0) {
			return places;
		}
		return places.subList(0, sliceSize - 1);
	}
}
