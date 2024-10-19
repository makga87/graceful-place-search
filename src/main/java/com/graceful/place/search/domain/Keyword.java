package com.graceful.place.search.domain;


import java.io.Serializable;
import java.util.Objects;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Keyword implements Comparable<Keyword>, Serializable {

	private static final long serialVersionUID = 1L;
	private String keyword;
	private Long count;

	public static Keyword of(String keyword, Long count) {
		return new Keyword(keyword, count);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Keyword keyword1 = (Keyword) o;
		return Objects.equals(keyword, keyword1.keyword) && Objects.equals(count, keyword1.count);
	}

	@Override
	public int compareTo(Keyword o) {
		return Long.compare(o.count, count);
	}
}
