# framework 技术设计

本文维护本工程专属的技术边界；公开能力与保证见 [RESPONSIBILITIES.md](RESPONSIBILITIES.md)。通用工程约定以 AGENTS.md 路由的共享规范为准，不在此重复定义。

## 模块与依赖

- `framework-kernel` 是只依赖 JDK 的最内层 module，提供错误码基础、异常和 `Require`；其他生产 module 可按实际使用单向依赖 kernel，kernel 不依赖任何其他 framework module。
- 除对 `framework-kernel` 的单向依赖外，其他 `framework-*` 生产 module 之间不得相互依赖；每个 module 独立声明自身契约及所需三方依赖。
- 除 `framework-dependencies` 和 `framework-bom` 外，三方依赖统一使用 `provided`；框架只声明编译契约，不将三方实现传递给引用方。
- 每个 module 只声明实现当前契约所需的最小依赖，不通过聚合型 starter 获取基础类型。
- `framework-dependencies` 和 `framework-bom` 只管理版本，不承载运行时代码。

## kernel 与 core 能力包

- kernel 的 `error` 持有错误码结构、`BaseException`、通用 `FrameworkError` 与 `FrameworkException`，`validation` 持有 `Require`。kernel 不使用 Lombok、Spring 或其他三方依赖。
- core 包边界：根包仅保留 package-info。`request` 持有 Request、App/Client/认证会话请求及 SubjectType；`response` 持有 BaseResult、Result、PageResult；`pagination` 持有 Pagination、PageQueryRequest、PageQuery、PagedList；`http` 持有 RequestHeader、HeaderType、RestClientProperties 与 Web 入站错误。`naming`、`mapper`、`util` 保持各自职责。分页参数与内部查询条件放在同一能力包；PageResult 作为响应信封留在 response。

## 框架错误归属

- 六位错误码的前三位按错误契约的所有者分配。`FRAMEWORK`（`100–199`）用于框架定义的错误，`EDGE`（`200–299`）用于网关及业务服务的入站 adapter 自己定义的接入错误，`BIZ`（`300–899`）用于业务服务定义的领域、应用与基础设施错误。接入层返回框架或业务服务的错误时保留原码。
- `FrameworkError` 只保留 `INTERNAL_ERROR`、`ILLEGAL_ARGUMENT`、`ILLEGAL_STATE` 三个跨能力通用错误。
- 能够稳定识别和恢复的技术失败由能力所有者声明错误枚举，并统一通过 `FrameworkException` 抛出：Web 入口归 core，锁归 lock，消息归 message，Outbox 与事件存储归对应 starter，持久化能力归 persistence。
- 框架本地码按能力分段：通用 `000-009`、Web `020-029`、锁 `300-319`、消息 `400-419`、Outbox `500-519`、事件存储 `520-539`、持久化 `600-619`。新增错误只在真实调用场景存在时追加。
- 未分类的 JDK 或三方异常不在来源处无差别包装；统一边界记录后返回 `INTERNAL_ERROR`。框架技术异常在 Web 边界返回服务端错误和稳定枚举消息，不暴露调用点诊断文本。

`PagedList` 是技术中立的内部页数据，不属于业务领域模型或某个持久化技术，因此与 `PageQuery` 一起归 core.pagination；`PageResult` 是 API 响应信封，归 core.response。domain 与 core 之间不为此增加 module 依赖。

## 公开 Java 契约

- 公共契约以 JavaBean 风格 `getX()` 为规范访问器，同时提供紧凑风格 `x()` 委托；record 数据载体以紧凑访问器为规范并补充 `getX()` 委托。两类访问器语义一致、成对维护，不承载不同含义。
- 常量容器和纯静态工具类使用显式 final class 与 private 构造器，不用 Lombok 隐藏这一类简单结构。
- HTTP 请求头由 `RequestHeader` 枚举声明规范 headerName；消息头单独维护线上键，二者不因形式统一而改名。枚举方法值不能用作 Java 注解的字符串编译期常量。
- Header 名称比较键使用 Locale.ROOT 转小写，避免默认区域设置改变匹配结果。
- 资源名解析支持注入命名空间与不注入命名空间两种策略，framework 内部不引入业务场景 KeyResolver 层级。
- `Result<T>` 的 payload 为透明引用，框架不按运行时类型复制或转换，不声称其整体不可变；`PageResult`、`PagedList` 明确拥有列表容器，各自在构造边界完成不可修改浅复制。
- 请求绑定与 Header 回填需要分步赋值，请求载体保持可变 JavaBean；具备完整状态的分页条件使用 record。此处只记录选型原因，通用类型选择政策由共享规范维护。
- 新增能力 module 时必须同步维护 framework-bom；框架编译所需的第三方运行能力由使用方环境供应，消费者需显式组装，provided 不代表 JAR 自带依赖实现。

## 共享规范入口

- 集合、泛型信封、工具选择、校验和异常：develop-service 的 java-language、service-validation、validation 引用。
- Lombok、MapStruct、命名和单测：develop-service 的对应引用；本工程不维护另一份编码规则。
- 锁和消息可靠性：develop-distributed；运行适配代码属于 framework-starter。

泛型信封所有权边界的权威政策见 [共享 Java 规范](../../engineering-guidance-publisher/standards/bp_java.md#集合所有权与防御性复制)。
