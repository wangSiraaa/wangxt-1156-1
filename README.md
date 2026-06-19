# 风电场叶片缺陷复核系统

## 项目简介

风电场叶片缺陷复核管理系统，实现无人机巡检员、检修班、场站负责人三个角色的业务协同。

## 核心业务流程

1. **无人机巡检员** 上传叶片缺陷照片和信息
2. **检修班** 评估缺陷，确定是否需要复拍和检修窗口
3. **场站负责人** 确认复核结论，决定是否停机检修

## 业务规则

- 疑似裂纹类缺陷自动触发二次复拍流程
- 风速超过阈值（默认12m/s）不允许安排登塔作业
- 已确认停机的机组不能派发发电计划，相关发电计划自动取消
- 缺陷、复拍、检修窗口、停机状态全部持久化

## 角色说明

| 角色 | 职责 |
|------|------|
| 无人机巡检员 | 上传缺陷、执行复拍任务 |
| 检修班 | 缺陷评估、创建检修窗口、执行检修 |
| 场站负责人 | 缺陷复核、确认停机、审批检修窗口 |

## 技术栈

### 后端
- Java 17
- Spring Boot 3.2.x
- Spring Data JPA
- H2 内存数据库（开发）/ PostgreSQL（生产）
- Swagger / OpenAPI 3.0
- Lombok

### 前端
- 原生 HTML / CSS / JavaScript
- 响应式设计

## 项目结构

```
.
├── backend/                 # 后端项目
│   ├── src/main/java/com/windfarm/defect/
│   │   ├── WindDefectReviewApplication.java
│   │   ├── controller/      # REST API 控制器
│   │   ├── service/         # 业务逻辑层
│   │   ├── repository/      # 数据访问层
│   │   ├── entity/          # 数据实体
│   │   ├── dto/             # 数据传输对象
│   │   ├── enums/           # 枚举类型
│   │   ├── config/          # 配置类
│   │   └── exception/       # 异常处理
│   └── src/main/resources/
│       ├── application.yml  # 应用配置
│       └── db/              # 数据库脚本
│
└── frontend/                # 前端项目
    ├── index.html           # 主页面
    ├── styles/              # 样式文件
    ├── utils/               # 工具函数
    └── pages/               # 页面脚本
```

## 数据模型

### 核心实体

1. **WindTurbine** - 风电机组
2. **DefectRecord** - 缺陷记录
3. **ReshootRecord** - 复拍记录
4. **MaintenanceWindow** - 检修窗口
5. **OutageRecord** - 停机记录
6. **GenerationPlan** - 发电计划

### 缺陷状态流转

```
PENDING_EVALUATION (待评估)
    → PENDING_RESHOOT (待复拍) → PENDING_EVALUATION (复拍后重新评估)
    → PENDING_MAINTENANCE (待检修)
        → CONFIRMED (已确认)
        → CLOSED (已关闭)
        → FALSE_ALARM (误报)
```

## 快速开始

### 后端启动

```bash
cd backend
mvn spring-boot:run
```

后端服务启动在 `http://localhost:19456/api`

### API 文档

启动后访问 Swagger UI:
- http://localhost:19456/api/swagger-ui.html

H2 数据库控制台:
- http://localhost:19456/api/h2-console
- JDBC URL: `jdbc:h2:mem:winddefect`
- 用户名: `sa`
- 密码: (空)

### 前端访问

直接在浏览器打开 `frontend/index.html`，或使用任意静态文件服务器。

推荐使用 live-server:
```bash
npm install -g live-server
cd frontend
live-server --port=20456
```

## 主要 API 接口

### 缺陷管理
- `POST /api/defects` - 上传缺陷
- `GET /api/defects` - 查询缺陷列表
- `GET /api/defects/{id}` - 查询缺陷详情
- `PUT /api/defects/{id}/evaluate` - 评估缺陷
- `PUT /api/defects/{id}/review` - 复核缺陷

### 复拍管理
- `POST /api/reshoots` - 创建复拍任务
- `GET /api/reshoots` - 查询复拍列表
- `PUT /api/reshoots/{id}/complete` - 完成复拍

### 检修窗口
- `POST /api/maintenance-windows` - 创建检修窗口
- `GET /api/maintenance-windows` - 查询检修窗口列表
- `PUT /api/maintenance-windows/{id}/confirm` - 确认检修窗口
- `PUT /api/maintenance-windows/{id}/start` - 开始检修
- `PUT /api/maintenance-windows/{id}/complete` - 完成检修
- `PUT /api/maintenance-windows/{id}/cancel` - 取消检修窗口

### 停机管理
- `POST /api/outages` - 创建停机记录
- `GET /api/outages` - 查询停机记录列表
- `PUT /api/outages/{id}/end` - 结束停机

### 发电计划
- `POST /api/generation-plans` - 创建发电计划
- `GET /api/generation-plans` - 查询发电计划
- `PUT /api/generation-plans/{id}/cancel` - 取消发电计划

### 机组管理
- `GET /api/turbines` - 查询机组列表
- `POST /api/turbines` - 创建机组
- `GET /api/turbines/{id}` - 查询机组详情

## 配置说明

### 风速阈值配置

在 `application.yml` 中配置:

```yaml
windfarm:
  defect:
    wind-speed-threshold: 12.0  # 登塔风速阈值，单位m/s
    max-reshoot-count: 2         # 最大复拍次数
```

### 数据库切换到 PostgreSQL

修改 `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/winddefect
    driver-class-name: org.postgresql.Driver
    username: postgres
    password: password
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
```

## 测试数据

系统启动时会自动加载测试数据，包括：
- 5 台风机
- 4 条缺陷记录
- 2 条复拍记录
- 2 个检修窗口
- 1 条停机记录
- 4 条发电计划

## 业务场景演示

### 场景一：疑似裂纹缺陷的完整复核流程

1. 无人机巡检员上传一条疑似裂纹缺陷
2. 系统自动将缺陷置为"待复拍"状态
3. 检修班创建复拍任务
4. 无人机执行复拍，上传复拍结果
5. 复拍完成后缺陷回到"待评估"状态
6. 检修班评估缺陷，确认需要检修
7. 检修班创建检修窗口
8. 场站负责人复核缺陷，确认需停机检修
9. 系统自动创建停机记录，取消相关发电计划
10. 检修班组执行检修
11. 检修完成，机组恢复备用状态

### 场景二：风速超限校验

- 创建检修窗口时，如果预计风速超过阈值，不允许创建
- 开始登塔作业时，如果实际风速超过阈值，不允许开始
- 复拍执行时，如果实际风速超过阈值，不允许执行

### 场景三：停机机组的发电计划拦截

- 已停机的机组不允许创建新的发电计划
- 机组停机时，该机组已发布的发电计划自动取消
