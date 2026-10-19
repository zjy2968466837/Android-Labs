package cn.edu.sicnu.cs.zjy.lab3

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import cn.edu.sicnu.cs.zjy.lab3.databinding.ActivityStopwatchBinding

/**
 * 二级 Activity ④：StopWatch 秒表。
 *
 * <p>要求：横屏、竖屏来回切换时计时器必须连续计时、不归零。
 *
 * <p>实现要点（对应教学视频 4「Activity 生命周期」）：
 * 屏幕旋转属于系统配置变更，Activity 会被销毁并重建。因此在
 * [onSaveInstanceState] 里把「已累计毫秒数」和「是否正在计时」存进 Bundle，
 * 重建时在 [onCreate] 里取出恢复。计时基准用 SystemClock.elapsedRealtime()，
 * 与系统开机时间挂钩，不受重建影响，从而保证跨旋转连续计时。
 */
class StopWatchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStopwatchBinding

    /** 是否正在计时 */
    private var isRunning = false

    /** 已累计的毫秒数（暂停时刻固化的净值） */
    private var accumulatedMs = 0L

    /** 本次开始计时时的 SystemClock.elapsedRealtime()，用于累加 */
    private var lastStartRealtime = 0L

    private val handler = Handler(Looper.getMainLooper())

    /** 每 10ms 刷新一次界面（百分秒精度） */
    private val ticker = object : Runnable {
        override fun run() {
            render(elapsedMs())
            handler.postDelayed(this, REFRESH_INTERVAL_MS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        enableEdgeToEdge()
        binding = ActivityStopwatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applyEdgeToEdgeInsets(binding.contentStopwatch)

        // 若是被系统销毁后重建（如屏幕旋转），从 Bundle 恢复计时状态
        savedInstanceState?.let {
            accumulatedMs = it.getLong(KEY_ACCUMULATED_MS)
            isRunning = it.getBoolean(KEY_IS_RUNNING)
            if (isRunning) {
                lastStartRealtime = SystemClock.elapsedRealtime()
            }
        }

        binding.btnStartPause.setOnClickListener { toggleStartPause() }
        binding.btnReset.setOnClickListener { reset() }

        updateStartPauseButton()
        render(elapsedMs())
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        // 重新可见且仍在计时中，则继续刷新
        if (isRunning) handler.post(ticker)
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
        // 离开前台时停掉刷新（计时基准是绝对时间，不受影响）
        handler.removeCallbacks(ticker)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
        handler.removeCallbacks(ticker)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState")
        // 销毁前保存计时状态 —— 横竖屏切换连续计时的关键
        outState.putLong(KEY_ACCUMULATED_MS, elapsedMs())
        outState.putBoolean(KEY_IS_RUNNING, isRunning)
    }

    /** 当前已计时的总毫秒数：累计值 + 本段正在计时的时间 */
    private fun elapsedMs(): Long =
        if (isRunning) accumulatedMs + (SystemClock.elapsedRealtime() - lastStartRealtime)
        else accumulatedMs

    private fun toggleStartPause() {
        if (isRunning) {
            // 暂停：把当前值固化进 accumulatedMs
            accumulatedMs = elapsedMs()
            isRunning = false
            handler.removeCallbacks(ticker)
        } else {
            // 开始/继续：以当前绝对时间为新的基准点
            lastStartRealtime = SystemClock.elapsedRealtime()
            isRunning = true
            handler.post(ticker)
        }
        updateStartPauseButton()
    }

    private fun reset() {
        isRunning = false
        accumulatedMs = 0L
        handler.removeCallbacks(ticker)
        updateStartPauseButton()
        render(0L)
    }

    private fun updateStartPauseButton() {
        binding.btnStartPause.setText(
            if (isRunning) R.string.btn_stopwatch_pause else R.string.btn_stopwatch_start
        )
    }

    /** 把毫秒渲染成 mm:ss.cc 显示到界面上 */
    private fun render(ms: Long) {
        val totalCentis = ms / 10
        val centis = (totalCentis % 100).toInt()
        val totalSeconds = totalCentis / 100
        val seconds = (totalSeconds % 60).toInt()
        val minutes = (totalSeconds / 60).toInt()
        binding.tvTime.text = getString(R.string.stopwatch_time_format, minutes, seconds, centis)
    }

    companion object {
        private const val TAG = "StopWatchActivity"
        private const val REFRESH_INTERVAL_MS = 10L
        private const val KEY_ACCUMULATED_MS = "key_accumulated_ms"
        private const val KEY_IS_RUNNING = "key_is_running"
    }
}
