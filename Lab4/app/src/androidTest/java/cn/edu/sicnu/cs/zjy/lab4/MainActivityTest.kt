package cn.edu.sicnu.cs.zjy.lab4

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import cn.edu.sicnu.cs.zjy.lab4.model.PlayerRepository
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * UI 测试：主 Activity。
 *
 * 用 Espresso 操作真实界面，验证默认资料显示、跳转修改信息并保存后回显、
 * 以及跳转翻牌游戏三个场景。
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private lateinit var scenario: ActivityScenario<MainActivity>

    private val context: Context
        get() = ApplicationProvider.getApplicationContext()

    @Before
    fun setUp() {
        // 每个用例都从同一份默认资料开始，避免用例之间互相影响
        val app = ApplicationProvider.getApplicationContext<Lab4Application>()
        PlayerRepository.set(app.createDefaultProfile())
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun showsAppTitleAndDefaultPlayerInfo() {
        onView(withText(R.string.app_name)).check(matches(isDisplayed()))

        onView(withId(R.id.tvName))
            .check(matches(withText(context.getString(R.string.default_player_name))))
        onView(withId(R.id.tvStudentId))
            .check(matches(withText(context.getString(R.string.default_student_id))))
        onView(withId(R.id.tvPhone))
            .check(matches(withText(context.getString(R.string.default_phone))))
        onView(withId(R.id.tvHighScore)).check(matches(withText("0")))
        onView(withId(R.id.btnEditInfo)).check(matches(isDisplayed()))
        onView(withId(R.id.btnPlayGame)).check(matches(isDisplayed()))
    }

    @Test
    fun editInfoAndSaveUpdatesMainScreen() {
        onView(withId(R.id.btnEditInfo)).perform(click())

        // 修改界面已打开，且输入框带出了当前资料
        onView(withId(R.id.etName)).check(
            matches(withText(context.getString(R.string.default_player_name)))
        )
        onView(withId(R.id.etName)).perform(replaceText("李四"))
        onView(withId(R.id.btnSave)).perform(click())

        // 保存后回到主界面，姓名立即刷新
        onView(withId(R.id.tvName)).check(matches(withText("李四")))
    }

    @Test
    fun playGameButtonOpensCardGameWithZeroScore() {
        onView(withId(R.id.btnPlayGame)).perform(click())

        onView(withId(R.id.tvScore)).check(
            matches(withText(context.getString(R.string.score_format, 0)))
        )
    }
}
