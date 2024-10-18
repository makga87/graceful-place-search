package com.graceful.place.search.application.port.in;

import com.graceful.place.search.application.SearchCriteria;
import com.graceful.place.search.domain.Places;

public interface PlaceSearchUseCase {

	Places placeSearch(SearchCriteria searchCriteria);
}
