# Swagger/OpenAPI 설정 가이드

## 개요
이 프로젝트는 **Springdoc OpenAPI 1.6.15**를 사용하여 REST API 문서를 자동으로 생성합니다.

## 접속 URL

| 항목 | URL | 설명 |
|------|-----|------|
| **Swagger UI** | http://localhost:8080/swagger-ui.html | API 문서 인터페이스 |
| **API Docs (JSON)** | http://localhost:8080/api-docs | OpenAPI 스펙 (JSON) |
| **API Docs (YAML)** | http://localhost:8080/api-docs.yaml | OpenAPI 스펙 (YAML) |

## 설정 파일

### 1. pom.xml
```xml
<!-- Swagger/OpenAPI -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-ui</artifactId>
    <version>1.6.15</version>
</dependency>
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-security</artifactId>
    <version>1.6.15</version>
</dependency>
```

### 2. application.yml
```yaml
# Swagger/OpenAPI 설정
springdoc:
  api-docs:
    path: /api-docs
    enabled: true
  swagger-ui:
    path: /swagger-ui.html
    enabled: true
    operations-sorter: method
    tags-sorter: alpha
    display-request-duration: true
  default-consumes-media-type: application/json
  default-produces-media-type: application/json
  packages-to-scan: com.ttwijang.cms
  paths-to-match: /api/**
```

### 3. SwaggerConfig.java
`com.ttwijang.cms.config.SwaggerConfig` 참조

## 주요 어노테이션 사용법

### 컨트롤러 레벨

```java
@Tag(name = "User", description = "사용자 관리 API")
@RestController
@RequestMapping("/api/user")
public class UserController {
    // ...
}
```

### 메소드 레벨

```java
@Operation(
    summary = "사용자 조회",
    description = "ID로 특정 사용자를 조회합니다.",
    security = @SecurityRequirement(name = "bearerAuth")
)
@ApiResponses({
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = UserDto.Detail.class))),
    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
    @ApiResponse(responseCode = "401", description = "인증 필요")
})
@GetMapping("/{id}")
public ResponseEntity<UserDto.Detail> getUser(
        @Parameter(description = "사용자 ID", required = true, example = "1")
        @PathVariable Long id) {
    // ...
}
```

### DTO 레벨

```java
@Schema(description = "사용자 상세 정보")
public class UserDetailDto {
    
    @Schema(description = "사용자 ID", example = "user123")
    private String userId;
    
    @Schema(description = "이메일", example = "user@example.com")
    private String email;
    
    @Schema(description = "이름", example = "홍길동")
    private String name;
    
    @Schema(description = "권한", example = "ROLE_USER", allowableValues = {"ROLE_USER", "ROLE_ADMIN"})
    private String authority;
}
```

## 주요 어노테이션 정리

| 어노테이션 | 레벨 | 용도 |
|-----------|------|------|
| `@Tag` | 컨트롤러 | API 그룹 지정 |
| `@Operation` | 메소드 | API 설명 |
| `@ApiResponses` / `@ApiResponse` | 메소드 | 응답 코드 및 내용 설명 |
| `@Parameter` | 파라미터 | 파라미터 설명 |
| `@Schema` | DTO 클래스/필드 | 데이터 모델 설명 |
| `@SecurityRequirement` | 메소드 | 인증 필요 표시 |

## JWT 인증 사용법

1. Swagger UI에 접속 (http://localhost:8080/swagger-ui.html)
2. 우측 상단의 **Authorize** 버튼 클릭
3. JWT 토큰 입력 (**Bearer 제외**, 토큰만 입력)
4. **Authorize** 버튼 클릭하여 인증 완료

## 예시

### 기본 GET API
```java
@Operation(summary = "사용자 목록 조회")
@GetMapping
public ResponseEntity<Page<UserDto>> list(
        @Parameter(description = "페이지 번호", example = "0")
        @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "페이지 크기", example = "10")
        @RequestParam(defaultValue = "10") int size) {
    // ...
}
```

### POST API with RequestBody
```java
@Operation(summary = "사용자 생성", description = "새로운 사용자를 생성합니다.")
@ApiResponses({
    @ApiResponse(responseCode = "200", description = "생성 성공"),
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
})
@PostMapping
public ResponseEntity<Void> create(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "생성할 사용자 정보",
            required = true,
            content = @Content(schema = @Schema(implementation = UserCreateDto.class))
        )
        @RequestBody @Valid UserCreateDto dto) {
    // ...
}
```

### PathVariable과 함께 사용
```java
@Operation(summary = "사용자 삭제")
@DeleteMapping("/{id}")
public ResponseEntity<Void> delete(
        @Parameter(description = "삭제할 사용자 ID", required = true)
        @PathVariable Long id) {
    // ...
}
```

## 참고사항

- **Spring Boot 2.x**: Springdoc OpenAPI 1.x 사용 (현재 버전: 1.6.15)
- **Spring Boot 3.x**: Springdoc OpenAPI 2.x 사용 필요
- JWT 토큰은 `Bearer` 접두사 없이 입력
- Spring Security에서 Swagger 경로 허용 설정 필수
  - `/swagger-ui/**`
  - `/swagger-ui.html`
  - `/api-docs/**`
  - `/v3/api-docs/**`

## 문제 해결

### Swagger UI에 접속되지 않을 때
1. Spring Security 설정 확인 (`SecurityConfig.java`)
2. application.yml의 `springdoc.swagger-ui.enabled` 설정 확인
3. 서버 재시작

### API가 표시되지 않을 때
1. `springdoc.packages-to-scan` 설정 확인
2. `@RestController` 어노테이션 확인
3. `@RequestMapping` 경로가 `/api/**` 패턴과 일치하는지 확인
