# Account Service API 명세서

# 📍 도메인 소개
인증, 인가, 회원 정보에 대한 포괄적인 관리를 포함하는 도메인 입니다.

# 📍 role (역할)
역할은 각각의 리소스에 대한 접근을 제어하는데 사용됩니다.

역할들은 `ROLE_`이라는 prefix가 붙게 됩니다.

## 일반 역할
중심 역할로, 데이터 베이스에 영속화 되어 있는 역할을 의미합니다. 사용자의 부여된 역할로 이해해도 됩니다.

- `ROLE_ADMIN` : 관리자 권한을 상징합니다
- `ROLE_USER` : 일반적인 사용자를 상징합니다.
- `ROLE_ANNONYMOUS` : 로그인 되지 않은 사용자를 의미합니다.(일반적인 경우 외부로 보일 일이 없음)

## 특수 역할 (`ROLE_SPECIAL_{}`)
계정의 상태에 따라 임시로 부여된 역할입니다. 로그인 시 계정의 보안 이슈나, 계정의 정보를 확인하여, 특이 사항이 발생하면, 본래의 역할 대신 특수 역할이 발급됩니다.(본 역할은 발급되지 않습니다.)

- `ROLE_SPECIAL_EXPIRED_ACCOUNT` : 만료된 계정을 가지고 로그인한 사용자를 의미합니다.
- `ROLE_SPECIAL_INCOMPLETE` : 필수 항목이 채워지지 않은 유저를 의미합니다.

# 📍 서브 도메인

## 계정 도메인 : `/account/*`
현재 로그인한 자신의 정보를 정의합니다.

## 유저 도메인 : `/users/*`
자신이 아닌 다른 유저의 정보를 정의합니다.

# 📍 공통 응답 사항
이 응답 사항은 모든 응답에서 발생할 수 있으며, 따로 명시 하지 않습니다. 혹시나 각 API 별로, 동일 상태 코드에 대한 추가적인 응답이 있을 수 있는 경우에만 명시합니다.
## 응답 바디 형식
모든 응답은 아래 응답을 포함함을 보장합니다.
```json
{
    "message" : "{}",
}
```
## 5xx : Server Error
API 호출 중 서버측 에러가 발생하면, 5xx를 상태코드와 함께 아래와 같이 응답합니다.
```json
{
    "message" : "{}",
    "error_code" : "{}"
}
```
에러 코드와 함께 서버 관리자에게 문의하라고 알려주면 됩니다.

메시지를 사용자에게 노출하는 것은 권장하지 않습니다.

## 401 : Unauthorization
사용자가 인증 정보가 없이, 인증이 필요한 (세션이 필요한) 리소스(API)에 접근을 했음을 나타냅니다.

다음과 같은 가능한 시나리오가 있습니다.
- 사용자가 로그인을 하지 않았음
- 사용자의 세션이 만료되었음
- 사용자가 보안 정책에 따라 세션을 빼았겼음

```json
{
    "message" : "{}"
}
```
메시지를 통해 어떠한 사유로 거절을 했는지 표시합니다.

## 403 : Forbidden
사용자가 인증 정보는 있으나, 현재 역할(role)이 이 리소스(API)에 접근할 권한이 없음을 나타냅니다.

가능한 시나리오
- 사용자가 권한이 없는 API를 호출
- API 요구사항에 있는 보안 사항을 충족하지 못함
- 비밀번호를 바꾸려고 하는데 이전 비밀번호를 잘못 입력
- 로그인을 한 상태에서 회원가입 API를 호출

```json
{
    "message" : "{}"
}
```

일반적인 경우, message로 원인을 설명하고, 추가적인 정보가 전달되는 경우, 각 API 명세에  기입

## 422 : Unprocessable Entity
요청이 서버에 정상적으로 전달이 되었지만, 요청의 형식이 잘못되어 처리할 수 없음을 나타냅니다.
- 데이터 타입이 잘못되었음
- 서버에 데이터 매핑이 되지 않는다
```json
{
  "message": "Invalid Format",
  "invalid_attribute" : [
    "{json attribute name}",
    "{query string name}",
    //...
  ]
}
```
잘못된 필드 명을 `invalid_attribute`에 담아서 응답합니다.

# 📍 계정 도메인 `/account`
현재 로그인 한 유저의 정보를 나타냅니다.

로그인 방법은 다음 방법이 제공됩니다.
- `auth2.0` 을 통한 로그인

## `auth2.0` 신규 로그인
1. auth2.0 인증을 위한 링크로 이동
2. auth2.0을 이용한 인증 시도
3. 자동으로 회원가입
4. `ROLE_SPECIAL_INCOMPLETE` 로 세션 발급(다른 곳 접근 안됨)
5. 이름 등록 API호출
6. 이름 성공적으로 등록시, `ROLE_USER`로 승급 후 세션 재발급

## 자신 계정의 상세 정보 조회
### Request
`GET /account`
### Response
#### 200 : 성공적 조회
```json
{
  "user_id" : "{long}",
  "name" : "{string}",
  "email" : "{email form}",
  "profile_img_uri" : "{url}",
  "roles" : ["ROLE_{}", "ROLE_{}","ROLE_{}"],
  "created_time" : "{yyyy-MM-dd HH:mm:ss}",
  "gender" : "{M or W, char(1)}",
  "birth_day" : "{yyyy-MM-dd}"
}
```
## 자신의 간단 프로파일 조회
### Request
`GET /account/profile`
### Response
#### 200 : 성공적 조회
```json
{
  "user_id" : "{long}",
  "name" : "{string}",
  "profile_img_uri" : "{url}",
  "roles" : ["ROLE_{}", "ROLE_{}","ROLE_{}"]
}
```

## 자신의 프로필 이미지 등록 엔드포인트 요청
### Request
`GET /account/profile_img/upload_uri`

### Response
#### 200 : 성공적으로 발급됨
```json
{
  "pre_signed_uri" : "{uri}"
}
```
이 uri 로 PUT 요청을 보내면 됨(aws presigned uri 참고)

### Response
#### 200 : 정상적으로 이름 등록
- 정상적으로 이름이 등록되어서 회원 가입이 완료되었음
- 역할이 업데이트 되어서 세션이 재발급 되었음
- 만약 `is_account_expired`가 true이면, 계정이 만료되었다는 것을 의미하며, 역할이 `ROLE_SPECIAL_EXPIRED_ACCOUNT`로 발급되었음을 의미한다.
- 역할이 `ROLE_SPECIAL_EXPIRED_ACCOUNT`이라면, 다른 API에 대한 호출이 제한되며, 이메일 인증 API만 호출이 가능함.
```json
```

## 이름 수정
### Request
`PUT /account/name`
```json
{
  "name" : "{string, length = [3,10]}"
}
```
### Response
#### 204 : 정상적으로 수정이 완료되었다.


## 계정 프로필 사진 삭제, 기본 프로필로 바꾸기
### Request
`GET /account/profile_img/delete_uri`
### Response
#### 200 : 성공적으로 발급됨
```json
{
  "pre_signed_uri" : "{uri}"
}
```

## 계정 삭제, 회원 탈퇴 요청
`DELETE /account`
### 202 : 삭제 요청이 잘 접수되었습니다.
바로 삭제되지는 않고 삭제되는데 조금 시간이 걸릴 수 있음. 다만 로그인 서비스는 즉시 반영되므로 로그인은 안됨

# 로그인 로그아웃

## Auth2.0 OpenId Redirect Endpoint
응답 값이 리다이렉트를 포함하지 않으므로, 프론트 측에서 리다이렉션 포인트를 제공하고, 이를 서버쪽으로 라우팅하여 흐름을 제어해야합니다.
### Request
`GET /account/login/oauth2/code/{registration id}`
### Response
가능한 응답 상황
- 성공적 로그인
  - 일반적인 역할로서 세션이 발급됨
- 계정이 만료됨
  - 계정이 오랫동안 활동하지 않아 계정이 만료되었음을 의미함
  - 계정 활성화 API를 통해 살리기 가능
  - `ROLE_SPECIAL_EXPIRED_ACCOUNT`의 역할로서 세션이 발급됨
- 계정이 잠김
  - 만료일이 존재하며, 그 시간까지 계정이 잠겼음을 의미함
  - 주로 정책 위반 등의 이유로 결정됨
  - 세션이 발급되지 않고, 로그인이 거부됨
- 계정이 비활성화 됨
  - 특정 관리자가 임의의 이유로 계정을 비활성화 했음을 의미함
  - 만료일이 정해져 있지 않다
  - 세션이 발급되지 않고, 로그인이 거부됨
- 추가 정보가 필요함
  - auth2.0으로 인해 필수 정보가 모두 입력되지 않았으며 제한된 역할만 부여되었음
  - `ROLE_SPECIAL_INCOMPLETE`의 역할로서 세션이 발급됨
  - 
#### 200 : 성공적으로 로그인이 되었습니다.
```json
{
  "user_info" : {
    "user_id" : "{long}",
    "name" : "{string}",
    "profile_img_uri" : "{url}",
    "roles" : ["ROLE_{}", "ROLE_{}","ROLE_{}"]
  }
}
```
#### 200 : 계정이 만료되었습니다.
```json
{
  "account_expired" : "true",
}
```

#### 200 : 계정 회원가입에 필요한 정보가 부족합니다.
```json
{
  "additional_info_required" : "true",
  "required_info" : ["{attribute name}","{attribute name}","{attribute name}"]
}
```
가능한 속성
- gender
- birth_day
- name

#### 403 : 로그인한 사용자가 호출할 경우
```json
{
  "message" : "No Permission"
}
```

#### 403 : 계정이 잠겼을 경우
```json
{
  "message" : "Account Locked",
  "until" : "{yyyy-MM-dd HH:mm:ss}",
  "since" : "{yyyy-MM-dd HH:mm:ss}",
  "reason" : "{string}"
}
```

#### 403 : 계정이 비활성화 되었을 경우
```json
{
  "message" : "Account Disabled",
  "since" : "{yyyy-MM-dd HH:mm:ss}"
}
```

## 추가정보 입력 API
`ROLE_SPECIAL_INCOMPLETE` 권한을 가진 사람만 호출할 수 있음, 첫 로그인시 부족한 정보를 채워 넣는데 사용되는 API

### Request
`POST /account/infos/supplement`
```json
{
  "attributes" : {
    "name" : "value",
    "name" : "value",
    "name" : "value"
  }
}
```
#### 200 : 성공적으로 로그인이 되었습니다.
세션이 재발급되었습니다.
```json
{
  "user_info" : {
    "user_id" : "{long}",
    "name" : "{string}",
    "profile_img_uri" : "{url}",
    "roles" : ["ROLE_{}", "ROLE_{}","ROLE_{}"]
  }
}
```

#### 200 : 계정 회원가입에 필요한 정보가 부족합니다.
```json
{
  "additional_info_required" : "false",
  "required_info" : ["{attribute}","{attribute}","{attribute}"]
}
```

#### 403 : 이미 정보가 유효한 사용자가 호출할 경우
```json
{
  "message" : "No Permission"
}
```

#### 403 : 계정이 잠겼을 경우
```json
{
  "message" : "Account Locked",
  "until" : "{yyyy-MM-dd HH:mm:ss}",
  "since" : "{yyyy-MM-dd HH:mm:ss}",
  "reason" : "{string}"
}
```

#### 403 : 계정이 비활성화 되었을 경우
```json
{
  "message" : "Account Disabled",
  "since" : "{yyyy-MM-dd HH:mm:ss}"
}
```


## 로그아웃 API
### Request
`POST /account/logout`
### Response
#### 204 : 성공적으로 로그아웃 되었다.

## 계정 활성화 API
계정이 만료되었을 때 호출하면, 이메일 인증 절차를 진행한다.

여러번 호출하면, 맨 마지막 호출만 유효하다.

### Request
`POST /account/activation`
### Response
#### 202 : 이메일 전송이 성공적으로 요청되었으며, 시간이 조금 걸릴 수 있음

## 계정 활성화 검증 API
이메일로 보낸 링크를 누르면, 이 API로 리다이렉트 되게 된다.
### Request
`GET /account/activation/validate?key={activation key}&email={email}`
### Response
#### 302 : 성공적으로 해제 되었다.
프론트의 홈으로 리다이렉트 시킨다.

# 📍 계정 도메인 `/users`
## 유저 프로필 간단 조회
### Request
`GET /users/{user_key}/profile`
### Response
#### 200 : 성공적 조회
```json
{
  "name" : "{string}",
  "profile_img_uri" : "{url}"
}
```

#### 404 : 요청한 유저를 찾을 수 없음
```json
{
  "message" : "User Not Found"
}
```

# 📍 관리자 API`/admin`
외부 공개 불가