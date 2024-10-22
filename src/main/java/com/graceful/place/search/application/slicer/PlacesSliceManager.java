package com.graceful.place.search.application.slicer;


import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.graceful.place.search.config.PlaceSearchConfig;
import com.graceful.place.search.domain.Place;

@RequiredArgsConstructor
@Component
public class PlacesSliceManager implements PlacesSliceStrategy {

	private final PlaceSearchConfig config;

	@Override
	public List<Place> slice(List<Place> places, int sliceSize) {
		if (sliceSize > places.size() || places.size() - sliceSize < 0) {
			return places;
		}

		if (places.size() - sliceSize > config.getSize().getTotal()) {
			return places.subList(0, config.getSize().getTotal() - sliceSize);
		}
		return places.subList(0, sliceSize);
	}
}
