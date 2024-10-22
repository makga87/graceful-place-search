package com.graceful.place.search.application.port.in;

import java.util.List;

import com.graceful.place.search.application.SearchCriteria;
import com.graceful.place.search.domain.Place;

public interface PlaceSearchUseCase {

	List<Place> placeSearch(SearchCriteria searchCriteria);
}
