# framework 内部工程规约

本文只约束 `framework` 工程及其 `framework-*` module 的内部设计。

## 工程职责

`framework` 定义与具体运行技术无关的基础契约、领域抽象和通用能力，不负责 Spring Boot 自动配置或具体中间件适配。服务通过 `framework-bom` 获取 module 版本约束，通过 `framework-dependencies` 获取完整依赖版本约束。

## 模块与依赖

- `framework-*` 生产 module 之间不得相互依赖；每个 module 独立声明自身契约及所需三方依赖。
- 除 `framework-dependencies` 和 `framework-bom` 外，三方依赖统一使用 `provided`；框架只声明编译契约，不将三方实现传递给引用方。
- 每个 module 只声明实现当前契约所需的最小依赖，不通过聚合型 starter 获取基础类型。
- `framework-dependencies` 和 `framework-bom` 只管理版本，不承载运行时代码，也不构成 module 间依赖。

## 模块职责

### `framework-dependencies`

- 提供所有工程统一导入的 dependency management，聚合三方依赖和 `framework-bom` 的版本约束。
- 只在此集中声明三方依赖版本，功能 module 不自行覆盖。

### `framework-bom`

- 提供 `framework` 工程内除 `framework-dependencies` 外的基础 module 版本约束。

### `framework-core`

- 提供通用请求与响应、错误码与异常、分页请求、入口客户端上下文请求、请求头契约、校验断言、命名空间与资源命名、REST 客户端属性和 MapStruct 公共配置等基础能力。
- 基础响应包装使用 `Result<T>`、`PageResult<T>`。分页 API 请求通过 `Pagination` 组合分页能力；只有分页字段时可直接使用 `PageQueryRequest`，同时具有 Client、Channel 或业务条件时由请求类型自行实现该接口，不以分页类作为共同父类。
- `PageQuery` 是接口层完成默认值解析后交给 application、repository 和 persistence 使用的不可变分页条件；非法页码或页大小直接拒绝，不做静默纠正。
- 完整构造后不再变化的数据载体优先使用 `record`；`Result`、`PageResult`、`PageQuery` 均为不可变值。Spring MVC 请求绑定、请求头回填和配置属性等需要分步写入的类型保留为普通类，不为追求形式统一而改为 `record`。
- `ClientRequest` 是所有入口客户端上下文请求的共同基类。渠道与认证会话不是互斥层级：分别由 `ChannelContext`、`AuthenticatedSessionContext` 表达可组合能力；框架提供只含渠道、只含认证会话和同时组合两者的具体请求类型。能力接口字段带 Bean Validation 约束，使用方只组合实际需要的上下文。
- 通用请求头名称由 `RequestHeader` 统一声明；`SubjectType` 表达受入口层写入的认证主体类型，区分宿主登录会话与受限浏览器会话。下游不得把缺失或未知的主体类型默认解释为宿主会话。
- Spring 解耦 module 的技术性前置条件使用 `org.apache.commons.lang3.Validate` 直接快速失败；面向业务语义的断言使用 `Require`，由调用方决定抛出的业务异常。

### `framework-domain`

- 提供领域 ID、领域事件、领域事件存储端口、Repository 和应用/领域内部分页数据能力。
- 原始 `DomainEvent` 只表达已经发生的业务事实，不携带持久化记录 ID，也不依赖 ID 生成端口；EventStore 在构造持久化 Envelope 时分配事件记录 ID。
- 领域事件的 `occurredAt` 使用 `Instant`，由事件创建者显式传入，不在事件模型内部获取当前时间。
- 应用和领域内部分页数据使用 `PagedList<T>`。

### `framework-id`

- 提供通用 `LongIdGenerator` 发号抽象，不绑定 JDBC、Zookeeper 或其他运行技术。
- 每个聚合根使用独立 generator name；领域事件 ID 适配同样使用独立 generator name。

### `framework-persistence`

- 提供与具体持久化技术无关的持久化配置和表命名等基础能力。
- `PersistenceProperties` 承载数据库类型与表命名 prefix、suffix 的配置默认值。
- `PersistenceTableNaming` 只处理 prefix 和 suffix，不承担具体技术的名称转换；技术差异由对应 starter 适配。

### `framework-lock`

- 提供与存储技术无关的锁上下文、锁执行器和锁提供者抽象。
- `LockContext` 在构造时校验等待时间与锁键段，非法输入直接拒绝；`LockExecutor` 负责获取、执行和释放锁；具体存储技术只需将锁实现适配为 JDK `Lock`。

### `framework-message`

- 提供通用消息契约、统一消息 header、消息转换和消息能力抽象。
- 系统间集成事件使用 `IntegrationEvent`，发布端口使用 `IntegrationEventPublisher`，outbox 端口使用 `IntegrationEventOutbox`。
- 集成事件必须携带 `eventId`、`eventType`、`occurredAt`、`aggregateType` 和 `aggregateId`；其中 `occurredAt` 使用 `Instant`。
- 分区和延迟消息分别通过 `PartitionedOperations`、`DelayedOperations` 表达，不绑定 RabbitMQ、RocketMQ 或 Kafka。

## 内部实现

- 公共契约保持技术中立，不出现具体中间件、数据库、Spring Boot 自动配置或服务领域类型。
- 接口按能力或端口命名，不使用 `I*`、`*Interface`；默认实现按策略或角色命名，不使用笼统的 `*Impl`。
- 常量容器和纯静态工具类使用显式的 `final class` 和 `private` 构造器；不使用 `lombok.experimental` 下的注解，也不用 Lombok 隐藏这一类简单的类结构。
- 公共契约以 JavaBean 风格 `getX()` 为规范访问器，同时提供紧凑风格 `x()` 委托；record 数据载体以紧凑访问器为规范并补充 `getX()` 委托。两类访问器语义一致、成对维护，不承载不同含义。
- HTTP 请求头常量由 `RequestHeader` 以大写中划线形式统一声明，消息头常量以 `x-` 小写前缀组合形式声明；两者分别是 HTTP 与消息协议的线上契约，已发布名称不因风格统一而调整。
- 新增 module 时必须同步加入 `framework-bom`；只有需要全局统一版本的三方依赖才加入 `framework-dependencies`。
