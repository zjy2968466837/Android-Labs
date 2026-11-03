package cn.edu.sicnu.cs.zjy.lab5

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Edge-to-edge（沉浸式）留白处理。
 *
 * Android 15 起 targetSdk 35+ 强制开启 edge-to-edge，界面会绘制到状态栏/导航栏之下。
 * 这里统一处理：
 * - 根布局（紫色，与顶部 Toolbar 同色）接住「状态栏 + 左右」留白，
 *   让状态栏区域也是紫色，与 Toolbar 连成一片；
 * - 内容容器额外接住「导航栏」留白，避免内容被底部手势条挡住。
 *
 * @param bottomPaddingTarget 需要为底部导航栏预留留白的内容容器（可空）
 */
fun View.applyEdgeToEdgeInsets(bottomPaddingTarget: View? = null) {
    val baseBottomPadding = bottomPaddingTarget?.paddingBottom ?: 0
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        // 根布局：顶部/左右留白由根布局的紫色背景呈现
        view.setPadding(bars.left, bars.top, bars.right, 0)
        // 内容容器：底部留白并入原有 padding
        bottomPaddingTarget?.setPadding(
            bottomPaddingTarget.paddingLeft,
            bottomPaddingTarget.paddingTop,
            bottomPaddingTarget.paddingRight,
            baseBottomPadding + bars.bottom
        )
        insets
    }
}
