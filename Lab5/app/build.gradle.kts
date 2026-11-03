plugins {
    // AGP 9 内置 Kotlin（android.builtInKotlin 默认 true）：
    // 直接编译 src 下的 .kt 源文件，无需再应用 org.jetbrains.kotlin.android 插件。
    alias(libs.plugins.android.application)
}

android {
    namespace = "cn.edu.sicnu.cs.zjy.lab5"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "cn.edu.sicnu.cs.zjy.lab5"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        // 作业要求：使用 ViewBinding 访问控件（自动生成 ActivityMainBinding 等）
        viewBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity.ktx)
    // 实验五：翻牌游戏界面改用 RecyclerView 实现
    implementation(libs.recyclerview)
    // 单元测试：在 JVM 上直接测 Model（Card / Deck / CardMatchingGame）
    testImplementation(libs.junit)
    // UI 测试：Espresso + ActivityScenarioRule 驱动真实界面
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
    // RecyclerView 的 UI 测试动作（actionOnItemAtPosition）
    androidTestImplementation(libs.espresso.contrib)
}
