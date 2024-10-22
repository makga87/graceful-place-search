package com.graceful.place.search.config;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

@Configuration
public class UndertowConfig {

	@Bean
	public UndertowServletWebServerFactory undertowFactory() {

		Semaphore semaphore = new Semaphore(100, true);

		UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();

		factory.addDeploymentInfoCustomizers(deploymentInfo -> {
			deploymentInfo.addInitialHandlerChainWrapper(nextHandler -> {
				return (exchange -> {
					if (semaphore.tryAcquire(4000, TimeUnit.MILLISECONDS)) {
						try {
							nextHandler.handleRequest(exchange);
						} finally {
							semaphore.release();
						}
					} else {
						exchange.setStatusCode(StatusCodes.TOO_MANY_REQUESTS);
						exchange.getResponseHeaders().put(Headers.RETRY_AFTER, "5");
						exchange.endExchange();
					}
				});
			});
		});

		return factory;
	}
}
