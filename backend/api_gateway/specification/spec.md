# API Gateway Specification

## 인가 로직
API Gateway 단에서 토큰 검증을 실시하고, 성공한 경우 포워딩 합니다.

포워딩 할때는 토큰을 제거하고, 사용자 정보를 헤더에 추가합니다.

```java
public interface CustomHeaders {
    public final static String X_USER_ID = "X-Authorization-Id";
    public final static String X_USER_ROLES = "X-Authorization-Roles";

}
```

```java
public record JwtPayLoad(
        String userId,
        Collection<String> roles
) {
}
```