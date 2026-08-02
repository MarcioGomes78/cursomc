# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Spring Boot 4.1 (Java 25) REST API for an e-commerce backend ("cursomc" — from a Udemy Spring Boot course). Package root: `com.mjgomes.cursomc`. Migrated from Spring Boot 3.5.16 after that line reached OSS end-of-support (~2026-06-30); see "Spring Boot 4 migration notes" below.

## Commands

Use the Maven wrapper (`mvnw.cmd` on Windows, `./mvnw` on Unix) — do not rely on a globally installed Maven.

```
mvnw.cmd clean install          # build + run tests
mvnw.cmd test                   # run all tests
mvnw.cmd test -Dtest=CursomcApplicationTests            # run a single test class
mvnw.cmd test -Dtest=CursomcApplicationTests#contextLoads  # run a single test method
mvnw.cmd spring-boot:run                                # run the app (defaults to the "test" profile, H2 in-memory)
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev  # run against local MySQL with sample data
```

There is effectively one test in the repo today (`CursomcApplicationTests`, a context-load smoke test).

## Spring profiles

Profile is selected via `spring.profiles.active` (default set to `test` in `application.properties`).

- **test** (`application-test.properties`): H2 in-memory DB, schema always recreated on startup, `DBService.instantiateTestDatabase()` reseeds sample data every run, email sending is mocked (`MockEmailService`). This is the default/local profile.
- **dev** (`application-dev.properties`): local MySQL (`curso_spring`), `ddl-auto=create`, sample data seeded only when the schema was just recreated (`DevConfig` checks `ddl-auto=create` before calling `DBService`), real SMTP email (`SmtpEmailService`).
- **prod** (`application-prod.properties`): MySQL on RDS, `ddl-auto=none` (no schema recreation, no reseeding), real SMTP email. Used via the `Procfile` (`web: java ... -Dspring.profiles.active=prod ...`), i.e. deployed as a Heroku-style dyno.

`application-test.properties` and `application-dev.properties` currently contain real-looking credentials (Gmail app password, MySQL password) committed to the repo — be aware of this when touching those files, and never add real secrets to `application-prod.properties` or commit new ones.

## Architecture

Classic layered Spring MVC structure, one package per layer rather than per feature:

- `domain/` — JPA entities (`Cliente`, `Produto`, `Categoria`, `Pedido`, `Endereco`, `Cidade`, `Estado`, `Pagamento` + subclasses, `ItemPedido`). Notable relationships:
  - `Categoria` <-> `Produto` is many-to-many.
  - `Pedido` (order) has one `Pagamento` (payment), which is polymorphic: `PagamentoComBoleto` or `PagamentoComCartao` (JPA single-table/joined inheritance).
  - `ItemPedido` (order line item) uses a composite primary key (`@EmbeddedId ItemPediodPK` of `Pedido` + `Produto`) and freezes `preco`/`desconto` at purchase time — it does not reflect later changes to `Produto`'s price.
  - `Cliente` stores `Perfil` (role) codes as a serialized set of ints (see `Cliente.addPerfil`/`getPerfis`), not a join table.
- `repositories/` — Spring Data JPA repositories, one per entity, no custom implementations beyond derived query methods (e.g. `ClienteRepository.findByEmail`, `PedidoRepository.findByCliente`).
- `services/` — business logic and authorization checks live here, not in resources/controllers. Key pattern: services call `UserService.authenticated()` to get the `UserSS` principal off `SecurityContextHolder` and throw `AuthorizationException` for access-control failures (e.g. `ClienteService.find` only allows a client to fetch their own record unless ADMIN; `PedidoService.findPage` only returns the authenticated client's own orders).
  - `DBService` seeds sample data for `dev`/`test` profiles (invoked from `DevConfig`/`TestConfig`, never in `prod`).
  - `EmailService` is an interface with `SmtpEmailService` (real) and `MockEmailService` (logs only) implementations, chosen per-profile in `DevConfig`/`TestConfig`.
  - `services/validation/` holds custom Bean Validation annotations/validators (`@ClienteInsert`, `@ClienteUpdate`) that run CPF/CNPJ checks (`validation/utils/BR.java`) and uniqueness checks against the repository — these run before the service layer via `@Valid` on the DTO in the resource.
- `resources/` — `@RestController`s, one per entity, thin: convert DTOs, delegate to a service, map to `ResponseEntity`. `resources/exception/ResourceExceptionHandler` is a `@ControllerAdvice` centralizing exception -> HTTP status mapping (`ObjectNotFoundException` -> 404, `DataIntegrityException` -> 400, `MethodArgumentNotValidException` -> 400 with per-field errors, `AuthorizationException` -> 403).
- `dto/` — request/response DTOs (e.g. `ClienteDTO` for updates vs `ClienteNewDTO` for registration, which carries password/address/phone fields the update flow doesn't allow changing).
- `security/` — stateless JWT auth:
  - `JWTUtil` signs/validates HS256 tokens (`jwt.secret`/`jwt.expiration` properties).
  - `JWTAuthenticationFilter` handles login (`POST` to the security filter, issues a token).
  - `JWTAuthorizationFilter` runs per-request, validates the `Authorization: Bearer` header, and populates `SecurityContextHolder`.
  - `UserSS` is the `UserDetails` principal exposing the client id and roles; `UserDetailsServiceImpl` loads it from `Cliente`.
  - `config/SecurityConfig` wires the filters, defines public endpoints (`PUBLIC_MATCHERS`, `PUBLIC_MATCHERS_GET` for read-only product/category browsing, `PUBLIC_MATCHERS_POST` for client registration and forgot-password), disables CSRF (stateless JWT API), and enables permissive CORS.
  - `resources/AuthResource` + `services/AuthService` handle the two non-login `/auth` endpoints: `POST /auth/refresh_token` (re-issues a token for the currently authenticated user) and `POST /auth/forgot` (generates a new random password, saves it BCrypt-encoded, and emails it via `EmailService`). Login itself (`POST` to the security filter chain, not a resource method) is handled by `JWTAuthenticationFilter` reading a `CredenciaisDTO` (email/senha).
- `config/` — `SecurityConfig`, `JacksonConfig` (JSON mapping config), plus the profile-scoped `DevConfig`/`TestConfig` described above.
- `enums/` — `Perfil` (role: CLIENTE/ADMIN), `TipoCliente` (individual/company), `EstadoPagamento` (payment status), each with an int code + `toEnum(code)` lookup used when deserializing DTOs.

Auth/authorization is enforced in two places that both matter: `SecurityConfig` decides which endpoints require *any* authenticated user, and individual services additionally check *which* authenticated user is allowed (self vs ADMIN) — don't assume a passing `SecurityConfig` matcher is the full authorization story.

## Spring Boot 4 migration notes

- `config/SecurityConfig` uses `@EnableMethodSecurity` (Spring Security 7 renamed this from the legacy `@EnableGlobalMethodSecurity`); `DaoAuthenticationProvider` now takes `UserDetailsService` via constructor instead of a `setUserDetailsService` setter.
- Jackson 2 → 3 is NOT fully migrated: `spring.jackson.use-jackson2-defaults=true` (`application.properties`) plus the `spring-boot-jackson2` dependency (`pom.xml`) keep the existing Jackson 2 annotations (`@JsonIgnore`/`@JsonFormat`/`@JsonTypeInfo`/`@JsonTypeName` across `domain/`) and `config/JacksonConfig`'s `Jackson2ObjectMapperBuilder` subclass working unchanged. This bridge module is deprecated upstream and will eventually be removed — a follow-up task is to migrate `JacksonConfig` and the domain annotations to native Jackson 3 (`tools.jackson.*`) and drop the compat flag/dependency.
- `spring-boot-starter-web` was renamed to `spring-boot-starter-webmvc` in Boot 4.
