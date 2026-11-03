package cn.edu.sicnu.cs.zjy.lab5.model

/**
 * 翻牌游戏的核心逻辑（Model 层）。
 *
 * 实验五在实验四的基础上改了两处：
 * 1. 新增 [reset]：清空当前牌局、重新发牌、分数归零，供“重置游戏”按钮调用；
 * 2. 把玩法从“两张配对”改成“三张配对”：连续翻开 3 张牌才判定一次。
 *
 * 三张牌规则（自行设计）：
 * - 每翻开一张牌扣 1 分（[COST_TO_CHOOSE]）；
 * - 已翻开的牌再点一次就翻回去，不扣分；
 * - 当第 3 张牌被翻开时，若三张牌**点数全相同**（三条）得 4×[MATCH_BONUS] 分，
 *   **花色全相同**（同花）得 1×[MATCH_BONUS] 分，三张牌一起进入“已配对”状态；
 * - 若三张既不三条也不同花，扣 [MISMATCH_PENALTY] 分，并把之前翻开的 2 张全部翻回去；
 * - 已配对的牌不能再被翻开。
 *
 * 本类只负责“规则与状态”，不碰界面：界面由 CardGameActivity 读取本类状态后刷新。
 */
class CardMatchingGame(private val count: Int) {

    var score = 0
        private set

    private val cards: MutableList<Card> = mutableListOf()

    init {
        dealCards()
    }

    /** 当前牌数（RecyclerView 适配器用它决定 item 数量） */
    val cardCount: Int get() = cards.size

    /** 重新开始一局：分数归零、重新发牌、所有牌回到未翻开状态 */
    fun reset() {
        score = 0
        dealCards()
    }

    private fun dealCards() {
        val deck = Deck()
        cards.clear()
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
    /** 三张牌奖励基数：同花 = 1×8，三条 = 4×8，与一次失配（3 次翻牌 + 2 分）大致对等 */
    val MATCH_BONUS = 8
    val COST_TO_CHOOSE = 1

    fun chooseCardAtIndex(index: Int) {
        val card = cardAtIndex(index)
        if (card.isMatched) {
            return
        }
        if (card.isChosen) {
            // 再点一次已翻开的牌：翻回去，不扣分
            card.isChosen = false
            return
        }

        // 已翻开且尚未配对的牌（三张牌玩法下最多 2 张）
        val chosenCards = cards.filter { it.isChosen && !it.isMatched }
        if (chosenCards.size == MATCH_COUNT - 1) {
            val matchScore = card.match(chosenCards.toTypedArray())
            if (matchScore > 0) {
                score += matchScore * MATCH_BONUS
                chosenCards.forEach { it.isMatched = true }
                card.isMatched = true
            } else {
                score -= MISMATCH_PENALTY
                chosenCards.forEach { it.isChosen = false }
            }
        }

        score -= COST_TO_CHOOSE
        card.isChosen = true
    }

    companion object {
        /** 三张牌玩法：连续翻开 3 张才判定配对 */
        const val MATCH_COUNT = 3
    }
}
