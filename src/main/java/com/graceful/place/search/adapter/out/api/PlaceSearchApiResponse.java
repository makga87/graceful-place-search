package com.graceful.place.search.adapter.out.api;


import java.util.List;

public interface PlaceSearchApiResponse<T> {

	List<T> getResults();

}
