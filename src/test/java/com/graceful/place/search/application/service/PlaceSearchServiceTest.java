package com.graceful.place.search.application.service;

import javax.cache.CacheManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import lombok.extern.slf4j.Slf4j;

import com.graceful.place.search.testContext.TestContext;


@Slf4j
class PlaceSearchServiceTest extends TestContext {

	@Autowired
	private PlaceSearchService placeSearchService;

	@Autowired
	@Qualifier("ehCacheManager")
	private CacheManager ehCacheManager;


}