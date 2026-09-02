# framework

本文只约束 `framework` 工程及其 `framework-*` module 的内部设计，按各 module 当前实际能力描述。

## 工程职责

`framework` 定义与具体运行技术无关的基础契约、领域抽象和通用能力，基于 Java 17 构建，不负责 Spring Boot 自动配置或具体中间件适配。服务通过 `framework-bom` 获取 module 版本约束，通过 `framework-dependencies` 获取完整依赖版本约束。

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

- 提供 `framework` 工程内除 `framework-dependencies` 外的全部基础 module 版本约束：core、domain、message、id、persistence、lock。

### `framework-core`

- 提供通用请求与响应、错误码与异常、分页请求、入口客户端上下文请求、请求头契约、校验断言、命名空间与资源命名、REST 客户端属性和 MapStruct 公共配置等基础能力。
- 请求契约：`Request` 是请求类型标记接口；`ClientRequest` 是所有入口客户端上下文请求的共同基类，承载 `clientAppId`（必填）、`clientPlatform`、`clientVersion`。渠道与认证会话不是互斥层级：分别由 `ChannelContext`、`AuthenticatedSessionContext` 表达可组合能力；框架提供只含渠道（`ChannelClientRequest`）、只含认证会话（`AuthenticatedSessionClientRequest`）和同时组合两者（`AuthenticatedSessionChannelClientRequest`）的具体请求类型。能力接口字段带 Bean Validation 约束，使用方只组合实际需要的上下文。
- 请求头契约：通用请求头名称由 `RequestHeader` 统一声明；`SubjectType` 表达受入口层写入的认证主体类型（`HOST_SESSION`、`BROWSER_SESSION`），区分宿主登录会话与受限浏览器会话。下游不得把缺失或未知的主体类型默认解释为宿主会话。
- 分页契约：分页 API 请求通过 `Pagination` 组合分页能力（默认页码 1、默认页大小 20、页大小上限 200）；只有分页字段时可直接使用 `PageQueryRequest`，同时具有 Client、Channel 或业务条件时由请求类型自行实现该接口，不以分页类作为共同父类。`PageQuery` 是接口层完成默认值解析后交给 application、repository 和 persistence 使用的不可变分页条件，提供 `offset()`、`zeroBasedPageNo()` 换算；非法页码或页大小直接拒绝，不做静默纠正。
- 响应契约：基础响应包装使用 `Result<T>`、`PageResult<T>`，共同契约由 `BaseResult` 表达（成功码 `0`、成功消息 `SUCCESS`）。失败响应不得携带数据；成功响应缺失的列表数据归一化为空列表。
- 错误与异常：完整错误码固定 6 位（3 位服务前缀 + 3 位本地码），由 `ErrorCodeRange` 定义取值范围与格式化，`ErrorCodeNamespace` 划分 FRAMEWORK、CORE、BIZ、EDGE、RESERVED 前缀段，`ErrorCodePrefix` 定位服务前缀，`BaseError` 组合前缀与本地码。框架内部错误使用 `FrameworkError` 枚举（中文消息模板）与 `FrameworkException`；服务异常共同基类为 `BaseException`。错误消息以 `命名空间:模板` 形式输出。
- 校验断言：`Require` 面向业务语义断言（非空、非空白、集合/Map 非空、正数），由调用方决定抛出的业务异常；Spring 解耦 module 的技术性前置条件使用 `org.apache.commons.lang3.Validate` 直接快速失败。
- 命名空间与资源命名：`Namespaced` 声明命名空间载体，`NamespaceResolver` 解析命名空间前缀，`ResourceNameResolver` 把业务键段解析为最终资源名（分隔符 `:`，跳过空白键段）；`DefaultResourceNameResolver` 只拼接键段，`NamespacedResourceNameResolver` 在键段前注入命名空间。
- 其它基础能力：`RestClientProperties` 提供连接/读取超时的配置默认值与约束；`MapStructConfig` 提供组件模型为 Spring、忽略未映射字段的公共 Mapper 配置。
- 完整构造后不再变化的数据载体优先使用 `record`；`Result`、`PageResult`、`PageQuery` 均为不可变值。Spring MVC 请求绑定、请求头回填和配置属性等需要分步写入的类型保留为普通类，不为追求形式统一而改为 `record`。

### `framework-domain`

- 提供领域 ID、聚合根、领域事件、领域事件存储端口、Repository 和应用/领域内部分页数据能力。
- `EntityId` 是按值判等的强类型 ID 基类，支持子类覆写校验钩子；`AggregateRoot` 表达聚合根身份；`Repository` 声明 `nextId`、`save`、`findById`、`existsById` 仓储契约。
- 原始 `DomainEvent` 只表达已经发生的业务事实，不携带持久化记录 ID，也不依赖 ID 生成端口；EventStore 在构造持久化 Envelope 时分配事件记录 ID。
- 领域事件的 `occurredAt` 使用 `Instant`，由事件创建者显式传入，`AbstractDomainEvent` 在构造时拒绝缺失，不在事件模型内部获取当前时间；事件类型默认取类名。
- `DomainEventStore` 是领域事件追加端口；`DomainEffect` 表达携带领域事件的处理效果。
- 应用和领域内部分页数据使用 `PagedList<T>`：null 数据归一化为空列表，构造时防御性拷贝。

### `framework-id`

- 提供通用 `IdGenerator<T>`、`LongIdGenerator` 发号抽象，按 generator name 取号，不绑定 JDBC、Zookeeper 或其他运行技术。
- 每个聚合根使用独立 generator name；领域事件 ID 适配同样使用独立 generator name。

### `framework-persistence`

- 提供与具体持久化技术无关的持久化配置和表命名等基础能力。
- `PersistenceProperties` 承载数据库类型（默认 `mysql`）与表命名 prefix、suffix（默认 `_d_o`）的配置默认值。
- `PersistenceTableNaming` 只做先剥离既有 suffix、再幂等补全 prefix 的命名处理，不承担具体技术的名称转换；技术差异由对应 starter 适配。

### `framework-lock`

- 提供与存储技术无关的锁上下文、锁执行器和锁提供者抽象。
- `LockContext` 是不可变锁上下文：构造时校验等待时间（非空、非负，缺省零等待）与锁键段（非空、无空白项），非法输入直接拒绝。
- `LockProvider` 把锁键段解析为 JDK `Lock`；`LockExecutor` 负责获取、执行和释放锁，支持有返回值与无返回值回调，未获取到锁时静默跳过；`DefaultLockExecutor` 为默认实现。具体存储技术只需将锁实现适配为 JDK `Lock`。

### `framework-message`

- 提供通用消息契约、统一消息 header、分区选择和消息能力抽象。
- 系统间集成事件使用 `IntegrationEvent`，必须携带 `eventId`、`eventType`、`occurredAt`、`aggregateType` 和 `aggregateId`；其中 `occurredAt` 使用 `Instant`，`eventType` 默认取类名。
- 发布端口使用 `IntegrationEventPublisher`（单条与批量发布），outbox 端口使用 `IntegrationEventOutbox`（批量追加）。
- 消息头常量以 `x-` 前缀组合形式声明：集成事件身份头（`x-event-id`、`x-event-type`、`x-aggregate-type`、`x-aggregate-id`）由 `IntegrationEventHeaders` 从事件构造，`occurredAt` 随 payload 传输；分区键约定为 `x-partition-key`，由 `MessageHeaders.resolvePartitionKey` 统一解析并在缺失时拒绝。
- 分区选择通过 `MessageQueueSelector` 表达，内置哈希取模（`HashMessageQueueSelector`）与一致性哈希（`ConsistentHashMessageQueueSelector`）实现；分区和延迟消息分别通过 `PartitionedOperations`、`DelayedOperations` 表达，不绑定 RabbitMQ、RocketMQ 或 Kafka。
- 消息转换默认名称由 `MessageConverterNames` 声明。

## 内部实现

- 公共契约保持技术中立，不出现具体中间件、数据库、Spring Boot 自动配置或服务领域类型。
- 接口按能力或端口命名，不使用 `I*`、`*Interface`；默认实现按策略或角色命名，不使用笼统的 `*Impl`。
- 常量容器和纯静态工具类使用显式的 `final class` 和 `private` 构造器；不使用 `lombok.experimental` 下的注解，也不用 Lombok 隐藏这一类简单的类结构。
- 公共契约以 JavaBean 风格 `getX()` 为规范访问器，同时提供紧凑风格 `x()` 委托；record 数据载体以紧凑访问器为规范并补充 `getX()` 委托。两类访问器语义一致、成对维护，不承载不同含义。
- HTTP 请求头常量由 `RequestHeader` 以大写中划线形式统一声明，消息头常量以 `x-` 小写前缀组合形式声明；两者分别是 HTTP 与消息协议的线上契约，已发布名称不因风格统一而调整。
- 新增 module 时必须同步加入 `framework-bom`；只有需要全局统一版本的三方依赖才加入 `framework-dependencies`。
