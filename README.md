# Android-Labs

安卓开发课程作业提交仓库，按实验（Lab）分文件夹归档。

## 目录结构

| 目录 | 说明 |
|---|---|
| `Lab1/` | 实验一 |

每个 `LabN/` 都是一个**独立的 Android Studio 工程**，`LabN` 本身即工程根
（即 `LabN/app/`、`LabN/build.gradle.kts`、`LabN/settings.gradle.kts`）。

## 开发环境

- Android Studio 2026.1.1
- Gradle 9.4.1（Gradle Wrapper）
- JDK 21（Android Studio 内置 JBR）

## 提交约定

- 每个实验一个 `LabN/` 文件夹，互不依赖
- 不提交 `build/`、`.gradle/`、`local.properties`、签名文件
- 提交 `.idea/` 下的共享配置（编译/运行配置），但 `workspace.xml` 等本机状态文件已忽略
