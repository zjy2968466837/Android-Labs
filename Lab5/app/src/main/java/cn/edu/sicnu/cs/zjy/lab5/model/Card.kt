package cn.edu.sicnu.cs.zjy.lab5.model

/**
 * 一张扑克牌（Model 层，不依赖任何 Android 类，可直接用 JVM 单元测试）。
 *
 * 代码来自实验四，本实验把 [match] 扩展成支持“三张牌”的判断：
 * - 传入 1 张牌时与实验四完全一致：点数相同得 4 分，花色相同得 1 分；
 * - 传入 2 张牌时（三张牌玩法）：三张点数全相同得 4 分，花色全相同得 1 分，
 *   否则 0 分。
 */
class Card(
    private var suit: String,
    private var rank: String,
    var isChosen: Boolean = false,
    var isMatched: Boolean = false
) {
    companion object {
        val rankStrings = arrayOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
        val validSuits = arrayOf("♥", "♦", "♠", "♣")
    }

    /** 点数，如 "A"、"10"、"K" */
    val rankString: String get() = rank

    /** 花色，如 "♥"、"♠" */
    val suitString: String get() = suit

    /** 红桃、方块用红色显示，其余用黑色 */
    val isRedSuit: Boolean get() = suit == "♥" || suit == "♦"

    override fun toString(): String {
        return suit + rank
    }

    /**
     * 判断本牌与 [otherCards] 是否配对，返回配对得分：
     * - 点数全部相同 → 4 分（三张牌玩法下即“三条”）；
     * - 花色全部相同 → 1 分（即“同花”）；
     * - 否则 → 0 分。
     *
     * @param otherCards 已经翻开、等待配对的牌（三张牌玩法最多 2 张）
     */
    fun match(otherCards: Array<Card>): Int {
        if (otherCards.isEmpty()) return 0
        if (otherCards.all { it.rank == rank }) return 4
        if (otherCards.all { it.suit == suit }) return 1
        return 0
    }
}
