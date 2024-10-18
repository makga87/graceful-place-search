package com.graceful.place.search.application.port.out;

import com.graceful.place.search.adapter.out.api.PlaceSearchApiRequest;
import com.graceful.place.search.adapter.out.api.PlaceSearchApiResponse;

public interface PlaceSearchPort<P extends PlaceSearchApiRequest, R extends PlaceSearchApiResponse> {

	R searchPlaces(P p);
}
