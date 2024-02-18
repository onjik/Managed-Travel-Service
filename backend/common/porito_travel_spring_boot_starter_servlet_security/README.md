# 사용법
```java
@Configuration
@EnableJwtOperation
public class SecurityConfig {
    ...
}
```
이렇게 설정하면 기본으로 설정된 JwtOperation 이 bean 으로 등록됩니다.

```java
@Configuration
@EnableJwtPermitAllSecurityChain
class SecurityConfig {
    ...
}
```

이렇게 설정하면, 
- Jwt 필터가 적용되어 있고
- 다른 기본 설정은 모두 꺼져있고,
- 세션은 Stateless 로 설정되어 있고,
- method security 가 활성화 되어 있는 필터가 활성화 됩니다.

```java
@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@EnableJwtOperation
@ConditionalOnBean(HttpSecurity.class)
public class JwtPermitAllSecurityChainAutoConfiguration {
    @Bean
    public SecurityFilterChain config(
            HttpSecurity http,
            ApplicationEventPublisher applicationEventPublisher,
            JwtOperation jwtOperation
    ) throws Exception {
        // disable unnecessary security features
        CommonSecurityUtils.clearDefaults(http);
        // exception handling
        http.exceptionHandling(
                exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new UnauthorizedAuthenticationEntryPoint()) // on AuthenticationException, respons with 401
                        .accessDeniedHandler(new ForbiddenAccessDeniedHandler()) // on AccessDeniedException, response with 403
        );
        // authentication, authorization
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtOperation);
        if (applicationEventPublisher != null) {
            jwtAuthenticationFilter.setApplicationEventPublisher(applicationEventPublisher);
        }
        return http
                .authorizeHttpRequests(
                        c -> c.anyRequest().permitAll()
                )
                .addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}

```
