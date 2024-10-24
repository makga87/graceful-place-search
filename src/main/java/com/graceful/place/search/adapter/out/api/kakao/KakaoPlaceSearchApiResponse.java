package com.graceful.place.search.adapter.out.api.kakao;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.graceful.place.search.adapter.out.api.PlaceSearchApiResponse;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KakaoPlaceSearchApiResponse implements PlaceSearchApiResponse<KakaoPlaceSearchApiResponse.Document> {

	private List<Document> documents;
	private Meta meta;

	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class Document {
		@JsonProperty("address_name")
		private String addressName;
		private String categoryGroupCode;
		private String categoryGroupName;
		private String categoryName;
		private String distance;
		private String id;
		private String phone;
		@JsonProperty("place_name")
		private String placeName;
		private String placeUrl;
		@JsonProperty("road_address_name")
		private String roadAddressName;
		private String x;
		private String y;
	}

	@Data
	public static class Meta {
		private boolean isEnd;
		private int pageableCount;
		private SameName sameName;
		@JsonProperty("total_count")
		private int totalCount;

		@Data
		public static class SameName {
			private String keyword;
			private List<String> region;
			private String selectedRegion;
		}
	}

	@Override
	public List<Document> getResults() {
		return this.documents;
	}
}
