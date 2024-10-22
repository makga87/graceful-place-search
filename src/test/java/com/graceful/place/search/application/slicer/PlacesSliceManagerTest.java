package com.graceful.place.search.application.slicer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.graceful.place.search.config.PlaceSearchConfig;
import com.graceful.place.search.domain.Place;

@ExtendWith(MockitoExtension.class)
class PlacesSliceManagerTest {

	@Mock
	private PlaceSearchConfig config;

	@InjectMocks
	private PlacesSliceManager placesSliceManager;

	@DisplayName("빈 배열을 자르는 경우, 빈 배열이 그대로 반환된다.")
	@Test
	void testSliceEmptyList() {

		List<Place> result = placesSliceManager.slice(Collections.emptyList(), 5);

		assertTrue(result.isEmpty());
	}

	@DisplayName("배열 보다 큰 사이즈로 자르는 경우, 배열이 그대로 반환된다.")
	@Test
	void testSliceListSizeLessThanSliceSize() {

		List<Place> result = placesSliceManager.slice(loop("kakao", 3), 5);

		assertEquals(result.size(), 3);
	}

	@DisplayName("배열 사이즈-sliceSize가 10보다 작은 경우, sliceSize만큼 반환한다.")
	@Test
	void testSliceListSizeMinusSliceSizeLessThan10() {
		PlaceSearchConfig.Size size = mock(PlaceSearchConfig.Size.class);
		when(config.getSize()).thenReturn(size);
		when(config.getSize().getTotal()).thenReturn(10);

		List<Place> result = placesSliceManager.slice(loop("kakao", 13), 5);

		assertEquals(result.size(), 5);

	}
	@DisplayName("배열 사이즈-sliceSize가 10보다 큰 경우, 10-sliceSize만큼 반환한ㄴ다.")
	@Test
	void testSliceListSizeMinusSliceSizeBiggerThan10() {

		PlaceSearchConfig.Size size = mock(PlaceSearchConfig.Size.class);
		when(config.getSize()).thenReturn(size);
		when(config.getSize().getTotal()).thenReturn(10);

		List<Place> result = placesSliceManager.slice(loop("kakao", 100), 6);

		assertEquals(4, result.size());

	}

	List<Place> loop(String name, int count) {
		List<Place> places = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			places.add(Place.builder()
							.placeName(name + i)
							.build());
		}
		return places;
	}
}