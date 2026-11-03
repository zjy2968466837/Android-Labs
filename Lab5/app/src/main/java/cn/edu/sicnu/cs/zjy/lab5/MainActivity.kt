package cn.edu.sicnu.cs.zjy.lab5

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import cn.edu.sicnu.cs.zjy.lab5.databinding.ActivityMainBinding
import cn.edu.sicnu.cs.zjy.lab5.model.PlayerProfile
import cn.edu.sicnu.cs.zjy.lab5.model.PlayerRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * 主 Activity（MVC 里的 Controller）：读取 [PlayerRepository] 里的玩家资料并显示，
 * 同时提供两个二级 Activity 的入口：
 *
 * <ul>
 *   <li>修改个人信息 —— [PersonInfoActivity]；</li>
 *   <li>翻牌游戏 —— [CardGameActivity]。</li>
 * </ul>
 *
 * 实验五新增：工具栏右上角的“关于”菜单，点击后弹出关于对话框。
 * 两个二级界面都可能改动这份内存中的资料（改名、刷新最高分），
 * 所以这里在 [onResume] 里重新渲染，保证返回主界面时看到的总是最新数据。
 */
class MainActivity : AppCompatActivity() {

    /** 开启 ViewBinding 后由构建自动生成的绑定类 */
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applyEdgeToEdgeInsets(binding.contentMain)
        // 用布局里的 Toolbar 充当 ActionBar，菜单（关于）由 onCreateOptionsMenu 提供
        setSupportActionBar(binding.toolbar)

        binding.btnEditInfo.setOnClickListener {
            startActivity(Intent(this, PersonInfoActivity::class.java))
        }

        binding.btnPlayGame.setOnClickListener {
            startActivity(Intent(this, CardGameActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        // 从二级界面返回时也走这里，因此修改后的资料、最新最高分都会立即显示
        render(PlayerRepository.profile)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                showAboutDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /** 关于对话框：显示应用名、当前玩家姓名/学号与版本号 */
    private fun showAboutDialog() {
        val profile = PlayerRepository.profile
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.about_title)
            .setMessage(
                getString(
                    R.string.about_message,
                    profile.name,
                    profile.studentId,
                    getString(R.string.app_version)
                )
            )
            .setPositiveButton(R.string.about_ok, null)
            .show()
    }

    /** 把 Model 里的资料渲染到各个 TextView */
    private fun render(profile: PlayerProfile) = with(binding) {
        tvName.text = profile.name.ifBlank { getString(R.string.value_not_set) }
        tvGender.text = getString(profile.gender.labelRes())
        tvStudentId.text = profile.studentId.ifBlank { getString(R.string.value_not_set) }
        tvPhone.text = profile.phone.ifBlank { getString(R.string.value_not_set) }
        tvEmail.text = profile.email.ifBlank { getString(R.string.value_not_set) }
        tvHobby.text = if (profile.hobbies.isEmpty()) {
            getString(R.string.value_not_set)
        } else {
            profile.hobbies.joinToString(getString(R.string.hobby_separator)) { getString(it.labelRes()) }
        }
        tvHighScore.text = getString(R.string.high_score_value, profile.highScore)
        ivAvatar.showAvatar(profile.avatarUri)
    }
}
