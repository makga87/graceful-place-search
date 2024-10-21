package com.graceful.place.search.application.merger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.graceful.place.search.domain.Place;

@Component
public class PlacesMergeManager implements PlacesMergeStrategy {

	@Override
	public List<Place> merge(List<Place> standardList, List<Place> targetList) {

		List<Place> stdPlaces = new ArrayList<>(standardList);

		for (Place targetPlace : targetList) {
			boolean merged = false;

			for (Place place : stdPlaces) {
				if (place.equals(targetPlace)) {
					place.plusScore();
					merged = true;
					break;
				}
			}
			if (!merged) {
				stdPlaces.add(targetPlace);
			}
		}

		stdPlaces.sort(null);

		return stdPlaces;
	}
}
