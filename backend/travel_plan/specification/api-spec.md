# Travel-Plan 도메인 API 명세서
travel 구조
```json
{
  "travelId": "travel_id",
  "title": "travel_title",
  "startDate": "start_date",
  "days": [
    {
      "dayId": "day_id",
      "waypoints": [
        {
          "waypointId": "waypoint_id",
          "memo": "memo",
          "time": "time",
          "placeId": "place_id"
        }
      ]
    }
  ]
}
```

# 공통 사항
여행을 변경하는 모든 API의 응답에, `etag` 헤더를 붙여서 응답합니다.
`etag` 헤더는 여행의 버전을 나타내는 토큰입니다.
여행을 변경하는 모든 API의 요청에, `If-Match` 헤더를 붙여서 요청합니다.
`If-Match` 헤더는 여행의 버전을 나타내는 토큰입니다.
`If-Match` 헤더의 값과 `etag` 헤더의 값이 다르면, `412 Precondition Failed` 에러를 응답합니다.

# Travel 관련 API

## POST /v1/travels
여행 생성
### Request Body
```json
{
  "title" : "travel_title",
  "startDate" : "start_date",
  "dayCount" : 3
}
```
### Response Body
```json
{
  "travelId": "travel_id",
  "title": "travel_title",
  "startDate": "start_date",
  "days": [
    {
      "dayId": "day_id"
    },
    {
      "dayId": "day_id"
    },
    {
      "dayId": "day_id"
    }
  ]
}
```

## GET /v1/travels/{travelId}

# Waypoint 관련 API

## POST /v1/travels/{travelId}/days/{dayId}/waypoints
특정 날짜에 여행 경유지들 추가

### Request Body
```json
{
  "insertionType": "AFTER_WAYPOINT",
  "referenceWaypointId": "waypoint_id",
  "places": ["place_id", "place_id"]
}
```
insertionType : AFTER_WAYPOINT, BEFORE_WAYPOINT, FIRST, LAST, OPTIMAL

### Response
```json
{
  "dayId": "day_id",
  "waypoints": [
    {
      "waypointId": "waypoint_id",
      "memo": "memo",
      "time": "time",
      "place": {
        "placeId": "place_id",
        "location": {
          "latitude": "latitude",
          "longitude": "longitude"
        }
      }
    }
  ]
}
```

## POST /v1/travels/{travelId}/waypoints
여행 경유지들 추가(최적 경로)

### Request Body
```json
{
  "insertionType": "OPTIMAL",
  "places": ["place_id", "place_id"]
}
```

### Response Body
```json
{
  "travelId": "travel_id",
  "title": "travel_title",
  "startDate": "start_date",
  "days": [
    {
      "dayId": "day_id",
      "waypoints": [
        {
          "waypointId": "waypoint_id",
          "memo": "memo",
          "time": "time",
          "place": {
            "placeId": "place_id",
            "location": {
              "latitude": "latitude",
              "longitude": "longitude"
            }
          }
        }
      ]
    }
  ]
}
```

## GET /v1/travels/{travelId}/days/{dayId}/waypoints/optimal-route
특정 날짜에 여행 경유지들 최적 경로 조회 (서버측 변경 없음)

### Response Body
```json
{
  "waypointIds": ["waypoint_id", "waypoint_id"]
}
```

## GET /v1/travels/{travelId}/waypoints/optimal-route
여행 경유지들 최적 경로 조회 (서버측 변경 없음)

### Response Body
```json
{
  "days": [
    {
      "dayId": "day_id",
      "waypointIds": ["waypoint_id", "waypoint_id"]
    },
    {
      "dayId": "day_id",
      "waypointIds": ["waypoint_id", "waypoint_id"]
    }
  ]
}
```

## PUT /v1/travels/{travelId}/days/{dayId}/waypoints/order
특정 날짜에 여행 경유지들 전체적인 순서 변경 api

### Request Body
```json
{
  "waypoints": ["waypoint_id", "waypoint_id"]
}
```

## PUT /v1/travels/{travelId}/waypoints/order
여행 경유지들 전체적인 순서 변경 api

### Request Body
```json
{
  "days": [
    {
      "dayId": "day_id",
      "waypointIds": ["waypoint_id", "waypoint_id"]
    },
    {
      "dayId": "day_id",
      "waypointIds": ["waypoint_id", "waypoint_id"]
    }
  ]
}
```
기존의 waypoints 목록에서 하나도 추가되거나 삭제되면 안된다.

### Response Body
```json
{
  "days": [
    {
      "dayId": "day_id",
      "waypoints": ["waypoint_id", "waypoint_id"]
    },
    {
      "dayId": "day_id",
      "waypoints": ["waypoint_id", "waypoint_id"]
    }
  ]
}
```

## PUT /v1/travels/{travelId}/days/{dayId}/waypoints/{waypointId}
여행 경유지 수정

### Request Body
```json
{
  "memo": "memo",
  "time": "time"
}
```

### Response Body
```json
{
  "waypointId": "waypoint_id",
  "memo": "memo",
  "time": "time",
  "place": {
    "placeId": "place_id"
  }
}
```

## DELETE /v1/travels/{travelId}/days/{dayId}/waypoints/{waypointId}
여행 경유지 삭제



