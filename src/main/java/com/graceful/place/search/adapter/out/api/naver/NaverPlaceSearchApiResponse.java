package com.graceful.place.search.adapter.out.api.naver;


import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.graceful.place.search.adapter.out.api.PlaceSearchApiResponse;

@Getter
public class NaverPlaceSearchApiResponse implements PlaceSearchApiResponse {
	private String lastBuildDate;
	private int total;
	private int start;
	private int display;
	private List<Item> items;

	@Getter
	@Setter
	public static class Item {
		private String title;
		private String link;
		private String category;
		private String description;
		private String telephone;
		private String address;
		private String roadAddress;
		private String mapx;
		private String mapy;
	}
}
