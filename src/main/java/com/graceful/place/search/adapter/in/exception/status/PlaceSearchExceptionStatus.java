package com.graceful.place.search.adapter.in.exception.status;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PlaceSearchExceptionStatus implements ResponseStatus {

	INTERNAL_SERVER_ERROR(500, "내부 시스템 에러");

	private final int code;
	private final String message;

	@Override
	public int getCode() {
		return this.code;
	}

	@Override
	public String getMessage() {
		return this.message;
	}
}
