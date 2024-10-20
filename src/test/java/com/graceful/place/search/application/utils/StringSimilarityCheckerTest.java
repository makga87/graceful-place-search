package com.graceful.place.search.application.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


class StringSimilarityCheckerTest {


	@ParameterizedTest
	@CsvSource({
			"aaabbbccc, bbbcccaaa, false",
			"짜장, 짬뽕, false",
			"짜장면, 짜장, false",
			"짜장면, 짜장밥, false",
			"서울 특별시 도봉구, 서울특별시 도봉구, true",
			"서울특별시 중구 태평로1가 25 프레스센터(서울신문사) 2층, 서울특별시 중구 세종대로 124 프레스센터(서울신문사) 2층, true",
			"카카오, 카카오뱅크, false"
	})
	void testSimilarity(String a, String b, boolean expected) {

		boolean actual = StringSimilarityChecker.isSimilar(a, b);
		assertEquals(expected, actual);
	}
}