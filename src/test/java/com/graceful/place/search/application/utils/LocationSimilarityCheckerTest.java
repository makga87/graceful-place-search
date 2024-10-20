package com.graceful.place.search.application.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class LocationSimilarityCheckerTest {

	@ParameterizedTest
	@CsvSource({
			"127.04851048667157, 37.548246613175785, 1270485757, 375482393, true",
			"127.10890911822922, 37.5143143020754, 1271095074, 375073149, false",
			"127, 37, 227, 47, false"
	})
	void testLocationSimilarity(String aX, String aY, String bX, String bY, boolean expected) {

		LocationSimilarityChecker checker = new LocationSimilarityChecker();

		assertEquals(expected, checker.isSimilarLocation(aX, bX) && checker.isSimilarLocation(aY, bY));
	}
}