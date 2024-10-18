package com.graceful.place.search.adapter.out.api.kakao;


import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.graceful.place.search.adapter.out.api.PlaceSearchApiResponse;

@Getter
public class KakaoPlaceSearchApiResponse implements PlaceSearchApiResponse {

	private List<Document> documents;
	private Meta meta;

	@Getter
	@Setter
	public static class Document {
		private String addressName;
		private String categoryGroupCode;
		private String categoryGroupName;
		private String categoryName;
		private String distance;
		private String id;
		private String phone;
		private String placeName;
		private String placeUrl;
		private String roadAddressName;
		private String x;
		private String y;
	}

	@Getter
	@Setter
	public static class Meta {
		private boolean isEnd;
		private int pageableCount;
		private SameName sameName;
		private int totalCount;

		@Getter
		@Setter
		public static class SameName {
			private String keyword;
			private List<String> region;
			private String selectedRegion;
		}
	}
}
