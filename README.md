# 个人博客系统

一个包含前台展示、后台管理、AI 聊天机器人的全栈个人博客系统。

## 项目结构

```
web-blog/
├── admin/              # 博客后台管理系统（Vue 3 + Vite + Element Plus）
├── demo/               # 后端 API 服务（Spring Boot 3）
└── my-blig-web/        # 前台展示网站（Next.js 16 + React 19 + Tailwind CSS）
```

## 技术栈

| 模块 | 技术 |
|------|------|
| 前台 | Next.js 16、React 19、TypeScript、Tailwind CSS |
| 后台 | Vue 3、Vite、Element Plus、Pinia、Vue Router |
| 后端 | Spring Boot 3、Spring Security、Spring Data JPA、JWT、H2/PostgreSQL |
| AI | DeepSeek API（煤球猫咪聊天机器人） |

## 环境要求

- **JDK 25**（后端编译需要，已在 `demo/build.gradle` 中指定）
- **Node.js 18+**
- **npm** 或 **pnpm**
- （可选）**PostgreSQL**（生产环境）

## 快速启动

### 1. 启动后端服务（demo）

```powershell
cd demo

# 方式一：使用 Gradle Wrapper 启动
.\gradlew.bat bootRun

# 方式二：先构建再运行
.\gradlew.bat build
java -jar build\libs\demo-0.0.1-SNAPSHOT.jar
```

后端默认运行在 `http://localhost:8080`。

### 2. 启动前台网站（my-blig-web）

```powershell
cd my-blig-web
npm install
npm run dev
```

前台默认运行在 `http://localhost:3000`。

### 3. 启动后台管理系统（admin）

```powershell
cd admin
npm install
npm run dev
```

后台默认运行在 `http://localhost:5173`。

## 安全配置（重要）

本项目已将敏感配置文件加入 `.gitignore`，**请勿将真实密钥提交到 GitHub**。部署前请完成以下配置。

### 1. 复制配置文件模板

```powershell
cd demo\src\main\resources
copy application.properties.example application.properties
```

### 2. 修改 JWT 密钥

编辑 `demo/src/main/resources/application.properties`：

```properties
# 请替换为一个长度至少 256 位（32 字节）的随机字符串
jwt.secret=YOUR_RANDOM_SECRET_KEY_HERE_MUST_BE_AT_LEAST_32_BYTES_LONG
```

> 注意：`JwtTokenProvider` 已移除 fallback 默认值。如果未配置 `jwt.secret`，应用启动时会直接报错。

### 3. 配置 DeepSeek API Key（如需使用 AI 聊天机器人）

```properties
llm.deepseek.url=https://api.deepseek.com/v1/chat/completions
llm.deepseek.key=sk-你的真实DeepSeekAPIKey
llm.deepseek.model=deepseek-chat
```

### 4. 配置初始管理员和简历密码（推荐）

为了避免硬编码默认密码，系统支持通过环境变量设置初始密码：

```powershell
# Windows PowerShell
$env:ADMIN_DEFAULT_PASSWORD="你的强密码"
$env:RESUME_DEFAULT_PASSWORD="你的简历访问密码"
.\gradlew.bat bootRun
```

如果不设置，系统**第一次启动时会随机生成密码**，并打印在控制台中，请妥善保存。

### 5. 生产环境数据库配置

生产环境请使用 PostgreSQL，创建 `application-prod.properties`：

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/blogdb
spring.datasource.username=postgres
spring.datasource.password=你的数据库密码
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.show-sql=false
spring.h2.console.enabled=false
```

运行时激活生产配置：

```powershell
$env:SPRING_PROFILES_ACTIVE="prod"
java -jar build\libs\demo-0.0.1-SNAPSHOT.jar
```

### 6. 前台 API 地址配置

前台默认连接 `http://localhost:8080`。如需修改，编辑 `my-blig-web/.env.local`：

```env
NEXT_PUBLIC_API_URL=http://localhost:8080
```

> `.env.local` 已加入 `.gitignore`，不会被提交。

## 默认访问地址

| 服务 | 地址 |
|------|------|
| 前台网站 | http://localhost:3000 |
| 后台管理 | http://localhost:5173 |
| 后端 API | http://localhost:8080 |
| H2 控制台 | http://localhost:8080/h2-console |

## 构建部署

### 构建前端

```powershell
cd admin
npm run build

cd ../my-blig-web
npm run build
```

### 构建后端

```powershell
cd demo
.\gradlew.bat bootJar
```

构建产物位于 `demo/build/libs/demo-0.0.1-SNAPSHOT.jar`。

## 安全注意事项

1. **永远不要提交真实密钥**：`application.properties`、`application-prod.properties`、`.env.local` 已加入 `.gitignore`。
2. **JWT 密钥必须足够长**：建议生成 64 位随机字符串。
3. **生产环境务必修改初始密码**：通过环境变量 `ADMIN_DEFAULT_PASSWORD` 和 `RESUME_DEFAULT_PASSWORD` 设置。
4. **数据库密码不要使用简单值**：生产环境请使用强密码。
5. **关闭 H2 控制台**：生产环境 `spring.h2.console.enabled=false`。

## 常见问题

### Q: 启动后端时报错 "jwt.secret" 不存在？

A: 这是预期行为。请按照上文【安全配置】第 2 步配置 JWT 密钥。

### Q: 忘记管理员密码怎么办？

A: 目前需要直接操作数据库重置 `users` 表中 `admin` 用户的 `password` 字段（BCrypt 加密后的值）。

### Q: 如何更新简历访问密码？

A: 登录后台管理系统，在设置页面中修改简历密码。
