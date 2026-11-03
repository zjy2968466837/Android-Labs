package cn.edu.sicnu.cs.zjy.lab5

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import cn.edu.sicnu.cs.zjy.lab5.model.Gender
import cn.edu.sicnu.cs.zjy.lab5.model.Hobby
import cn.edu.sicnu.cs.zjy.lab5.model.PlayerRepository
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * UI 测试：信息修改界面。
 *
 * 验证控件回填、单选/多选状态，以及“保存”写回 Model、“取消”不改动 Model。
 */
@RunWith(AndroidJUnit4::class)
class PersonInfoActivityTest {

    private lateinit var scenario: ActivityScenario<PersonInfoActivity>

    private val context: Context
        get() = ApplicationProvider.getApplicationContext()

    @Before
    fun setUp() {
        val app = ApplicationProvider.getApplicationContext<Lab5Application>()
        PlayerRepository.set(app.createDefaultProfile())
        scenario = ActivityScenario.launch(PersonInfoActivity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun formIsPrefilledFromCurrentProfile() {
        onView(withId(R.id.etName))
            .check(matches(withText(context.getString(R.string.default_player_name))))
        onView(withId(R.id.etStudentId))
            .check(matches(withText(context.getString(R.string.default_student_id))))
        onView(withId(R.id.etPhone))
            .check(matches(withText(context.getString(R.string.default_phone))))
        onView(withId(R.id.etEmail))
            .check(matches(withText(context.getString(R.string.default_email))))

        // 默认资料：男，爱好为阅读 + 游戏
        onView(withId(R.id.rbMale)).check(matches(isChecked()))
        onView(withId(R.id.rbFemale)).check(matches(isNotChecked()))
        onView(withId(R.id.cbReading)).check(matches(isChecked()))
        onView(withId(R.id.cbGame)).check(matches(isChecked()))
        onView(withId(R.id.cbTravel)).check(matches(isNotChecked()))
        onView(withId(R.id.cbShopping)).check(matches(isNotChecked()))
    }

    @Test
    fun saveWritesNewValuesIntoRepository() {
        onView(withId(R.id.etName)).perform(replaceText("李四"))
        onView(withId(R.id.etPhone)).perform(replaceText("13900139000"))
        onView(withId(R.id.rbFemale)).perform(click())
        onView(withId(R.id.cbTravel)).perform(click())   // 勾选
        onView(withId(R.id.cbReading)).perform(click())  // 取消勾选
        onView(withId(R.id.btnSave)).perform(click())

        val profile = PlayerRepository.profile
        assertEquals("李四", profile.name)
        assertEquals("13900139000", profile.phone)
        assertEquals(Gender.FEMALE, profile.gender)
        assertTrue(Hobby.TRAVEL in profile.hobbies)
        assertFalse(Hobby.READING in profile.hobbies)
        assertTrue(Hobby.GAME in profile.hobbies)
    }

    @Test
    fun cancelDoesNotChangeRepository() {
        val defaultName = context.getString(R.string.default_player_name)

        onView(withId(R.id.etName)).perform(replaceText("王五"))
        onView(withId(R.id.btnCancel)).perform(click())

        assertEquals(defaultName, PlayerRepository.profile.name)
        assertNotEquals("王五", PlayerRepository.profile.name)
    }

    @Test
    fun emptyNameIsRejectedAndActivityStaysOpen() {
        val defaultName = context.getString(R.string.default_player_name)

        onView(withId(R.id.etName)).perform(replaceText(""))
        onView(withId(R.id.btnSave)).perform(click())

        // 仍停留在修改界面，姓名未被写入
        onView(withId(R.id.etName)).check(matches(withText("")))
        assertEquals(defaultName, PlayerRepository.profile.name)
    }
}
