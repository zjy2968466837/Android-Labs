plugins {
    // AGP 9 内置 Kotlin（android.builtInKotlin 默认 true）：
    // 直接编译 src 下的 .kt 源文件，无需再应用 org.jetbrains.kotlin.android 插件。
    alias(libs.plugins.android.application)
}

android {
    namespace = "cn.edu.sicnu.cs.zjy.lab2"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "cn.edu.sicnu.cs.zjy.lab2"
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
    // 内置 Kotlin 下无需单独设置 kotlinOptions.jvmTarget，
    // 其默认值与上面的 compileOptions.targetCompatibility 相同。
    buildFeatures {
        // 作业要求：建议使用 ViewBinding 访问控件（自动生成 ActivityMainBinding）
        viewBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}
