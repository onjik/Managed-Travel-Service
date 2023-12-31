# Account Service Event Specification

## account.authentication
인증과 관련된 이벤트의 모음입니다.
### account.authentication-success-tracking-{version}
인증 성공 후 토큰 발급됨
#### payload
```json
{
  "userId" : {
    "type" : "string",
    "optional" : false,
    "description" : "유저 아이디",
    "example" : "1234567890"
  },
  "requestIp" : {
    "type" : "string",
    "optional" : false,
    "description" : "요청자 아이피 주소"
  },
  "requestUrl" : {
    "type" : "string",
    "optional" : false,
    "description" : "요청한 url"
  }
}
```

### account.authentication-fail-tracking-{version}
인증 실패
```json
{
  "requestIp" : {
    "type" : "string",
    "optional" : false,
    "description" : "요청자 아이피 주소"
  },
  "requestUrl" : {
    "type" : "string",
    "optional" : false,
    "description" : "요청한 url"
  },
  "exceptionName" : {
    "type" : "string",
    "optional" : false,
    "description" : "인증 실패 예외 이름"
  },
  "message" : {
    "type" : "string",
    "optional" : false,
    "description" : "인증 실패 세부 메시지(english)",
    "example" : "Trying to Register With Existing User Id"
  }
}
```

## account.principal
계정과 관련된 이벤트의 모음입니다.
### account.principal-put-changing-{version}
계정 생성됨, 수정됨
#### payload
```json
{
  "userId" : {
    "type" : "string",
    "optional" : false,
    "description" : "유저 아이디",
    "example" : "1234567890"
  },
  "username" : {
    "type" : "string",
    "optional" : false,
    "description" : "유저 이름",
    "example" : "username"
  },
  "email" : {
    "type": "string",
    "optional": false,
    "description": "유저 이메일(이메일 형식)",
    "example": "email@email.com"
  },
  "roles" : {
    "type" : "array",
    "optional" : false,
    "element" : "string",
    "elementDescription" : "유저 권한",
    "example" : ["ROLE_USER", "ROLE_ADMIN"]
  },
  "createdAt" : {
    "type" : "utc-timestamp",
    "optional" : false,
    "description" : "유저 생성일",
    "example" : "2020-01-01T00:00:00.000Z"
  },
  "gender" : {
    "type" : "string enum",
    "enums" : ["MALE", "FEMALE"], 
    "optional" : false,
    "description" : "유저 성별",
    "example" : "MALE"
  },
  "birthdate" : {
    "type" : "date",
    "optional" : false,
    "description" : "유저 생년월일",
    "example" : "2020-01-01"
  }
}
```
### account.principal-delete-changing-{version}
계정 삭제됨
#### 페이로드
```json
{
  "userId" : {
    "type" : "string",
    "optional" : false,
    "description" : "유저 아이디",
    "example" : "1234567890"
  }
}
```