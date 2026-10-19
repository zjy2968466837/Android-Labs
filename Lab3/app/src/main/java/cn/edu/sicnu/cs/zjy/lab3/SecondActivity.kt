package cn.edu.sicnu.cs.zjy.lab3

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import cn.edu.sicnu.cs.zjy.lab3.databinding.ActivitySecondBinding

/**
 * 二级 Activity ①：正向传参的接收端。
 *
 * 取出 [MainActivity] 通过 Intent 传来的参数并显示。
 */
class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applyEdgeToEdgeInsets(binding.contentSecond)

        // 拿到本次启动自己的 Intent，再取出里面携带的参数
        val received = intent.getStringExtra(EXTRA_FORWARD_MESSAGE)
        binding.tvReceived.text = if (received.isNullOrEmpty()) {
            getString(R.string.second_placeholder)
        } else {
            received
        }
    }

    companion object {
        /** 正向传参的 Key：发送端（MainActivity）与接收端必须一致 */
        const val EXTRA_FORWARD_MESSAGE = "first_message"
    }
}
