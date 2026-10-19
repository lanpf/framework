# framework 技术设计

本文维护本工程专属的技术边界；公开能力与保证见 [RESPONSIBILITIES.md](RESPONSIBILITIES.md)。通用工程约定以 AGENTS.md 路由的共享规范为准，不在此重复定义。

## 模块与依赖

- `framework-*` 生产 module 之间不得相互依赖；每个 module 独立声明自身契约及所需三方依赖。
- 除 `framework-dependencies` 和 `framework-bom` 外，三方依赖统一使用 `provided`；框架只声明编译契约，不将三方实现传递给引用方。
- 每个 module 只声明实现当前契约所需的最小依赖，不通过聚合型 starter 获取基础类型。
- `framework-dependencies` 和 `framework-bom` 只管理版本，不承载运行时代码，也不构成 module 间依赖。

## core 能力包

- 包边界：根包仅保留 package-info。`request` 持有 Request、App/Client/认证会话请求及 SubjectType；`response` 持有 BaseResult、Result、PageResult；`pagination` 持有 Pagination、PageQueryRequest、PageQuery、PagedList；`http` 持有 RequestHeader、HeaderType、RestClientProperties。`error`、`validation`、`naming`、`mapper`、`util` 保持各自职责，Require 保留在 `core.validation`，不因调用频繁迁入根包。分页参数与内部查询条件放在同一能力包；PageResult 作为响应信封留在 response。

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
