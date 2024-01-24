# API Gateway Specification

## 인가 로직
API Gateway는 이제 인증 인가 로직을 담당하지 않습니다. 
인증 인가 로직은 각 서비스에서 처리합니다.

## Correlation ID
API Gateway 에서는 두개의 필터가 동작합니다.
- `click.porito.gateway.filter.CorrelationIdGrantFilter` : Correlation ID가 없는 경우 생성합니다.
- `click.porito.gateway.filter.ResponseFilter` : 응답시 Correlation ID를 헤더에 추가합니다.

Correlation ID는 `X-Correlation-Id` 헤더에 저장됩니다.