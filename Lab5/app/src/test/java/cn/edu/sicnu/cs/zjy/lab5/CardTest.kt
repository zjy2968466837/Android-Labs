package cn.edu.sicnu.cs.zjy.lab5

import cn.edu.sicnu.cs.zjy.lab5.model.Card
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Model 单元测试：单张牌的配对规则（两张与三张两种玩法）。
 *
 * 纯 JVM 测试，用 `./gradlew testDebugUnitTest` 运行，不需要模拟器。
 */
class CardTest {

    @Test
    fun sameRankScoresFour() {
        val card = Card(suit = "♥", rank = "A")
        assertEquals(4, card.match(arrayOf(Card(suit = "♠", rank = "A"))))
    }

    @Test
    fun sameSuitOnlyScoresOne() {
        val card = Card(suit = "♥", rank = "A")
        assertEquals(1, card.match(arrayOf(Card(suit = "♥", rank = "K"))))
    }

    @Test
    fun differentRankAndSuitScoresZero() {
        val card = Card(suit = "♥", rank = "A")
        assertEquals(0, card.match(arrayOf(Card(suit = "♠", rank = "K"))))
    }

    @Test
    fun noOtherCardScoresZero() {
        assertEquals(0, Card(suit = "♥", rank = "A").match(emptyArray()))
    }

    @Test
    fun threeCardsWithSameRankScoreFour() {
        val card = Card(suit = "♥", rank = "7")
        val others = arrayOf(Card(suit = "♠", rank = "7"), Card(suit = "♦", rank = "7"))

        assertEquals(4, card.match(others))
    }

    @Test
    fun threeCardsWithSameSuitScoreOne() {
        val card = Card(suit = "♣", rank = "7")
        val others = arrayOf(Card(suit = "♣", rank = "K"), Card(suit = "♣", rank = "3"))

        assertEquals(1, card.match(others))
    }

    @Test
    fun threeCardsSharingNeitherRankNorSuitScoreZero() {
        val card = Card(suit = "♣", rank = "7")
        // 点数有一张相同、花色也有一张相同，但三张整体既不“三条”也不“同花”
        val others = arrayOf(Card(suit = "♥", rank = "7"), Card(suit = "♣", rank = "3"))

        assertEquals(0, card.match(others))
    }

    @Test
    fun toStringCombinesSuitAndRank() {
        assertEquals("♥A", Card(suit = "♥", rank = "A").toString())
        assertEquals("♠10", Card(suit = "♠", rank = "10").toString())
    }

    @Test
    fun redSuitsAreRedAndBlackSuitsAreNot() {
        assertEquals(true, Card(suit = "♥", rank = "A").isRedSuit)
        assertEquals(true, Card(suit = "♦", rank = "A").isRedSuit)
        assertEquals(false, Card(suit = "♠", rank = "A").isRedSuit)
        assertEquals(false, Card(suit = "♣", rank = "A").isRedSuit)
    }
}
