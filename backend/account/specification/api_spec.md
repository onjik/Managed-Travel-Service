# Account Service API 명세서

# 📍 도메인 소개
인증, 인가, 회원 정보에 대한 포괄적인 관리를 포함하는 도메인 입니다.

# 📍 role (역할)
- `ADMIN` : 관리자 권한을 상징합니다
- `USER` : 일반적인 사용자를 상징합니다.
- `ANNONYMOUS` : 로그인 되지 않은 사용자를 의미합니다.(일반적인 경우 외부로 보일 일이 없음)

# 📍 서브 도메인

## 계정 도메인 : `/account/*`
현재 로그인한 자신의 정보를 정의합니다.

## 유저 도메인 : `/users/*`
자신이 아닌 다른 유저의 정보를 정의합니다.

# 📍 공통 응답 사항
이 응답 사항은 모든 응답에서 발생할 수 있으며, 따로 명시 하지 않습니다. 혹시나 각 API 별로, 동일 상태 코드에 대한 추가적인 응답이 있을 수 있는 경우에만 명시합니다.
## 5xx : Server Error
API 호출 중 서버측 에러가 발생하면, 5xx를 상태코드와 함께 아래와 같이 응답합니다.
```json
{
    "domain" : "{}",
    "errorCode" : "{}",
    "description" : "{}"
}
```

## 400 : Bad Request
API 호출에서 필드의 제한사항을 준수하지 않았을 경우 발생한다(길이, 타입 등)
```json
{
  "invalidFields" : ["{}", "{}"] 
}
```

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

# 📍 계정 도메인 `/account`
## 로그인 된 자신 계정의 상세 정보 조회
### Request
`GET /account`
### Response
#### 200 : 성공적 조회
```json
{
  "userId": "{long}",
  "name": "{name}",
  "email": "{email}",
  "roles": [
    "USER"
  ],
  "createdAt": "2023-10-08T03:59:55.536658Z",
  "gender": "MALE|FEMALE", 
  "birthDate": "{yyyy-MM-dd}",
  "profileImgUri": "{uri}",
  "locale": "{사용자의 언어로, BCP 47 언어 태그로 표시됩니다.}",
  "emailVerified": "{boolean}"
}
```
gender, birthDate, pictureUri, locale, emailVerified 는 선택적 필드입니다.

## 자신의 간단 프로파일 조회
### Request
`GET /account/profile`
### Response
#### 200 : 성공적 조회
```json
{
  "userId": "{long}",
  "name": "{name}",
  "profileImgUri": "{uri}"
}
```
gender, birthDate, pictureUri, locale, emailVerified 는 선택적 필드입니다. (null 일 수 있음)

## 계정 정보 수정 요청
### Request
`PATCH /account/profile`
```json
{
  "name" : "string(2~20)",
  "gender" : "MALE|FEMALE",
  "birthDate" : "yyyy-MM-dd"
}
```
이 중 원하는 필트만 정의
### Response
#### 204 : 성공적으로 수정됨

#### 400 : 잘못된 요청


## 자신의 프로필 이미지 등록 엔드포인트 요청
### Request
`GET /account/profile/image/signed-put-url`
#### Query Parameter
- `filename` : 파일 이름 (필수, 확장자와 함께)
  - 지원되는 타입은 png, jpg, jpeg, gif 입니다. 

### Response
#### 200 : 성공적으로 발급됨
- content-type : text/plain;charset=UTF-8
- body : PUT 요청을 보낼 수 있는 uri

#### 400 : 잘못된 요청
- 쿼리스트링을 전달하지 않음
- 지원하지 않는 파일 확장자임

## 계정 프로필 사진 삭제, 기본 프로필로 바꾸기
### Request
`DELETE /account/profile/image`
### Response
#### 204 : 성공적으로 삭제됨

## 계정 삭제, 회원 탈퇴 요청
`DELETE /account`
### 204 : 삭제 요청이 잘 접수되었습니다.

# 로그인 로그아웃

## Auth2.0 login EntryPoint
이 Uri로 요청을 보낼 경우, 적절한 주소로 리다이렉트 시킵니다.
### Request
`GET /account/login/oauth2/authorization/{registrationId}`
```html
<a href="/account/login/oauth2/authorization/google">Google</a>
```
### Response
#### 302 Found
header
- `Location` : redirect uri


## Auth2.0 redirect endpoint
Auth2.0 인증 제공자에서 리다이렉트 시킬 api
### Request
`GET /login/oauth2/code/{registrationId}`
### Response
#### 200 : 성공적으로 인증이 완료되었습니다.
```json
{
  "userId" : "{long}",
  "name" : "{string}",
  "profileImageUri" : "{uri}",
  "roles" : ["{}", "{}","{}"]
}
```
#### 412(PRECONDITION_FAILED) : 새로운 회원이면서 회원 가입 정보 부족
```json
{
  "message" : "insufficient user info",
  "invalidFields" : ["{}", "{}"]
}
```
Invalid Fields
- name : string(2~50)
- email : email form
- gender : MALE | FEMALE
- birthDate : yyyy-MM-dd

## 회원가입 추가정보 보충 API
### Request
`POST /account/register`
```json
{
  "{invalidField}" : "{value}",
  "{invalidField}" : "{value}"
}
```

### Response
#### 200 : 성공적으로 보충되고, 회원가입 되고, 로그인 되었습니다.
```json
{
  "message" : "successfully registered and logged in"
}
```
#### 400 : 잘못된 요청
```json
{
  "message" : "insufficient user info"
}
```

## 로그아웃 API
### Request
`POST /account/logout`
### Response
#### 204 : 성공적으로 로그아웃 되었다.

# 📍 계정 도메인 `/users`
## 유저 프로필 간단 조회
### Request
`GET /users/{userId}/profile`
### Response
#### 200 : 성공적 조회
```json
{
  "userId": "{long}",
  "name": "{name}",
  "profileImgUri": "{uri}"
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