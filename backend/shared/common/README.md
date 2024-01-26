# 사용법
```java
@Configuration
@EnableCommonExceptionHandler
class SomeClass {
    ...
}
```
이 설정은 `click.porito.common.exception.CommonExceptionHandler` 를 등록합니다.

ServerThrownException 과 각종 기본 예외를 자동으로 처리해줍니다.

```java
@Configuration
@EnableDistributedRestTemplate
class SomeClass {
    ...
}
```

다음 코드를 등록합니다.

```java
@EnableDiscoveryClient
@Configuration(proxyBeanMethods = false)
public class DistributedRestTemplateAutoConfiguration {

    @Primary
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors()
                .add(new TraceContextInterceptor());
        return restTemplate;
    }

    @Order(1)
    @Bean
    public TraceContextFilter traceContextFilter() {
        return new TraceContextFilter();
    }
}
```