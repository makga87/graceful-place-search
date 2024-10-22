package com.graceful.place.search.config;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "place")
@Data
public class PlaceSearchConfig {
	private Executor executor;
	private Size size;
	private Api api;

	@PostConstruct
	public void init() {
		log.debug("=== Place Properties Initialized ===");
		log.info("Thread Pool - Core Size: {}", executor.getCorePoolSize());
		log.info("Thread Pool - Max Size: {}", executor.getMaxPoolSize());
		log.info("Thread Pool - Queue Capacity: {}", executor.getQueueCapacity());
		log.debug("Default Size: {}", size.getByApi());
		log.debug("Total Size: {}", size.getTotal());
		log.debug("Kakao API URL: {}", api.getKakao().getUrl());
		log.debug("Kakao API Key: {}", maskApiKey(api.getKakao().getKey()));
		log.debug("Naver API URL: {}", api.getNaver().getUrl());
		log.debug("Naver Client ID: {}", maskApiKey(api.getNaver().getId()));
		log.debug("Naver Client Secret: {}", maskApiKey(api.getNaver().getSecret()));
		log.debug("==============================");
	}

	private String maskApiKey(String key) {
		if (key == null || key.length() < 8) {
			return "***";
		}
		return key.substring(0, 4) + "..." + key.substring(key.length() - 4);
	}

	@Data
	public static class Executor {
		private int corePoolSize;
		private int maxPoolSize;
		private int queueCapacity;
	}

	@Data
	public static class Size {
		private int byApi;
		private int total;
	}

	@Data
	public static class Api {
		private Kakao kakao;
		private Naver naver;
	}

	@Data
	public static class Kakao {
		private String url;
		private String key;
	}

	@Data
	public static class Naver {
		private String url;
		private String id;
		private String secret;
	}
}
