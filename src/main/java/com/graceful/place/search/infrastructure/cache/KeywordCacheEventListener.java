package com.graceful.place.search.infrastructure.cache;

import java.util.concurrent.ConcurrentSkipListMap;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.ehcache.event.EventType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KeywordCacheEventListener implements CacheEventListener<String, Long> {

	public static ConcurrentSkipListMap<Long, String> sortedKeywordRankMap = new ConcurrentSkipListMap<>();

	@Override
	public void onEvent(CacheEvent<? extends String, ? extends Long> event) {
		String keyword = event.getKey();
		Long newValue = event.getNewValue();
		Long oldValue = event.getOldValue();

		if (event.getType() == EventType.CREATED || event.getType() == EventType.UPDATED) {
			if (oldValue != null) {
				sortedKeywordRankMap.remove(oldValue, keyword);
			}
			sortedKeywordRankMap.put(newValue, keyword);
			log.debug("Cache event: {}, Key: {}, Old Value: {}, New Value: {}", event.getType(), keyword, oldValue, newValue);

		} else {
			sortedKeywordRankMap.remove(oldValue, keyword);
			log.debug("Cache event: {}, Key: {}, Removed Value: {}", event.getType(), keyword, oldValue);
		}
	}
}
