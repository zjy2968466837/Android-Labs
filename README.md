# Android-Labs

安卓开发课程实验作业仓库。**只存放源码**，实验报告作为附件单独提交。

## 目录结构

| 目录 | 说明 |
|---|---|
| `Lab1/` | 实验一：多语言版本交互式 Hello World |

每个 `LabN/` 都是一个独立的 Android Studio 工程，**`LabN` 本身即工程根**
（即 `LabN/app/`、`LabN/build.gradle.kts`、`LabN/settings.gradle.kts`）。

## 环境要求

| 项 | 要求 |
|---|---|
| JDK | 21（用 Android Studio 自带的 JBR 即可） |
| Android SDK | compileSdk / targetSdk 36，minSdk 30 |
| Gradle | 9.4.1，由项目自带的 Gradle Wrapper 自动处理，无需预装 |

## 如何编译运行

### 方式一：Android Studio

1. `File → Open`，选中 **`Lab1`** 这一层目录（不要选仓库根目录）
2. 首次打开时 IDE 会自动生成 `local.properties` 并执行 Gradle Sync
3. `Run → Run 'app'`

### 方式二：命令行

```bash
cd Lab1
./gradlew assembleDebug                      # Windows 下用 gradlew.bat
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n cn.edu.sicnu.cs.zjy.coursedemo/.MainActivity
```

> `local.properties` 记录的是本机 Android SDK 路径，不入库。Android Studio 打开工程时会自动生成；
> 若用命令行构建且该文件缺失，手动加一行 `sdk.dir=<你的SDK路径>` 即可。

## 仓库约定

只提交源码与构建脚本，以下内容不入库（详见 `.gitignore`）：

| 排除项 | 原因 |
|---|---|
| `build/`、`.gradle/` | 构建产物，可重新生成 |
| `local.properties` | 本机 SDK 路径，因机器而异 |
| `.idea/`、`*.iml` | IDE 工程配置，换机器后自动重新生成 |
| `*.docx` | 实验报告，随作业作为附件单独提交 |
| `Lab*/docs/` | 报告配图与运行截图，与能否编译运行无关 |
