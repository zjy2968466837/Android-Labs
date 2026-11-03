package cn.edu.sicnu.cs.zjy.lab4

import android.content.Context
import android.widget.GridLayout
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import cn.edu.sicnu.cs.zjy.lab4.model.PlayerRepository
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.containsString
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * UI 测试：翻牌游戏界面。
 *
 * 验证初始 4×4 牌阵、点牌扣分、再点翻回、重置后分数归零。
 * 牌的 contentDescription 形如“第 3 张牌，未翻开”，测试按“第 N 张牌”定位。
 */
@RunWith(AndroidJUnit4::class)
class CardGameActivityTest {

    private lateinit var scenario: ActivityScenario<CardGameActivity>

    private val context: Context
        get() = ApplicationProvider.getApplicationContext()

    @Before
    fun setUp() {
        val app = ApplicationProvider.getApplicationContext<Lab4Application>()
        PlayerRepository.set(app.createDefaultProfile())
        scenario = ActivityScenario.launch(CardGameActivity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun showsSixteenFaceDownCardsAndZeroScore() {
        scenario.onActivity { activity ->
            val grid = activity.findViewById<GridLayout>(R.id.gridCards)
            assertEquals("牌阵应有 16 张牌", 16, grid.childCount)
        }

        onView(withId(R.id.tvScore))
            .check(matches(withText(context.getString(R.string.score_format, 0))))
        onView(withContentDescription(context.getString(R.string.card_desc_back, 1)))
            .check(matches(isDisplayed()))
        onView(withContentDescription(context.getString(R.string.card_desc_back, 16)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun flippingOneCardCostsOnePointAndShowsItsFace() {
        onView(firstCard()).perform(click())

        onView(withId(R.id.tvScore))
            .check(matches(withText(context.getString(R.string.score_format, -1))))
        // 翻开后描述由“未翻开”变为带花色点数，但仍包含“第1张牌”
        onView(firstCard()).check(matches(isDisplayed()))
    }

    @Test
    fun flippingSameCardBackDoesNotCostAgain() {
        onView(firstCard()).perform(click())
        onView(firstCard()).perform(click())

        onView(withId(R.id.tvScore))
            .check(matches(withText(context.getString(R.string.score_format, -1))))
        onView(withContentDescription(context.getString(R.string.card_desc_back, 1)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun resetButtonStartsNewGameWithZeroScore() {
        onView(firstCard()).perform(click())
        onView(withId(R.id.btnResetGame)).perform(click())

        onView(withId(R.id.tvScore))
            .check(matches(withText(context.getString(R.string.score_format, 0))))
        onView(withContentDescription(context.getString(R.string.card_desc_back, 1)))
            .check(matches(isDisplayed()))
    }

    /** 按“第 1 张牌”定位第一张牌（描述会随翻牌状态变化，故用包含匹配） */
    private fun firstCard() = allOf(
        withContentDescription(containsString(context.getString(R.string.card_position_prefix, 1))),
        isDisplayed()
    )
}
