package cn.edu.sicnu.cs.zjy.lab3

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import cn.edu.sicnu.cs.zjy.lab3.databinding.ActivityMainBinding

/**
 * 实验三：多 Activity。主界面提供 4 个按钮，分别启动 4 个二级 Activity：
 *
 * <ul>
 *   <li>按钮①：正向传参 —— 把输入框内容传给 [SecondActivity] 并显示；</li>
 *   <li>按钮②：反向传参 —— 启动 [ThirdActivity] 输入内容，回传后显示到本界面；</li>
 *   <li>按钮③：隐式 Intent 调系统相机拍照 —— [CameraActivity]；</li>
 *   <li>按钮④：StopWatch 秒表 —— [StopWatchActivity]。</li>
 * </ul>
 *
 * <p>反向传参沿用教学视频里的经典写法：startActivityForResult + onActivityResult
 * （该 API 在新版本已被标记为废弃，但为与教学视频一致仍采用）。
 */
@Suppress("DEPRECATION", "OVERRIDE_DEPRECATION")
class MainActivity : AppCompatActivity() {

    /** 开启 ViewBinding 后由构建自动生成的绑定类 */
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applyEdgeToEdgeInsets(binding.contentMain)

        // ① 正向传参：构造 Intent 并 putExtra，直接 startActivity
        binding.btnLaunchForward.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java).apply {
                putExtra(SecondActivity.EXTRA_FORWARD_MESSAGE, binding.etForwardInput.text.toString())
            }
            startActivity(intent)
        }

        // ② 反向传参：用 startActivityForResult 启动，才能收到回传的数据
        binding.btnLaunchReverse.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            startActivityForResult(intent, REQUEST_REVERSE)
        }

        // ③ 拍照界面
        binding.btnLaunchCamera.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }

        // ④ 秒表界面
        binding.btnLaunchStopwatch.setOnClickListener {
            startActivity(Intent(this, StopWatchActivity::class.java))
        }
    }

    /**
     * 反向传参的回调：二级 Activity 调用 setResult(...) + finish() 后系统回调本方法。
     * 先核对请求码（区分是哪个请求返回的），再核对结果码是否为「成功」。
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_REVERSE && resultCode == ThirdActivity.RESULT_CODE_OK) {
            // data 可能为 null（用户直接返回未确定），用安全调用 ?. 兜底
            binding.tvReverseResult.text = data?.getStringExtra(ThirdActivity.EXTRA_REPLY_MESSAGE)
                ?: getString(R.string.reverse_result_default)
        }
    }

    companion object {
        /** 反向传参的请求码：让回调能区分返回来源（本实验只有一个，用 1 即可） */
        private const val REQUEST_REVERSE = 1
    }
}
