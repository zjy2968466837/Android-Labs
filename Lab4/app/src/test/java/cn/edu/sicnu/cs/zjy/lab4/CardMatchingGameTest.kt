package cn.edu.sicnu.cs.zjy.lab4

import cn.edu.sicnu.cs.zjy.lab4.model.CardMatchingGame
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Model 单元测试：翻牌游戏的计分与配对规则。
 *
 * 牌是随机发的，但测试不依赖具体发到什么牌：
 * 16 张牌只有 13 个点数，按抽屉原理必然存在点数相同的一对；
 * 同理也必然存在点数、花色都不同的一对。用例先用这两条性质找到目标牌，再断言分数。
 */
class CardMatchingGameTest {

    @Test
    fun newGameStartsWithZeroScoreAndAllCardsFaceDown() {
        val game = CardMatchingGame(CARD_COUNT)

        assertEquals(0, game.score)
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
    fun matchingRankPairScoresBonusAndMarksBothMatched() {
        val game = CardMatchingGame(CARD_COUNT)
        val (first, second) = findSameRankPair(game)

        game.chooseCardAtIndex(first)
        game.chooseCardAtIndex(second)

        // 第一张 -1，配对 +4×4，第二张 -1 → 14
        assertEquals(14, game.score)
        assertTrue(game.cardAtIndex(first).isMatched)
        assertTrue(game.cardAtIndex(second).isMatched)
    }

    @Test
    fun matchedCardCannotBeFlippedAgain() {
        val game = CardMatchingGame(CARD_COUNT)
        val (first, second) = findSameRankPair(game)
        game.chooseCardAtIndex(first)
        game.chooseCardAtIndex(second)
        val scoreAfterMatch = game.score

        game.chooseCardAtIndex(first)

        assertEquals(scoreAfterMatch, game.score)
        assertTrue(game.cardAtIndex(first).isMatched)
    }

    @Test
    fun mismatchedPairCostsPenaltyAndFlipsFirstCardBack() {
        val game = CardMatchingGame(CARD_COUNT)
        val (first, second) = findZeroScorePair(game)

        game.chooseCardAtIndex(first)
        game.chooseCardAtIndex(second)

        // 第一张 -1，不配对 -2，第二张 -1 → -4
        assertEquals(-4, game.score)
        assertFalse(game.cardAtIndex(first).isChosen)
        assertTrue(game.cardAtIndex(second).isChosen)
        assertFalse(game.cardAtIndex(first).isMatched)
        assertFalse(game.cardAtIndex(second).isMatched)
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun choosingCardBeyondBoardThrows() {
        CardMatchingGame(CARD_COUNT).cardAtIndex(CARD_COUNT)
    }

    /** 点数相同的一对（按抽屉原理必定存在） */
    private fun findSameRankPair(game: CardMatchingGame): Pair<Int, Int> {
        for (i in 0 until CARD_COUNT) {
            for (j in i + 1 until CARD_COUNT) {
                if (game.cardAtIndex(i).rankString == game.cardAtIndex(j).rankString) {
                    return i to j
                }
            }
        }
        throw AssertionError("16 张牌、13 个点数，必然存在点数相同的一对")
    }

    /** 点数、花色都不同的一对（得分为 0，必定存在） */
    private fun findZeroScorePair(game: CardMatchingGame): Pair<Int, Int> {
        for (i in 0 until CARD_COUNT) {
            for (j in i + 1 until CARD_COUNT) {
                if (game.cardAtIndex(i).match(arrayOf(game.cardAtIndex(j))) == 0) {
                    return i to j
                }
            }
        }
        throw AssertionError("16 张牌中必然存在点数、花色都不同的一对")
    }

    private companion object {
        const val CARD_COUNT = 16
    }
}
