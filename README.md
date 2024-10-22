# Graceful Place Search


# 1. LIBRARY
## Hexagonal Architecture
### 사용 목적
- 프로그램의 지속적 유지보수 및 확장에 용이한 아키텍처 설계를 고민 중, 다음의 충족사항을 만족시키는 것을 찾았습니다.
  1. 핵심이 되는 비즈니스 로직과 도메인을 외부 의존성으로 부터 격리를 시킬수 있는지?
  2. 코드 수정 혹은 새로운 기능을 추가할 경우, 변경에 대해 유연한지?
  3. 테스트 코드 작성 단위가 작은지?
  4. 어느정도 표준화된 코드컨벤션이 존재하는지? (내가 만들기 나름이지만, 많은 레퍼런스들을 참고 시, 혼동이 발생하지 않는지)
  5. 레퍼런스가 있는지?

- 위 기준으로 클린코드 아키텍처들을 검색하다가, 헥사고날 아키텍처가 적절해보여 이로 결정했습니다.

## Common Text 1.12.0
### 사용 목적
- 장소명으로 검색 API를 호출 시, 서비스를 제공하는 업체마다 장소명에 대한 값이 조금씩 달랐습니다.
- 같은 장소를 판단하기 위해선, 비슷한 장소 문자열을 동일한 장소로 판단하기 위한, 문자열 유사도 측정하는 기능이 필요했습니다.
- 문자열 알고리즘에 관하여 찾던 중, Levenshtein Distance 라는, 문자열 편집거리로 문자열의 유사도를 측정하는 알고리즘이 있는 것을 확인했습니다.
- 마침 아파치에서 개발되어진 commons-text라는 라이브러리가 존재함을 알게되어 해당 라이브러리를 사용하게 됐습니다.

## Jsoup 1.18
### 사용 목적
- 각 외부 검색 API 응답들을 병합할 때, HTML 태그가 섞여 있는 것들이 있어 문자열간의 유사도를 측정하기 어려움이 있었습니다.
- 해당 값들을 파싱하는 코드를 직접 작성하기보단, 이미 사전에 검증된 외부 라이브러리를 쓰는 것이 더욱 간편하고 코드에서도 깔끔하여, 선택했습니다.

## EHCache 3.10.0
### 사용 목적
- 추가적인 인프라 구축 없이, 많은 트래픽을 처리하기 위해 캐시를 물색했습니다.
- Memcached, Redis, Caffein, EHCache... 등등 많은, 대상자가 있었습니다.
- 이 중, 제가 필요한 기능은 다음과 같습니다.
  1. 어플리케이션과 생명주기를 함께 할 수 있는지? (별도의 관리포인트가 없는지)
  2. 디스크 저장 기능이 존재하는지? (이는, 실제 서비스를 운영한다는 가정하에, 서버 인스턴스를 재기동할 때마다, 검색 키워드 순위 정보 초기화를 방지하기 위함입니다.)
  3. Java, Spring과 궁합이 좋은지?
  4. 동시성 제어에 대한 해결책이 있는지?
  
- 위 기준으로 볼 때, 다수가 2번에서 기준미달이 되었고, EhCache만이 기준에 적합하여, 선택하게되었습니다.

# 2. 기술적 요구 사항

### 2-1. 헥사고날 아키텍처 현재 소스 구조

### 2-2. 동시성 이슈 제어
- 사용자가 특정 장소명으로 검색 시, Counting 서비스가 함께 실행되게 됩니다.
- 다수의 사용자가 동일한 키워드로 요청을 할 경우, 요청 건수가 사용자 요청 수 만큼 책정되지 않을 수 있습니다.
- 이를 JSR 107 에서 제공하는 Cache 객체의 invoke 메서드를 사용했습니다.
- invoke 메서드는 내부적으로 AtomicReference로 구현되어, 동시 접근에도 Thread safe하게 처리할 수 있습니다.
```java
public Long incrementKeywordCache(String keyword) {
	try {
		return keywordCountCache.invoke(keyword, ((entry, objects) -> {
			Long currentCount = entry.getValue();
			Long newCount = currentCount == null ? 1 : currentCount + 1;
            entry.setValue(newCount);
            return newCount;
		}));
	} catch (Exception e) {
		log.error("캐시 업데이트 실패 keyword : {}, Exception={}", keyword, e.getStackTrace());
		return 0L;
	}
}
```

### 2-3. 검색 서비스 외부 API 장애 발생 시, 대응 방식

### 2-4. 대용량 트래픽 처리 대응
1. EHCache로 장소 정보 및 검색어를 캐싱하였으며, 디스크에도 해당 정보를 저장하여, 서버 재기동시에도 영속화된 데이터를 재 캐싱하여 볼 수 있습니다.
2. Undertow에 HttpHandler를 Semaphore를 추가하여, Backpressure 기능을 넣었습니다. 동시 처리가능 수를 넘어간 상황에서 지정한 타임아웃을 넘기면 Http status 429 Too Many Requests를 
응답하도록 개발하였습니다.
```java 
@Bean
public UndertowServletWebServerFactory undertowFactory() {

	Semaphore semaphore = new Semaphore(500, true);

	UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();

	factory.addDeploymentInfoCustomizers(deploymentInfo -> {
		deploymentInfo.addInitialHandlerChainWrapper(nextHandler -> {
			return (exchange -> {
				if (semaphore.tryAcquire(3000, TimeUnit.MILLISECONDS)) {
					try {
						nextHandler.handleRequest(exchange);
					} finally {
						semaphore.release();
					}
				} else {
					exchange.setStatusCode(StatusCodes.TOO_MANY_REQUESTS);
					exchange.endExchange();
				}
			});
		});
	});

	return factory;
}
```
3. Cache Stempede 대응 방안 (?)

4. 테스트 코드

5. 향후, 추가 검색 API 서비스 연동 발생 시에는 다음과 같은 작업을 진행합니다. 
   1. PlaceSearchPort interface 구현 
   ```java
   public interface PlaceSearchPort<P extends PlaceSearchApiRequest, R extends PlaceSearchApiResponse> {
	      R searchPlaces(P p);
   }
   ```
   2. PlaceSearchApiFactory에 PlaceSearchPort 구현부 등록
   ```java
    @SuppressWarnings("rawtypes")
    @Component
    public class PlaceSearchApiFactory {
    
        private final Map<SearchApiType, PlaceSearchPort<PlaceSearchApiRequest, PlaceSearchApiResponse<?>>> portMap;
    
        @SuppressWarnings("unchecked")
        public PlaceSearchApiFactory(@Qualifier("kakaoPlaceSearchAdapter") PlaceSearchPort kakaoPlaceSearchAdapter,
                                     @Qualifier("naverPlaceSearchAdapter") PlaceSearchPort naverPlaceSearchAdapter) {
    
            this.portMap = Map.of(SearchApiType.KAKAO, kakaoPlaceSearchAdapter,
                                  SearchApiType.NAVER, naverPlaceSearchAdapter);
        }
    
        public PlaceSearchPort getSearchApi(SearchApiType searchApiType) {
            return portMap.get(searchApiType);
        }
    }
   ```
   3. SearchApiType enum객체에 타입 추가
   ```java
    @Getter
    @AllArgsConstructor
    public enum SearchApiType {
    
        KAKAO(1),
        NAVER(0);
    
        private int additionalScore; // 검색어 정렬을 위한 가중치 입니다.
    }
   ```
   4. PlaceMapper interface 구현으로 Response -> Place 도메인 객체로 변환
   ```java
    public interface PlaceMapper<T> {
        Place toPlace(T t);
    }
   ```
   5. 위 과정을 완료 후, PlaceSearchService에 getPlace 메서드를 다른 서비스 API와 마찬가지로 추가해주면 됩니다.
   ```java
    @Cacheable(key = "#searchCriteria.keyword")
    @Override
    public List<Place> placeSearch(SearchCriteria searchCriteria) {
            CompletableFuture<List<Place>> kakaoPlaceList = getPlaces(SearchApiType.KAKAO, searchCriteria);
            CompletableFuture<List<Place>> naverPlaceList = getPlaces(SearchApiType.NAVER, searchCriteria);
    ...
    ```
   6. 이 후, 데이터 병합 정책이나, 갯수 정책에 따라, PlacesSliceStrategy, PlacesMergeStrategy를 수정 및 구현해주면 됩니다.
   ```java
    public interface PlacesSliceStrategy {
        List<Place> slice(List<Place> places, int sliceSize);
    }
   ...
    public interface PlacesMergeStrategy {
        List<Place> merge(List<Place> standardList, List<Place> targetList);
    }
   ```

6. 문자열 유사도, 위 경도 좌표 유사도 검증 로직
- 각각의 외부 연동 서비스 API의 응답 값에서, 장소명 / 지번 / 도로명 / 위경도 좌표에 대한 정의가 조금씩 다릅니다.
- 따라서, 문자열 '일치'로는 도저히 해결할 수 없는 동일한 장소의 데이터 병합 및 정렬 이슈가 있었습니다.
- 위 문제를 Levenshtein distance 알고리즘을 포함하는 Common-text 라이브러리를 사용하여, 지정한 기준 값 이상인 경우, 두 문자는 유사하다고 판단하였습니다.
- 각각의 유사도 기준 값은 다르고, 유사도들의 결과치를 &&, || 연산으로 equals 하다라고 판단하도록 하였습니다.
- 문자열 유사도 기준 값을 0.68로 잡은 이유는, 실제 동일한 장소를 각각 호출 후, 두 결과값의 문자열이 동일하다는 판다으로 가는 기준을 최소한으로 맞춘 값입니다. (이는, 운영 시스템에 따라, 미세 조정이 필요할 수 있습니다.)
- 위경도 값은 각각 응답값의 좌표 표현방식이 달랐습니다. 네이버 지역 검색 문서에 적힌 내용은 KATECH으로 적혀 있으나, 2023년 네이버 개발자 포럼에 WSG84로 변환됐다는 답변이 있었습니다. 
- 위치 정보 좌표계를 추측만 할 수 밖에 없었으나, 응답 값에 유사한 수치로 보였기에, 두 값의 차이를 0.9999일치 시, 유사한 위치라는 판단을 하였습니다. (더 정밀한 지리 정보 시스템에 사용해야하는 경우, 수치를 더 미세하게 조정해줘야 합니다.)
- 위 로직들은 Place 도메인 객체의 equals에서 사용하였습니다.

```java
@Override
public boolean equals(Object o) {
    if (o == null) {
        return false;
    }
    Place place = (Place) o;
    return isSimilarPlaceName(place) && (isSimilarAddress(place) || isSimilarRoadAddress(place) || isSimilarLocation(place));
}

private boolean isSimilarPlaceName(Place place) {
    if (StringUtils.isNotEmpty(placeName) && StringUtils.isNotEmpty(place.placeName)) {
        return StringSimilarityChecker.isSimilar(placeName, place.placeName);
    }
    return false;
}

private boolean isSimilarAddress(Place place) {
    if (StringUtils.isNotEmpty(address) && StringUtils.isNotEmpty(place.address)) {
        return StringSimilarityChecker.isSimilar(address, place.address);
    }
    return false;
}

private boolean isSimilarRoadAddress(Place place) {
    if (StringUtils.isNotEmpty(roadAddress) && StringUtils.isNotEmpty(place.roadAddress)) {
        return StringSimilarityChecker.isSimilar(roadAddress, place.roadAddress);
    }
    return false;
}

private boolean isSimilarLocation(Place place) {
    if (StringUtils.isNotEmpty(x) && StringUtils.isNotEmpty(y)
        && StringUtils.isNotEmpty(place.x) && StringUtils.isNotEmpty(place.y)) {
        return LocationSimilarityChecker.isSimilarLocation(x, y, place.x, place.y);
    }
    return false;
}
```

- 위 소스 내용을 요약하자면, 장소명이 유사하면서, 위치 정보 중 하나라도 유사하다면 동일한 두 장소는 동일하다라는 판단을 하는 로직입니다.

7. 서버 재기동을 하더라도 검색 랭킹 API에 대한 일관성
- 간혹 서버가 종료되어 인스턴스를 다시 올려야하는 경우가 있습니다.
- 정기/비정기적인 배포 혹은 시스템 이상으로 서비스가 내려가는 경우가 그렇습니다.
- 일반적인 메모리 캐시는, 해당 상황에서 정보가 초기화 됩니다. 
- 검색 키워드 조회 API의 경우, 위 상황마다 랭킹이 초기화 된다면, 사용자 입장에서 신뢰할 수 없을 것 입니다.
- EHCache는 디스크 저장 기능도 제공되는 캐시로, 해당 기능을 활성화 하여, 서버 어플리케이션 재기동에도, 신뢰할 수 있는 응답을 줄 수 있도록 하였습니다.
- 뿐만 아니라, 부하가 몰리는 중에 서비스 재기동 시, 캐싱된 정보가 없어 외부 자원을 다이렉트로 호출하는 상황도 예방할 수 있습니다. 
```xml
<ehcache:persistence directory="persistence"/> <!-- 데이터 저장 경로 입니다. -->

<ehcache:cache-template name="PLACE_SEARCH_KEYWORD_COUNTER_TEMPLATE">
...
    <ehcache:disk unit="MB" persistent="true">20</ehcache:disk> <!-- 영속화 여부 설정입니다. -->
...
</ehcache:cache-template>
```
# 3. If Release
## 추가 인프라 구축 시, 연동 방안 (Terracota / Redis 등 Global Cache)
## 글로벌 캐시
## Gateway 증설
## 

# 4. API TEST 방법

### 1. 소스 경로로 이동하여 bootJar 생성한다.
```bash
./gradlew clean bootJar
```

### 2. bootJar가 생성된 경로인, 소스의 build/libs 폴더로 이동해 jar를 실행합니다.
```bash
java -jar build/libs/graceful-place-search-0.0.1-SNAPSHOT.jar
```

### 3. API 테스트용 curl을 실행합니다.
#### 3-1. 장소 조회 API
```bash
curl -X GET -H "Content-Type: application/json" http://localhost:8080/v1/places/${placeName}
```

#### 3-2. 키워드 랭킹 TOP10 조회 API
```bash
키워드 랭킹 TOP10 조회 API : curl -X GET -H "Content-Type: application/json" http://localhost:8080/v1/places/search/keywords
```
