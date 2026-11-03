package cn.edu.sicnu.cs.zjy.lab5

import android.content.Context
import android.content.res.Configuration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import cn.edu.sicnu.cs.zjy.lab5.model.PlayerRepository
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * UI 测试：RecyclerView 翻牌游戏界面（实验五）。
 *
 * 验证 24 张牌 / 4 列、点牌扣分、再点翻回、重置，以及横竖屏资源给出不同牌数。
 */
@RunWith(AndroidJUnit4::class)
class CardGameActivityTest {

    private lateinit var scenario: ActivityScenario<CardGameActivity>

    private val context: Context
        get() = ApplicationProvider.getApplicationContext()

    @Before
    fun setUp() {
        val app = ApplicationProvider.getApplicationContext<Lab5Application>()
        PlayerRepository.set(app.createDefaultProfile())
        scenario = ActivityScenario.launch(CardGameActivity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun showsTwentyFourCardsInFourColumns() {
        scenario.onActivity { activity ->
            val recyclerView = activity.findViewById<RecyclerView>(R.id.rvCards)
            assertEquals("竖屏应显示 24 张牌", 24, recyclerView.adapter?.itemCount)
            val layoutManager = recyclerView.layoutManager as GridLayoutManager
            assertEquals("竖屏应为 4 列", 4, layoutManager.spanCount)
        }

        onView(withId(R.id.tvScore))
            .check(matches(withText(context.getString(R.string.score_format, 0))))
    }

    @Test
    fun landscapeResourcesProvideDifferentCardCount() {
        fun configFor(orientation: Int): Configuration =
            Configuration(context.resources.configuration).apply { this.orientation = orientation }

        val portrait = context.createConfigurationContext(
            configFor(Configuration.ORIENTATION_PORTRAIT)
        )
        val landscape = context.createConfigurationContext(
            configFor(Configuration.ORIENTATION_LANDSCAPE)
        )

        assertEquals(24, portrait.resources.getInteger(R.integer.card_count))
        assertEquals(4, portrait.resources.getInteger(R.integer.card_span_count))
        assertEquals("横屏牌数应与竖屏不同", 18, landscape.resources.getInteger(R.integer.card_count))
        assertEquals(6, landscape.resources.getInteger(R.integer.card_span_count))
    }

    @Test
    fun flippingOneCardCostsOnePoint() {
        clickCard(0)

        onView(withId(R.id.tvScore))
            .check(matches(withText(context.getString(R.string.score_format, -1))))
    }

    @Test
    fun flippingSameCardBackDoesNotCostAgain() {
        clickCard(0)
        clickCard(0)

        onView(withId(R.id.tvScore))
            .check(matches(withText(context.getString(R.string.score_format, -1))))
    }

    @Test
    fun resetButtonStartsNewGameWithZeroScore() {
        clickCard(0)
        clickCard(1)
        onView(withId(R.id.btnResetGame)).perform(click())

        onView(withId(R.id.tvScore))
            .check(matches(withText(context.getString(R.string.score_format, 0))))
        scenario.onActivity { activity ->
            val recyclerView = activity.findViewById<RecyclerView>(R.id.rvCards)
            assertEquals(24, recyclerView.adapter?.itemCount)
        }
    }

    private fun clickCard(position: Int) {
        onView(withId(R.id.rvCards)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position, click())
        )
    }
}
