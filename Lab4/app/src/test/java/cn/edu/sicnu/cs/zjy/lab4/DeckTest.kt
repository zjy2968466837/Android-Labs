package cn.edu.sicnu.cs.zjy.lab4

import cn.edu.sicnu.cs.zjy.lab4.model.Card
import cn.edu.sicnu.cs.zjy.lab4.model.Deck
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** Model 单元测试：一副牌共 52 张，随机抽取不重复，抽完返回 null。 */
class DeckTest {

    @Test
    fun newDeckHasFiftyTwoCards() {
        assertEquals(52, Deck().size)
    }

    @Test
    fun drawingAllCardsReturnsEveryCardExactlyOnce() {
        val deck = Deck()
        val drawn = mutableListOf<Card>()

        while (deck.size > 0) {
            drawn.add(requireNotNull(deck.drawRandomCard()))
        }

        assertEquals(52, drawn.size)
        assertEquals("52 张牌不应有重复", 52, drawn.toSet().size)
    }

    @Test
    fun drawingFromEmptyDeckReturnsNull() {
        val deck = Deck()
        repeat(52) { deck.drawRandomCard() }

        assertEquals(0, deck.size)
        assertNull(deck.drawRandomCard())
    }

    @Test
    fun drawnCardsAlwaysUseValidSuitsAndRanks() {
        val deck = Deck()
        while (deck.size > 0) {
            val card = requireNotNull(deck.drawRandomCard())
            assertTrue(card.suitString in Card.validSuits)
            assertTrue(card.rankString in Card.rankStrings)
        }
    }
}
