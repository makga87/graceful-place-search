# Graceful Place Search


# LIBRARY
# Hexagonal Architecture
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

## jsoup 1.18
### 사용 목적
- 각 외부 검색 API 응답들을 병합할 때, HTML 태그가 섞여 있는 것들이 있어 문자열간의 유사도를 측정하기 어려움이 있었습니다.
- 해당 값들을 파싱하는 코드를 직접 작성하기보단, 이미 사전에 검증된 외부 라이브러리를 쓰는 것이 더욱 간편하고 코드에서도 깔끔하여, 선택했습니다.

# 기술적 시행착오



# API TEST 방법

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
