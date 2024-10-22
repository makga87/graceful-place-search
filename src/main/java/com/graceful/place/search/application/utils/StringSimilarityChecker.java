package com.graceful.place.search.application.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringSimilarityChecker {

	private static final BigDecimal SIMILAR_VALUE = BigDecimal.valueOf(0.68);

	private static final LevenshteinDistance LEVENSHTEIN_DISTANCE = new LevenshteinDistance();

	public static boolean isSimilar(String a, String b) {

		if (StringUtils.isEmpty(a) || StringUtils.isEmpty(b)) {
			return false;
		}

		int maxLength = Math.max(a.length(), b.length());

		int distance = LEVENSHTEIN_DISTANCE.apply(StringUtils.deleteWhitespace(a),
												  StringUtils.deleteWhitespace(b));

		BigDecimal similarity = BigDecimal.valueOf((double) (maxLength - distance) / maxLength).setScale(3, RoundingMode.CEILING);

		log.debug("similarity: {}", similarity.doubleValue());

		return similarity.compareTo(SIMILAR_VALUE) >= 0;
	}

}
