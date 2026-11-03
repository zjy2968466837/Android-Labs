package cn.edu.sicnu.cs.zjy.lab5

import cn.edu.sicnu.cs.zjy.lab5.model.CardMatchingGame
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Model 单元测试：三张牌玩法的计分、配对与 reset()。
 *
 * 牌是随机发的，但用例不依赖具体发到什么牌：
 * 24 张牌分布在 4 种花色上，按抽屉原理至少有 6 张同花色，因此必然能凑出三张同花；
 * 同理也必然存在“点数、花色都不完全一致”的三张。
 */
class CardMatchingGameTest {

    @Test
    fun newGameStartsWithZeroScoreAndAllCardsFaceDown() {
        val game = CardMatchingGame(CARD_COUNT)

        assertEquals(0, game.score)
        assertEquals(CARD_COUNT, game.cardCount)
        repeat(CARD_COUNT) { index ->
            assertFalse(game.cardAtIndex(index).isChosen)
            assertFalse(game.cardAtIndex(index).isMatched)
        }
    }

    @Test
    fun flippingFirstCardCostsOnePoint() {
        val game = CardMatchingGame(CARD_COUNT)

        game.chooseCardAtIndex(0)

        assertEquals(-1, game.score)
        assertTrue(game.cardAtIndex(0).isChosen)
    }

    @Test
    fun flippingSameCardBackDoesNotCostAgain() {
        val game = CardMatchingGame(CARD_COUNT)

        game.chooseCardAtIndex(0)
        game.chooseCardAtIndex(0)

        assertEquals(-1, game.score)
        assertFalse(game.cardAtIndex(0).isChosen)
    }

    @Test
    fun threeSameSuitCardsMatchAndScoreBonus() {
        val game = CardMatchingGame(CARD_COUNT)
        val (first, second, third) = findSameSuitTriple(game)

        game.chooseCardAtIndex(first)
        game.chooseCardAtIndex(second)
        assertEquals("翻开两张只扣两次成本", -2, game.score)

        game.chooseCardAtIndex(third)
        // -1 -1 + 同花 1×4 -1 = +1
        assertEquals(1, game.score)
        assertTrue(game.cardAtIndex(first).isMatched)
        assertTrue(game.cardAtIndex(second).isMatched)
        assertTrue(game.cardAtIndex(third).isMatched)
    }

    @Test
    fun threeCardsWithoutCommonRankOrSuitPenalizeAndFlipBack() {
        val game = CardMatchingGame(CARD_COUNT)
        val (first, second, third) = findMixedTriple(game)

        game.chooseCardAtIndex(first)
        game.chooseCardAtIndex(second)
        game.chooseCardAtIndex(third)

        // 前两张 -2，不配对扣 2 分，第三张成本 -1 → -5
        assertEquals(-5, game.score)
        assertFalse("失配后第一张应翻回去", game.cardAtIndex(first).isChosen)
        assertFalse("失配后第二张应翻回去", game.cardAtIndex(second).isChosen)
        assertTrue("刚翻开的第三张保持翻开", game.cardAtIndex(third).isChosen)
        assertFalse(game.cardAtIndex(third).isMatched)
    }

    @Test
    fun matchedCardsCannotBeChosenAgain() {
        val game = CardMatchingGame(CARD_COUNT)
        val (first, second, third) = findSameSuitTriple(game)
        game.chooseCardAtIndex(first)
        game.chooseCardAtIndex(second)
        game.chooseCardAtIndex(third)
        val scoreAfterMatch = game.score

        game.chooseCardAtIndex(first)

        assertEquals(scoreAfterMatch, game.score)
        assertTrue(game.cardAtIndex(first).isMatched)
    }

    @Test
    fun resetClearsScoreAndDealsNewCards() {
        val game = CardMatchingGame(CARD_COUNT)
        game.chooseCardAtIndex(0)
        game.chooseCardAtIndex(1)

        game.reset()

        assertEquals(0, game.score)
        assertEquals(CARD_COUNT, game.cardCount)
        repeat(CARD_COUNT) { index ->
            assertFalse(game.cardAtIndex(index).isChosen)
            assertFalse(game.cardAtIndex(index).isMatched)
        }
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun choosingCardBeyondBoardThrows() {
        CardMatchingGame(CARD_COUNT).cardAtIndex(CARD_COUNT)
    }

    /** 同花色的三张（24 张牌中必然存在） */
    private fun findSameSuitTriple(game: CardMatchingGame): Triple<Int, Int, Int> {
        val bySuit = mutableMapOf<String, MutableList<Int>>()
        for (index in 0 until CARD_COUNT) {
            val suit = game.cardAtIndex(index).suitString
            bySuit.getOrPut(suit) { mutableListOf() }.add(index)
        }
        val triple = bySuit.values.firstOrNull { it.size >= 3 }
            ?: throw AssertionError("24 张牌分布在 4 种花色上，必然存在同花色的 3 张")
        return Triple(triple[0], triple[1], triple[2])
    }

    /** 点数、花色都不完全一致的三张（不配对的组合，必然存在） */
    private fun findMixedTriple(game: CardMatchingGame): Triple<Int, Int, Int> {
        for (a in 0 until CARD_COUNT) {
            for (b in a + 1 until CARD_COUNT) {
                for (c in b + 1 until CARD_COUNT) {
                    val cards = arrayOf(game.cardAtIndex(a), game.cardAtIndex(b), game.cardAtIndex(c))
                    val allSameRank = cards.all { it.rankString == cards[0].rankString }
                    val allSameSuit = cards.all { it.suitString == cards[0].suitString }
                    if (!allSameRank && !allSameSuit) {
                        return Triple(a, b, c)
                    }
                }
            }
        }
        throw AssertionError("24 张牌中必然存在不配对的三张")
    }

    private companion object {
        /** 竖屏默认牌数（values/integers.xml） */
        const val CARD_COUNT = 24
    }
}
