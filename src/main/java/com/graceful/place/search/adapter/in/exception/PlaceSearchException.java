package com.graceful.place.search.adapter.in.exception;


import lombok.Getter;

import com.graceful.place.search.adapter.in.exception.status.ResponseStatus;

public class PlaceSearchException extends Exception {

	private static final long serialVersionUID = -765128339179894175L;

	@Getter
	private ResponseStatus responseStatus;
	private int code;

	public PlaceSearchException(ResponseStatus responseStatus) {
		super(responseStatus.getMessage());
		this.responseStatus = responseStatus;
	}

	public PlaceSearchException(ResponseStatus responseStatus, Throwable t) {
		super(responseStatus.getMessage(), t);
		this.responseStatus = responseStatus;
	}
}
