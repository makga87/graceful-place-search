package com.graceful.place.search.config;

import java.io.File;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class CacheConfig {

	@Bean(name = "ehCacheManager")
	public CacheManager ehCacheManager() throws Exception {
		CachingProvider provider = Caching.getCachingProvider();
		ClassPathResource cacheConfigResource = new ClassPathResource("/ehcache.xml");
		File cacheConfigFile = cacheConfigResource.getFile();

		return provider.getCacheManager(cacheConfigFile.toURI(), getClass().getClassLoader());
	}
}
