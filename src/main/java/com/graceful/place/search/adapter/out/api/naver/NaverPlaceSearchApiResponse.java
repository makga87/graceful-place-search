package com.graceful.place.search.adapter.out.api.naver;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.graceful.place.search.adapter.out.api.PlaceSearchApiResponse;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NaverPlaceSearchApiResponse implements PlaceSearchApiResponse<NaverPlaceSearchApiResponse.Item> {
	private String lastBuildDate;
	private int total;
	private int start;
	private int display;
	private List<Item> items;

	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	@Data
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

	@Override
	public List<Item> getResults() {
		return this.items;
	}
}
