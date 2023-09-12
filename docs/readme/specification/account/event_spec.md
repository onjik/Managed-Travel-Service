# Account Service Event Specification


## tracking
### account.authentication
인증과 관련된 이벤트의 모음입니다.
#### account.authentication-success-tracking-{version}
인증이 성공적으로 이루어 졌음을 의미합니다.

아직 이름이나 추가정보를 입력하지 않았을 경우 제한된 역할로 로그인이 성공하는데 이러한 경우도 login.success 이벤트를 발생시킵니다.
```json
{
    "user_key" : "{USER_KEY}",
    "ip_address" : "{IP_ADDRESS}",
    "user-agent" : "{USER_AGENT}", 
    "auth_method" : "{AUTH_METHOD}", 
    "auth_provider" : "{AUTH_PROVIDER}" 
}
```
- IP_ADDRESS : 로그인을 시도한 ip
- USER_AGENT : optional, http 헤더의 user-agent 값을 그대로 전달
- ROLE : ROLE_{} : 부여된 역할을 나타냄
- AUTH_METHOD : 현재는 auth2.0 밖에 없지만 password 같은거 추가 가능
- AUTH_PROVIDER : auth2.0일 경우만 표시, ex) google, facebook
#### account.authentication-fail-tracking-{version}
인증 시도가 실패했음을 나타냅니다.
```json
{
    "user_key" : "{USER_KEY}",
    "ip_address" : "{IP_ADDRESS}",
    "user-agent" : "{USER_AGENT}", 
    "auth_method" : "{AUTH_METHOD}", 
    "auth_provider" : "{AUTH_PROVIDER}" ,
    "reason" : "{REASON}"
}
```
- IP_ADDRESS : 로그인을 시도한 ip
- USER_AGENT : optional, http 헤더의 user-agent 값을 그대로 전달
- AUTH_METHOD : 현재는 auth2.0 밖에 없지만 password 같은거 추가 가능
- AUTH_PROVIDER : auth2.0일 경우만 표시, ex) google, facebook
- REASON : 로그인이 실패한 이유

### account.session
#### account.session-create-tracking-{version}
```json
{
    "user_key" : "{USER_KEY}",
    "roles" : ["{ROLE}","{ROLE}"]
}
```
#### account.session-drop-tracking-{version}
```json
{
    "user_key" : "{USER_KEY}",
    "roles" : ["{ROLE}","{ROLE}"]
}
```

### account.activation
#### account.activation-success-tracking-{version}
```json
{
    "user_key" : "{USER_KEY}",
    "ip_address" : "{IP_ADDRESS}",
    "user-agent" : "{USER_AGENT}"
}
```
- IP_ADDRESS : 로그인을 시도한 ip
- USER_AGENT : optional, http 헤더의 user-agent 값을 그대로 전달

#### account.activation-fail-tracking-{version}
```json
{
    "user_key" : "{USER_KEY}",
    "ip_address" : "{IP_ADDRESS}",
    "user-agent" : "{USER_AGENT}"
}
```
- IP_ADDRESS : 로그인을 시도한 ip
- USER_AGENT : optional, http 헤더의 user-agent 값을 그대로 전달

### security.issue
#### security.issue-resoleved-tracking-{version}
```json
{
    "security_issue_key" : "{SECURITY_ISSUE_KEY}",
    "reason" : "{REASON}"
}
```
- SECURITY_ISSUE_KEY : 부여된 보안 이슈 키
- REASON : 보안 이슈가 해제된 이유

#### security.issue.expired-emerged-tracking-{version}
```json
{
    "security_issue_key" : "{SECURITY_ISSUE_KEY}",
    "user_key" : "{USER_KEY}",
    "details" : "{DETAILS}",
    "last_login_time" : "{TIME_MILLS}"
}
```

#### security.issue.locked-emerged-tracking-{version}
```json
{
    "security_issue_key" : "{SECURITY_ISSUE_KEY}",
    "user_key" : "{USER_KEY}",
    "details" : "{DETAILS}",
    "expiration_time" : "{TIME_MILLS}",
    "reason" : "{REASON}"
}
```

#### security.issue.ip-emerged-tracking-{version}
```json
{
    "security_issue_key" : "{SECURITY_ISSUE_KEY}",
    "subnet" : "{CIDR}",
    "since" : "{TIME_MILLS}",
    "until" : "{TIME_MILLS}"
}
```

#### security.issue.disabled-emerged-tracking-{version}
```json
{
    "security_issue_key" : "{SECURITY_ISSUE_KEY}",
    "user_key" : "{USER_KEY}",
    "details" : "{DETAILS}"
}
```

## changing
### account.user
#### account.user-update-changing-{version}
```json
{
    "user_key" : "{USER_KEY}",
    "name" : "{NAME}",
    "email" : "{EMAIL}",
    "gender" : "{GENDER}",
    "birth_date" : "{yyyy-MM-dd}"
}
```

#### account.user-delete-changing-{version}
```json
{
    "user_key" : "{USER_KEY}"
}
```


