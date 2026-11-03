package cn.edu.sicnu.cs.zjy.lab4

import cn.edu.sicnu.cs.zjy.lab4.model.Card
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Model 单元测试：单张牌的配对规则。
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
