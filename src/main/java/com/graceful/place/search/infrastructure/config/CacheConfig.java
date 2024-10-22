package com.graceful.place.search.infrastructure.config;

import java.io.File;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentSkipListMap;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.graceful.place.search.domain.Keyword;

@Configuration
public class CacheConfig {

	@Bean
	public ConcurrentSkipListMap<Long, String> sortedKeywordRankMap() {
		return new ConcurrentSkipListMap<>();
	}

	@Bean
	public PriorityQueue<Keyword> rankedKeywordsQueue() {
		return new PriorityQueue<>();
	}

	@Bean(name = "ehCacheManager")
	public CacheManager defaultEhCacheManager(@Value("${spring.cache.jcache.config}") String configPath) throws Exception {
		CachingProvider provider = Caching.getCachingProvider();
		ClassPathResource cacheConfigResource = new ClassPathResource(configPath);

		return provider.getCacheManager(cacheConfigResource.getURI(), getClass().getClassLoader());
	}

}
