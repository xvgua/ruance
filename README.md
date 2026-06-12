# Harness Engineering 项目模板

这是一个用于快速构建 Harness Engineering（驾驭工程）项目的模板，专门针对系统级脚本开发。它提供了完整的约束架构、前馈引导和反馈感应组件。

## 什么是 Harness Engineering？

Harness Engineering（驾驭工程）是一门设计基础设施、机械约束与自动反馈回路的学科。它的目标是通过构建精密的软件框架（Harness），将 AI 模型不可预测的随机性转化为工程级的高度可靠。

**核心公式：智能体 = 模型 + 约束架构（Harness）**

## 特性

- ✅ 完整的前馈引导系统（Feedforward）
- ✅ 强大的反馈感应机制（Feedback）
- ✅ 多种语言的 Linter 配置
- ✅ 模块化的项目结构
- ✅ 可扩展的约束架构
- ✅ 系统级脚本开发支持
- ✅ 与 Trae IDE 集成

## 支持的技术栈

- **Python**（最优先）
- **Bash**（次优）
- **C**（次优）
- **Go**
- **Rust**

## 快速开始

### 1. 使用 harness-generate 工具创建项目

```bash
harness-generate
```

按照交互式提示配置项目。

### 2. 初始化项目

进入项目目录并运行初始化脚本：

```bash
cd test-project
bash scripts/setup/init.sh
```

### 3. 搭建 Python 开发环境

```bash
# 创建虚拟环境
python3 -m venv venv

# 激活环境
source venv/bin/activate

# 安装依赖
pip install -r test-project/requirements.txt
```

### 4. 开始开发

查看 [AGENTS.md](./AGENTS.md) 了解完整的项目规范和架构指南。

## 目录结构

```
test-project/
├── agents/                      # Harness 开发系列 Agents
├── .coco/                       # IDE 配置（软链接）
│   └── agents → ../agents
├── .traecli/                    # IDE 配置（软链接）
│   └── agents → ../agents
├── .aime/                       # IDE 配置（软链接）
│   └── agents → ../agents
├── .claude/                     # IDE 配置（软链接）
│   └── agents → ../agents
├── AGENTS.md                    # 智能体入口地图
├── ARCHITECTURE.md              # 系统架构文档
├── README.md                    # 项目说明
├── test-project/              # 项目目录
│   ├── src/                     # 源代码目录
│   │   └── __init__.py
│   ├── test/                    # 测试目录
│   │   └── __init__.py
│   ├── requirements.txt         # 依赖管理
│   └── setup.py                 # 包配置文件
├── docs/                        # 文档目录
│   ├── architecture/            # 架构设计文档
│   ├── completed/               # 已完成的文档
│   ├── exec-plans/              # 执行计划
│   │   ├── plans/               # 计划文件
│   │   └── reviews/             # 评审文件
│   ├── product-specs/           # 产品规格文档（格式：{feature_id}_spec.md）
│   └── memory/                  # 用户特殊要求记录（格式：{要求}_.md）
├── scripts/                     # 脚本目录
│   ├── feedforward/             # 前馈引导脚本
│   ├── feedback/                # 反馈感应脚本
│   └── setup/                   # 项目初始化脚本
├── linting/                     # 代码检查规则
│   └── python/
├── references/                  # 参考资料目录
│   ├── docs/                    # 参考文档
│   └── repo/                    # 参考代码库
└── .github/
    └── workflows/               # CI/CD 工作流
```

## 核心组件

### 前馈引导（Feedforward）

预判智能体的行为，在行动之前进行引导，提高智能体首轮输出的成功率。

- 架构规范文档
- 开发指南
- 代码风格配置
- 项目初始化脚本

### 反馈感应（Feedback）

在智能体行动之后进行观察，帮助其自愈，在问题提交给人类审查之前实现尽可能多的自愈。

- 自动化测试
- Linter 检查
- 类型检查
- CI/CD 工作流

## 使用场景

- **系统级脚本开发**：内核钩子、探针脚本、远程执行脚本
- **AI 智能体开发**
- **自动化代码生成项目**
- **需要高可靠性的 AI 应用**
- **团队协作的 AI 辅助开发**

## 与 Trae IDE 结合使用

1. **在 Trae IDE 中打开项目**
2. **配置 Agent 指令**：
   - 在 Agent 配置中添加 `AGENTS.md` 作为入口文档
   - 设置工作目录为项目根目录
   - 启用文件读写权限

3. **迭代开发流程**：
   - **前馈阶段**：让 Agent 读取文档了解项目结构和状态
   - **执行阶段**：在 `test-project/src/` 目录下实现功能，在 `test-project/test/` 目录下编写测试
   - **反馈阶段**：运行测试和代码检查，根据反馈修复
   - **收尾阶段**：更新文档记录进展

## 学习资源

- [智能体管控工程(Harness Engineering)](http://m.toutiao.com/group/7621941861964939802/)
- [Harness Engineering究竟是什么?](http://m.toutiao.com/group/7625276762833863208/)
- [构建你的"约束工程"架构](http://m.toutiao.com/group/7626691524348101171/)

## 许可证

MIT
