package cn.edu.sicnu.cs.zjy.coursedemo;

import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * 实验一：多语言版本交互式 Hello World。
 *
 * <p>界面完全由纯代码搭建（不使用任何 layout XML），自上而下依次为：
 * 顶部标题栏（显示当前语言对应的地区名）、国旗图片、问候语、按钮。
 *
 * <p>三处内容会随系统语言自动变化，切换语言后无需改任何代码：
 * <ul>
 *   <li>文字 —— res/values、values-zh、values-ru 三套 strings.xml</li>
 *   <li>国旗 —— drawable、drawable-zh、drawable-ru 三份同名的 flag 图片</li>
 * </ul>
 * 配色则随系统深浅色主题在 values / values-night 之间自动切换。
 */
public class MainActivity extends AppCompatActivity {

    private static final String STATE_CLICKED = "clicked";

    /** 点击后置为 true，决定显示问候语还是「已被点击」文案。 */
    private boolean clicked = false;
    private TextView messageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Android 15 起 targetSdk 35+ 强制 edge-to-edge，界面会绘制到状态栏/导航栏之下，
        // 因此统一开启 edge-to-edge，并在下面用 WindowInsets 把系统栏高度补成留白。
        EdgeToEdge.enable(this);

        if (savedInstanceState != null) {
            clicked = savedInstanceState.getBoolean(STATE_CLICKED, false);
        }

        setContentView(buildContentView());

        // 状态栏底色是深紫（标题栏延伸上来的），固定用浅色图标才看得清
        WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView())
                .setAppearanceLightStatusBars(false);
    }

    /** 用纯代码搭出整个界面，返回可直接交给 setContentView 的根视图。 */
    private ViewGroup buildContentView() {
        // ── 顶部标题栏：显示当前语言对应的地区名（America / 中国 / Россия）──
        Toolbar toolbar = new Toolbar(this);
        toolbar.setBackgroundColor(color(R.color.appbar_background));
        toolbar.setTitle(R.string.region_name);
        toolbar.setTitleTextColor(color(R.color.appbar_foreground));

        // ── 内容区：垂直居中排布国旗、问候语与按钮 ──
        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setGravity(Gravity.CENTER);
        content.setBackgroundColor(color(R.color.screen_background));
        final int padH = dp(24);
        final int padV = dp(16);
        content.setPadding(padH, padV, padH, padV);

        // 国旗：R.drawable.flag 会按系统语言自动解析到
        // drawable / drawable-zh / drawable-ru 里的同名资源
        ImageView flagView = new ImageView(this);
        flagView.setImageResource(R.drawable.flag);
        flagView.setAdjustViewBounds(true);
        flagView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        content.addView(flagView, new LinearLayout.LayoutParams(
                dp(260), ViewGroup.LayoutParams.WRAP_CONTENT));

        // 问候语，点击后换成另一条文案
        messageView = new TextView(this);
        messageView.setText(clicked ? R.string.i_am_clicked : R.string.hello_world);
        messageView.setTextSize(20f);
        messageView.setTextColor(color(R.color.text_primary));
        messageView.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams messageParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        messageParams.topMargin = dp(40);
        content.addView(messageView, messageParams);

        // 按钮
        Button clickButton = new Button(this);
        clickButton.setText(R.string.click_me);
        clickButton.setOnClickListener(v -> {
            clicked = true;
            messageView.setText(R.string.i_am_clicked);
        });
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonParams.topMargin = dp(48);
        content.addView(clickButton, buttonParams);

        // ── 根布局：标题栏在上，内容区占满剩余高度 ──
        // 根布局背景取标题栏颜色：edge-to-edge 下状态栏那条区域会露出根布局背景，
        // 这样它与标题栏连成一片紫色，而不是留出一条突兀的白边。
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(color(R.color.appbar_background));
        root.addView(toolbar, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(56)));
        root.addView(content, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));

        // 把系统栏占掉的高度补成内边距：上边留给根布局（呈紫色），下边留给内容区（呈页面底色）。
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, windowInsets) -> {
            Insets bars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, 0);
            content.setPadding(padH, padV, padH, padV + bars.bottom);
            return windowInsets;
        });
        return root;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // 旋转屏幕、切换深浅色主题都会重建 Activity，这里保住点击状态
        outState.putBoolean(STATE_CLICKED, clicked);
    }

    private int color(int colorResId) {
        return ContextCompat.getColor(this, colorResId);
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
