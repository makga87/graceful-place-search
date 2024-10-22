package com.graceful.place.search.config;


import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

	@Bean(name = "taskExecutor")
	public Executor taskExecutor(@Autowired PlaceSearchConfig config) {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

		executor.setCorePoolSize(config.getExecutor().getCorePoolSize());
		executor.setMaxPoolSize(config.getExecutor().getMaxPoolSize());
		executor.setQueueCapacity(config.getExecutor().getQueueCapacity());
		executor.setThreadNamePrefix("ASYNC-THREAD");
		executor.initialize();

		return executor;
	}
}
