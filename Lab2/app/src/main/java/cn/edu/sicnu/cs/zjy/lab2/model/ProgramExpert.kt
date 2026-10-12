package cn.edu.sicnu.cs.zjy.lab2.model

import androidx.annotation.StringRes
import cn.edu.sicnu.cs.zjy.lab2.R

/**
 * MVC 的 Model 层（对应教学视频里的 ProgramExpert 类）。
 *
 * <p>职责：接收「语言特性」的选择结果，返回对应的语言描述句子。
 * Model 层完全不知道界面的存在，只负责业务逻辑。
 *
 * <p><b>与视频写法的一点差别</b>：视频里是
 * `when (feature) { "快速" -> "你是C语言" ... }`，直接在代码里写中文字符串；
 * 而本实验要求「禁止在布局文件和代码中直接出现字符串」，所以这里以 Spinner 的
 * 选项下标为键，返回<b>字符串资源 ID</b>，由 View 层用 getString 解析。
 *
 * <p>下标与 @array/features 一一对应：
 * <pre>
 *   0 快速   -> C 语言
 *   1 容易   -> Python
 *   2 新语言 -> Kotlin
 *   3 面向对象 -> Java
 * </pre>
 */
class ProgramExpert {

    /** 根据下拉框选中项的下标，返回对应语言描述的字符串资源。 */
    @StringRes
    fun languageOf(featureIndex: Int): Int = when (featureIndex) {
        0 -> R.string.language_c
        1 -> R.string.language_python
        2 -> R.string.language_kotlin
        3 -> R.string.language_java
        else -> R.string.language_unknown
    }
}
