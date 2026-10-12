package cn.edu.sicnu.cs.zjy.lab2

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.edu.sicnu.cs.zjy.lab2.databinding.ActivityMainBinding
import cn.edu.sicnu.cs.zjy.lab2.model.ProgramExpert

/**
 * 实验二：UI 控件代码生成以及 MVC。
 *
 * <p>本类同时承担 MVC 中的 View 与 Controller 两个角色（与教学视频一致：
 * 界面由 activity_main.xml 描述，即 View 层；事件响应与调度写在 Activity 里，即 Controller 层）。
 *
 * <ul>
 *   <li>功能区 1：点击按钮后用<b>代码动态创建 TextView</b>，追加进 ScrollView 嵌套的 LinearLayout；</li>
 *   <li>功能区 2：交互式 ProgramExpert —— 点击「确定」后，Controller 取 Spinner 选中项，
 *       查询 Model 层 ProgramExpert，再把结果回填给 TextView。</li>
 * </ul>
 *
 * <p>约定：布局文件与代码中不出现任何硬编码界面字符串，界面文字一律取自
 * res/values/strings.xml；控件访问一律通过 ViewBinding（ActivityMainBinding）。
 */
class MainActivity : AppCompatActivity() {

    /** ① 开启 ViewBinding 后由构建自动生成的绑定类 */
    private lateinit var binding: ActivityMainBinding

    /** MVC 的 Model 层：根据「语言特性」返回对应的语言描述 */
    private val programExpert = ProgramExpert()

    /** 区 1 已生成 TextView 的数量，用于拼出「This is a new textView:n」 */
    private var messageCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ② Android 15 起 targetSdk 35+ 强制 edge-to-edge，这里显式开启并处理系统栏留白
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyWindowInsets()

        // ③ 区 1：交互后用代码动态添加 TextView
        binding.btnAddMessage.setOnClickListener { addMessage() }

        // ④ 区 2：Controller 响应按钮事件，查询 Model 层并返回结果
        binding.btnConfirm.setOnClickListener { queryLanguage() }
    }

    // ------------------------------------------------------------------
    // 功能区 1：用代码动态生成 TextView（不预置条目，也不用 findViewById）
    // ------------------------------------------------------------------

    private fun addMessage() {
        messageCount++

        // 完全用代码 new 出一个 TextView —— 这就是「UI 控件代码生成」
        val messageView = TextView(this).apply {
            text = getString(R.string.message_format, messageCount)
            textSize = 16f
            setTextColor(ContextCompat.getColor(context, R.color.text_primary))
            val p = dp(8)
            setPadding(p, p, p, p)
        }

        // 追加到 ScrollView 内部的 LinearLayout 中（滚动由外层 ScrollView 负责）
        binding.llMessages.addView(messageView)

        // 追加后自动滚动到最新一条
        binding.svMessages.post { binding.svMessages.fullScroll(View.FOCUS_DOWN) }
    }

    // ------------------------------------------------------------------
    // 功能区 2：MVC 调用链
    //   View(按钮点击) -> Controller(本方法) -> Model(ProgramExpert) -> 回填 View
    // ------------------------------------------------------------------

    private fun queryLanguage() {
        // Controller：读取用户在下拉框里的选择（第几项）
        val featureIndex = binding.spinnerFeature.selectedItemPosition

        // 查询 Model 层，拿到对应的语言描述（字符串资源 ID）
        val languageResId = programExpert.languageOf(featureIndex)

        // 回填 View：把结果显示到大字区域
        binding.tvResult.setText(languageResId)
    }

    // ------------------------------------------------------------------
    // edge-to-edge 适配：状态栏留白由根布局（紫色，与应用栏连成一片）呈现，
    // 导航栏留白并入功能区 2 的绿色底，保证绿色一直铺到屏幕底部。
    // ------------------------------------------------------------------

    private fun applyWindowInsets() {
        val basePadding = dp(16)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, 0)
            binding.sectionMvc.setPadding(
                basePadding, basePadding, basePadding, basePadding + bars.bottom
            )
            insets
        }
    }

    private fun dp(value: Int): Int = Math.round(value * resources.displayMetrics.density)
}
