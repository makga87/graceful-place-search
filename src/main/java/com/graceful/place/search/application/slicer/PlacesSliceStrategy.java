package com.graceful.place.search.application.slicer;


import java.util.List;

import com.graceful.place.search.domain.Place;

public interface PlacesSliceStrategy {

	List<Place> slice(List<Place> places, int sliceSize);
}
