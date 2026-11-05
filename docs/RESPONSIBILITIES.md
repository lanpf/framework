# framework

本文定义 `framework` 对使用方交付的职责、公开契约及保证与限制。项目专属技术设计见 [DESIGN.md](DESIGN.md)。

## 工程职责

`framework` 定义与具体运行技术无关的基础契约、领域抽象和通用能力，基于 Java 17 构建。服务通过 `framework-bom` 获取 module 版本约束，通过 `framework-dependencies` 获取完整依赖版本约束。

## 不负责

- Spring Boot 自动配置与具体中间件适配：由 `framework-starter` 工程负责。
- 业务领域规则、服务领域模型：由各业务服务工程负责。
- 持久化技术实现、消息 broker 实现：由 `framework-starter` 对应 starter 与服务的技术适配 module 负责。

## 协作契约

- 本工程与 `framework-starter` 的契约：`framework-*` 契约由本工程定义并保持稳定，`framework-starter` 依赖这些契约完成技术适配；契约需要变更时两侧同步更新，对方负责的适配契约见 [framework-starter 职责](../../framework-starter/docs/RESPONSIBILITIES.md)，不复制其内容。

## 公开能力与保证

### `framework-dependencies`

- 提供所有工程统一导入的 dependency management，聚合三方依赖和 `framework-bom` 的版本约束。
- 只在此集中声明三方依赖版本，功能 module 不自行覆盖。

### `framework-bom`

- 提供 `framework` 工程内除 `framework-dependencies` 外的全部基础 module 版本约束：core、domain、message、id、persistence、lock。

### `framework-core`

- 提供通用请求与响应、错误码与异常、分页请求、入口客户端上下文请求、请求头契约、校验断言、命名空间与资源命名、REST 客户端属性和 MapStruct 公共配置等基础能力。
- 请求契约：`Request` 是请求类型标记接口。`request.AppRequest` 承载 `appId`（必填）、`appPlatform`、`appVersion`，分别消费 `X-App-Id`、`X-App-Platform`、`X-App-Version`。`ClientContext` 独立声明必填 `clientId`，消费 `X-Client-Id`；`AuthenticatedSessionContext` 声明必填 `userId`、`sessionId`。框架提供 `ClientAppRequest`、`AuthenticatedSessionAppRequest`、`AuthenticatedSessionClientAppRequest`，按需组合 Client 和认证会话，不要求普通 App 请求必须有 Client。旧 ClientRequest/ChannelContext 命名直接迁移，不保留 Header 别名。
- 上下文绑定：Web starter 在请求体反序列化后、`@Valid`/`@Validated` 校验前，用受保护 Header 覆盖上述字段；缺失 Header 也覆盖为 null，不保留 body 声明。App/Client/会话身份由 gateway 验证，框架只消费已建立的 Header 契约，不依赖 gateway 的领域对象。业务目标与入口上下文需使用不同属性，如 `targetAppId` 与 `appId`。
- 请求头契约：通用请求头名称由 `RequestHeader` 统一声明；`SubjectType` 表达受入口层写入的认证主体类型（`HOST_SESSION`、`BROWSER_SESSION`），区分宿主登录会话与受限浏览器会话。下游不得把缺失或未知的主体类型默认解释为宿主会话。
- `http.RequestHeader` 是通用 HTTP 请求头契约枚举，当前 App/Client/Subject/Session 与签名字段由它统一声明，元数据为 headerName 与 HeaderType。线上字符串使用 `headerName()`；`normalizeName(name)` 验证 HTTP token 后生成小写比较键，不修剪空白，非法名称抛 FrameworkException；`fromName(name)` 按该规则查找，null 或合法未知名称返回空；`fromType(type)` 返回对应分类的不可变集合。可信取值、清除和转发规则由 [gateway](../../gateway/docs/RESPONSIBILITIES.md) 负责，不下沉到框架；消费方需独立迁移和重新编译。
- 分页契约：分页 API 请求通过 `Pagination` 组合分页能力（默认页码 1、默认页大小 20、页大小上限 200）；只有分页字段时可直接使用 `PageQueryRequest`，同时具有 Client、Channel 或业务条件时由请求类型自行实现该接口，不以分页类作为共同父类。`PageQuery` 是接口层完成默认值解析后交给 application、repository 和 persistence 使用的不可变分页条件，提供 `offset()`、`zeroBasedPageNo()` 换算；非法页码或页大小直接拒绝，不做静默纠正。
- 内部分页数据：core 提供 `PagedList<T>`，适用于领域、application 和持久化端口的页数据交换，不携带 API 响应码。null 列表归一为空；列表结构与输入隔离且不可修改，元素引用共享，null 元素使用框架技术错误拒绝。
- 响应契约：API 响应信封使用 `Result<T>`、`PageResult<T>`，共同契约由 `BaseResult` 表达（成功码 `0`、成功消息 `SUCCESS`）。失败响应不得携带数据。`Result<T>` 保留原始 payload 引用，不接管其状态所有权，不承诺快照或深层不可变；使用方按 payload 自身契约负责所需的隔离及发布期间稳定性。`PageResult<T>` 成功时缺失的列表归一为空，隔离原列表的结构变更，元素引用仍共享；失败时数据为 null。
- 错误与异常：完整错误码固定 6 位（3 位服务前缀 + 3 位本地码），由 `ErrorCodeRange` 定义取值范围与格式化，`ErrorCodeNamespace` 划分 FRAMEWORK、CORE、BIZ、EDGE、RESERVED 前缀段，`ErrorCodePrefix` 定位服务前缀，`BaseError` 组合前缀与本地码。框架内部错误使用 `FrameworkError` 枚举（英文技术诊断默认消息）与 `FrameworkException`；服务异常共同基类为 `BaseException`。默认错误消息以 `命名空间:消息` 形式输出；显式技术诊断消息保留调用方提供的内容，不改变错误码。
- 校验断言：`Require` 提供非空、非空白、集合/Map 非空、元素完整性及正数检查；默认使用框架技术错误，异常供应器可表达调用方的业务拒绝。`noNulls` 拒绝 null 容器，允许空容器，并检查集合元素及 Map 的 key/value；供应器重载对容器和元素均使用所提供的异常。`NullSafe` 仅在 null 与空容器等价的契约下提供遍历和不可修改浅复制，有序变体保留输入迭代顺序。
- 命名空间与资源命名：`Namespaced` 声明命名空间载体，`NamespaceResolver` 解析命名空间前缀，`ResourceNameResolver` 把业务键段解析为最终资源名（分隔符 `:`，跳过空白键段）；`DefaultResourceNameResolver` 只拼接键段，`NamespacedResourceNameResolver` 在键段前注入命名空间。
- 其它基础能力：`RestClientProperties` 提供连接/读取超时的配置默认值与约束；`MapStructConfig` 提供组件模型为 Spring、未映射目标字段按 ERROR 校验的公共 Mapper 配置。

### `framework-domain`

- 提供领域 ID、聚合根、领域事件、领域事件存储端口和 Repository 契约。
- `EntityId` 是按值判等的强类型 ID 基类，支持子类规范化 ID 值；`AggregateRoot` 表达聚合根身份；`Repository` 声明 `nextId`、`save`、`findById`、`existsById` 仓储契约。
- 原始 `DomainEvent` 只表达已经发生的业务事实，不携带持久化记录 ID，也不依赖 ID 生成端口；EventStore 在构造持久化 Envelope 时分配事件记录 ID。
- 领域事件的 `occurredAt` 使用 `Instant`，由事件创建者显式传入，`AbstractDomainEvent` 在构造时拒绝缺失，不在事件模型内部获取当前时间；事件类型默认取类名。
- `DomainEventStore` 是领域事件追加端口；`DomainEffect` 表达携带领域事件的处理效果。

### `framework-id`

- 提供通用 `IdGenerator<T>`、`LongIdGenerator` 发号抽象，按 generator name 取号，不绑定 JDBC、Zookeeper 或其他运行技术。
- 每个聚合根使用独立 generator name；领域事件 ID 适配同样使用独立 generator name。

### `framework-persistence`

- 提供与具体持久化技术无关的持久化配置和表命名等基础能力。
- `PersistenceProperties` 承载数据库类型（默认 `mysql`）与表命名 prefix、suffix（默认 `_d_o`）的配置默认值。
- `PersistenceTableNaming` 只做先剥离既有 suffix、再幂等补全 prefix 的命名处理，不承担具体技术的名称转换；技术差异由对应 starter 适配。

### `framework-lock`

- 提供与存储技术无关的锁上下文、锁执行器和锁提供者抽象。
- `LockContext` 提供锁上下文：构造时校验等待时间（非空、非负，缺省零等待）与锁键段（非空、无空白项），非法输入直接拒绝。
- `LockProvider` 把锁键段解析为 JDK `Lock`；`LockExecutor` 负责获取、执行和释放锁，支持有返回值与无返回值回调，未获取到锁时通过空 Optional 或 false 表示未执行；`DefaultLockExecutor` 为默认实现。具体存储技术只需将锁实现适配为 JDK `Lock`。

### `framework-message`

- 提供通用消息契约、统一消息 header、分区选择和消息能力抽象。
- 系统间集成事件使用 `IntegrationEvent`，必须携带 `eventId`、`eventType`、`occurredAt`、`aggregateType` 和 `aggregateId`；其中 `occurredAt` 使用 `Instant`，`eventType` 默认取类名。
- 发布端口使用 `IntegrationEventPublisher`（单条与批量发布），outbox 端口使用 `IntegrationEventOutbox`（批量追加）。
- 消息头常量以 `x-` 前缀组合形式声明：集成事件身份头（`x-event-id`、`x-event-type`、`x-aggregate-type`、`x-aggregate-id`）由 `IntegrationEventHeaders` 从事件构造，`occurredAt` 随 payload 传输；分区键约定为 `x-partition-key`，头名称由 `MessageHeaders.PARTITION_KEY` 声明，分区实参由 `PartitionKeyResolver.resolve` 统一解析并在缺失时拒绝。
- 分区选择通过 `MessageQueueSelector` 表达，内置哈希取模（`HashMessageQueueSelector`）与一致性哈希（`ConsistentHashMessageQueueSelector`）实现；分区和延迟消息分别通过 `PartitionedOperations`、`DelayedOperations` 表达，不绑定 RabbitMQ、RocketMQ 或 Kafka。
- 消息转换默认名称由 `MessageConverterNames` 声明。

## 保证边界与当前交付

- 六个生产能力 module 交付契约与局部技术实现，BOM 与 dependencies 仅管理版本；不包含可启动服务。
- 不提供真实数据库、消息 broker、分布式锁存储或发号实现，运行环境及相应实现由 starter 和使用方组装。
- 锁执行器只围绕回调获取和释放锁，不检测外围数据库事务是否已完成，也不替代数据一致性保护。
- 消息端口不独立保证数据库原子性、消费幂等、ACK、重试或补偿恢复；这些保证属于具体适配实现及使用方。
- 各 module 的开发验证是单元和模块契约测试，不据此宣称真实基础设施或消费方迁移已验证。
