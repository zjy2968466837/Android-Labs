package cn.edu.sicnu.cs.zjy.lab3

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import cn.edu.sicnu.cs.zjy.lab3.databinding.ActivityThirdBinding

/**
 * 二级 Activity ②：反向传参的返回端。
 *
 * 用户在输入框输入内容，点「确定」后把内容回传给 [MainActivity] 并关闭自己。
 */
class ThirdActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThirdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applyEdgeToEdgeInsets(binding.contentThird)

        binding.btnConfirm.setOnClickListener {
            // 反向传参：新建一个只用来携带数据的 Intent（不指定跳转目标）
            val replyIntent = Intent().apply {
                putExtra(EXTRA_REPLY_MESSAGE, binding.etReplyInput.text.toString())
            }
            // 设回结果码 + 数据 Intent，然后 finish() 关闭自己，触发主界面 onActivityResult
            setResult(RESULT_CODE_OK, replyIntent)
            finish()
        }
    }

    companion object {
        /** 反向传参的 Key：返回端与主界面接收端必须一致 */
        const val EXTRA_REPLY_MESSAGE = "second_message"

        /** 结果码：1 表示操作成功（与主界面 onActivityResult 里的判断对应） */
        const val RESULT_CODE_OK = 1
    }
}
