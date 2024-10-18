package com.graceful.place.search.infrastructure.external;


import java.util.Map;

import org.springframework.http.HttpHeaders;

public interface HttpClient {

	<R> R get(String url, Map<String, Object> param, HttpHeaders headers, Class<R> returnType);
}
