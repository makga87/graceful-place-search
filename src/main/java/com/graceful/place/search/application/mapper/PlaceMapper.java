package com.graceful.place.search.application.mapper;

import com.graceful.place.search.domain.Place;

public interface PlaceMapper<T> {

	Place toPlace(T t);
}
