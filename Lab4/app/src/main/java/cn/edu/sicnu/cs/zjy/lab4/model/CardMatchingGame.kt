package cn.edu.sicnu.cs.zjy.lab4.model

/**
 * 翻牌游戏的核心逻辑（Model 层，取自教师附件 model.zip）。
 *
 * 规则（附件原样）：
 * - 每翻开一张牌先扣 1 分（[COST_TO_CHOOSE]）；
 * - 已翻开的牌再点一次就翻回去，不扣分；
 * - 新翻开的牌若与已翻开的牌点数相同，得 4×[MATCH_BONUS] 分并双双标记为已配对；
 * - 若花色相同，得 1×[MATCH_BONUS] 分并配对；否则扣 [MISMATCH_PENALTY] 分并把对方翻回去；
 * - 已配对的牌不能再被翻开。
 *
 * 本类只负责“规则与状态”，不碰任何界面：界面由 CardGameActivity 读取本类状态后刷新。
 */
class CardMatchingGame(count: Int) {

    var score = 0
        private set

    private val cards: MutableList<Card>

    init {
        val deck = Deck()
        cards = mutableListOf()
        for (i in 1..count) {
            val card: Card? = deck.drawRandomCard()
            if (card != null) {
                cards.add(card)
            }
        }
    }

    fun cardAtIndex(index: Int): Card {
        return cards.get(index)
    }

    val MISMATCH_PENALTY = 2
    val MATCH_BONUS = 4
    val COST_TO_CHOOSE = 1

    fun chooseCardAtIndex(index: Int) {
        val card = cardAtIndex(index)
        if (!card.isMatched) {
            if (card.isChosen) {
                card.isChosen = false
            } else {
                for (otherCard in cards) {
                    if (otherCard.isChosen && !otherCard.isMatched) {
                        val matchScore = card.match(arrayOf(otherCard))
                        if (matchScore > 0) {
                            score += matchScore * MATCH_BONUS
                            otherCard.isMatched = true
                            card.isMatched = true
                        } else {
                            score -= MISMATCH_PENALTY
                            otherCard.isChosen = false
                        }
                        break
                    }
                }
                score -= COST_TO_CHOOSE
                card.isChosen = true
            }
        }
    }
}
