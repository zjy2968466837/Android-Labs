// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // AGP 9 起内置 Kotlin 支持（android.builtInKotlin 默认 true），
    // 无需再单独应用 org.jetbrains.kotlin.android 插件。
    alias(libs.plugins.android.application) apply false
}
