## External Event Topic 명명 규칙
카프카에서는 토픽 이름을 바꿀 수가 없으므로, 일정한 컨벤션을 통해 명명하고자 합니다.

`<{Dataset Name}.{Data Name}>-<behavior>-<message_type>-<version>`

## Allowed Charactor
- `' - '`
- `' . '`

## Message Type
- `logging` : 로그 데이터, 시스템 로그, 모니터링 로그 등
- `queuing` : 전형적인 큐잉을 위한 사용
- `tracking` : 트래킹 하는 이벤트, statelss 한 이벤트, 주로 aggregation 에 사용된다.
- `changing` : 특정 데이터가 변경되었음을 나타낸다, stateful한 이벤트, 주로 cdc에 사용된다.
- `streaming` : 스트리밍 처리 파이프라인에 의해 생성된 중계 역할을 하는 토픽
- `push` : 배치성 데이터, 오프라인 쪽에서 수집된 데이터(IOT등)

## Dataset Name
이는 서버의 이름이나, 논리적으로 정해진 비즈니스 도메인을 통해 특정하면 안된다. (변경 가능성이 있다.)

주로 변하지 않는 도메인 이름 (전통적인 데이터베이스에서 데이터 베이스 이름)으로 작성해야한다.

계층적으로 명명하는 것이 가능하다 이 경우 점('.')기호를 사용하자

## Data_Name
전통적인 데이터베이스에서 테이블 명에 해당되는 이름이다.

## behavior
이 이벤트가 무엇에 관한 이벤트인지 정의한다.

## Version
혹시나 이벤트의 페이로드가 바뀌게 될 경우, 자연스러운 전환을 가능하게 하기 위해 도입하였습니다.