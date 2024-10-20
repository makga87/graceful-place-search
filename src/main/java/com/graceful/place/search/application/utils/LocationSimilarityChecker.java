package com.graceful.place.search.application.utils;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LocationSimilarityChecker {

	private static final BigDecimal SIMILAR_VALUE = BigDecimal.valueOf(0.9999);

	public boolean isSimilarLocation(String a, String b) {

		if (StringUtils.isEmpty(a) || StringUtils.isEmpty(b)) {
			return false;
		}

		int minLength = Math.min(a.length(), b.length());

		long locationA = Long.parseLong(StringUtils.truncate(a.replaceAll("\\.", ""), minLength));
		long locationB = Long.parseLong(StringUtils.truncate(b.replaceAll("\\.", ""), minLength));

		double maxValue = Math.max(locationA, locationB);

		BigDecimal similarity = BigDecimal.valueOf((maxValue - Math.abs(locationA - locationB)) / maxValue);

		log.debug("similarity: {}", similarity);

		return similarity.compareTo(SIMILAR_VALUE) >= 0;
	}
}
