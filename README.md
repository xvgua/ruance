# 老年人健康管理助手

基于 JavaFX 的桌面应用程序，为老年人提供健康指标评估与日常健康管理功能。

## 项目结构

```
health-assistant/
├── pom.xml                          # Maven 构建配置
├── .gitignore
└── src/
    ├── main/java/com/healthassistant/
    │   ├── MainApp.java             # 程序入口
    │   ├── logic/                   # 业务逻辑层
    │   │   ├── BpEvaluator.java     # 血压评估
    │   │   ├── BgEvaluator.java     # 血糖评估
    │   │   ├── SleepEvaluator.java  # 睡眠评估
    │   │   ├── ExerciseAdvisor.java # 运动建议
    │   │   ├── Reminder.java           # 提醒实体模型
    │   │   ├── ReminderManager.java    # 用药提醒管理
    │   │   └── AIAdvisorService.java   # AI 健康问答服务
    │   └── ui/                      # 界面层
    │       ├── MainWindow.java      # 主窗口（导航框架）
    │       ├── BpPage.java          # 血压评估页
    │       ├── BgPage.java          # 血糖评估页
    │       ├── SleepPage.java       # 睡眠评估页
    │       ├── ExercisePage.java    # 运动建议页
    │       ├── ReminderPage.java    # 用药提醒页
    │       ├── AIPage.java          # AI 健康助手页
    │       └── Styles.java          # 全局样式常量
    └── test/java/com/healthassistant/logic/
        ├── BpEvaluatorTest.java     # 血压评估单元测试
        ├── BgEvaluatorTest.java     # 血糖评估单元测试
        ├── SleepEvaluatorTest.java  # 睡眠评估单元测试
        ├── ExerciseAdvisorTest.java # 运动建议单元测试
        ├── ReminderManagerTest.java # 提醒管理单元测试
        └── AIAdvisorServiceTest.java # AI 健康问答单元测试
```

## 功能模块

### 1. 血压评估 (BpEvaluator)

输入收缩压和舒张压，自动判断血压等级（正常 / 偏高 / 高血压）并给出健康建议。

- 分类标准：正常（<120/<80）、偏高（<140/<90）、高血压（>=140/>=90）
- 输入校验：拒绝负值、零值和超出合理范围的值

### 2. 血糖评估 (BgEvaluator)

根据测量时段（空腹 / 餐后）和血糖值，判断血糖等级（低血糖 / 正常 / 偏高 / 高血糖）并给出建议。

- 空腹正常范围：3.9 - 6.1 mmol/L
- 餐后正常范围：3.9 - 7.8 mmol/L
- 自动根据时段切换判断标准

### 3. 睡眠评估 (SleepEvaluator)

输入入睡时间、起床时间和睡眠质量自评（1-5分），计算睡眠时长并评估质量（不足 / 一般 / 充足）。

- 自动处理跨午夜入睡场景
- 结合时长与自评综合判断

### 4. 运动建议 (ExerciseAdvisor)

根据年龄和运动频率（几乎不 / 偶尔 / 经常），生成个性化的运动计划。

- 建议内容包括：运动类型、时长、频率、注意事项
- 70 岁以上老年人自动调整为低强度方案

### 5. 用药提醒 (ReminderManager)

管理药品提醒事项，支持添加、删除、查询和实时检测。

- 添加提醒：药品名称、剂量、时间
- 自动检测当前时间是否有需要服用的药品
- 数据校验：名称不超过50字、剂量不超过30字

### 6. AI 健康助手 (AIAdvisorService)

提供基于知识库的智能健康问答，用户输入健康相关问题，系统自动匹配最相关的答案。

- 知识库涵盖 6 大类别：血压、血糖、睡眠、运动、用药、饮食
- 关键词提取：自动清洗标点符号，过滤短词（<2字），提取关键词
- 智能匹配：精确匹配权重 5 分，部分匹配根据长度加权计分
- 无匹配兜底：无法识别的问题统一返回友好提示
- 界面上提供快捷提问按钮（"试试这样问我"）

## 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Java | 17+ | 编程语言 |
| JavaFX | 17.0.6 | GUI 框架 |
| Maven | 3.9+ | 项目构建 |
| JUnit 5 | 5.10.1 | 单元测试 |

## 快速开始

### 环境要求

- JDK 17 或更高版本
- Maven 3.9+

### 运行

```bash
mvn javafx:run
```

### 测试

```bash
mvn test
```
