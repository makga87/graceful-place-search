# Graceful Place Search

# 사용 아키텍처 & 사용 라이브러리

## Hexagonal Architecture
### 사용 목적
### 선택 사유

## Undertow
### 사용 목적
### 선택 사유

## EHCache 3.10
### 사용 목적
### 선택 사유

## jsoup 1.18
### 사용 목적
### 선택 사유


## Proj4j 1.3.0
### 사용 목적
### 선택 사유

## Common Text 1.12.0
### 사용 목적
### 선택 사유

# 기술적 시행착오

# API TEST 방법

### 1. 소스 경로로 이동하여 bootJar 생성한다.
```bash
./gradlew clean bootJar
```

### 2. bootJar가 생성된 경로인, 소스의 build/libs 폴더로 이동해 jar를 실행한다.
```bash
java -jar build/libs/graceful-place-search-0.0.1-SNAPSHOT.jar
```

### 3. API 테스트용 curl을 실행한다.
#### 3-1. 장소 조회 API
```bash
curl -X GET -H "Content-Type: application/json" http://localhost:8080/v1/places/${placeName}
```

#### 3-2. 키워드 랭킹 TOP10 조회 API
```bash
키워드 랭킹 TOP10 조회 API : curl -X GET -H "Content-Type: application/json" http://localhost:8080/v1/places/search/keywords
```
