package com.graceful.place.search.application.merger;


import java.util.List;

import com.graceful.place.search.domain.Place;

public interface PlacesMergeStrategy {

	List<Place> merge(List<Place> standardList, List<Place> targetList);
}
