package cn.edu.sicnu.cs.zjy.lab5

import android.app.Application
import cn.edu.sicnu.cs.zjy.lab5.model.Gender
import cn.edu.sicnu.cs.zjy.lab5.model.Hobby
import cn.edu.sicnu.cs.zjy.lab5.model.PlayerProfile
import cn.edu.sicnu.cs.zjy.lab5.model.PlayerRepository

/**
 * 进程启动时把“默认玩家资料”放进 [PlayerRepository]。
 *
 * 默认值写在 res/values/strings.xml 里（作业禁止在代码中直接写界面字符串），
 * 这里只是把资源读出来组装成 Model 对象；资料全程只存内存，不落文件。
 */
class Lab5Application : Application() {

    override fun onCreate() {
        super.onCreate()
        PlayerRepository.set(createDefaultProfile())
    }

    /** 供界面测试在用例之间重置数据，保证每个用例从同一份默认资料开始 */
    fun createDefaultProfile() = PlayerProfile(
        name = getString(R.string.default_player_name),
        gender = Gender.MALE,
        studentId = getString(R.string.default_student_id),
        phone = getString(R.string.default_phone),
        email = getString(R.string.default_email),
        hobbies = mutableSetOf(Hobby.READING, Hobby.GAME)
    )
}
