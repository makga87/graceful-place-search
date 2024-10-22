package com.graceful.place.search.adapter.in.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class CommonWebExceptionHandler {

	@ExceptionHandler(value = PlaceSearchException.class)
	public ResponseEntity<ErrorResponse> handlePlaceSearchException(PlaceSearchException ex) {
		log.error(ex.getMessage(), ex);
		return ResponseEntity
				.status(ex.getResponseStatus().getCode())
				.body(new ErrorResponse(ex.getResponseStatus().getMessage()));

	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex) {
		log.error(ex.getMessage(), ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
							 .body(new ErrorResponse(ex.getMessage()));

	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(IllegalStateException ex) {
		log.error(ex.getMessage(), ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
							 .body(new ErrorResponse(ex.getMessage()));

	}
}
