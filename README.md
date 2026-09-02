# framework

`framework` 定义与具体运行技术无关的基础契约、领域抽象和通用能力，为业务服务和 `framework-starter` 提供稳定的技术中立契约层。能力交付状态见 [docs/RESPONSIBILITIES.md](docs/RESPONSIBILITIES.md)。

## 构建与验证

- JDK 17，Maven 多 module 工程。
- 构建并安装全部 module：`mvn install`
- 只运行单元测试：`mvn test`
- 每个 module 自带单元测试，随常规 `verify` 执行；本工程没有独立的集成测试 module。

## 模块地图

| module | 职责 |
| --- | --- |
| `framework-dependencies` | 统一导入的 dependency management，集中声明三方依赖版本 |
| `framework-bom` | 工程内基础 module 的版本约束，不承载运行时代码 |
| `framework-core` | 请求/响应契约、错误码与异常、分页、请求头、校验断言、命名空间与资源命名等基础能力 |
| `framework-domain` | 领域 ID、聚合根、领域事件、事件存储端口、Repository 契约、`PagedList` |
| `framework-id` | `IdGenerator`/`LongIdGenerator` 发号抽象，按 generator name 取号 |
| `framework-message` | 集成事件契约、统一消息 header、分区选择、发布与 outbox 端口 |
| `framework-persistence` | 技术中立的持久化配置与表命名 prefix/suffix 处理 |
| `framework-lock` | 锁上下文、锁执行器、锁提供者抽象（`LockContext`/`LockExecutor`/`LockProvider`） |

各 module 的详细契约、约束与内部实现规则见 [docs/RESPONSIBILITIES.md](docs/RESPONSIBILITIES.md)。

## 文档路由

- 工程标准与任务路由：`AGENTS.md`（由 engineering-guidance-publisher 托管）。
- 工程职责、模块职责、内部实现规则：[docs/RESPONSIBILITIES.md](docs/RESPONSIBILITIES.md)。
- 纯类库工程，无 `docs/DOMAIN.md`；引入真实领域能力时再补。
