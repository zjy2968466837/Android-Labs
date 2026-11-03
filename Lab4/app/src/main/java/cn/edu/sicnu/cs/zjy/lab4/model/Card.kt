package cn.edu.sicnu.cs.zjy.lab4.model

/**
 * 一张扑克牌（Model 层，不依赖任何 Android 类，可直接用 JVM 单元测试）。
 *
 * 代码取自教师附件 model.zip，只补充了 [rankString] / [suitString] / [isRedSuit]
 * 三个只读属性：界面（View）需要显示花色和点数，而原实现里这两个字段是 private。
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
     * 与另一张牌比较：点数相同得 4 分，花色相同得 1 分，都不同得 0 分。
     * 一次只比较一张牌（附件原实现即如此）。
     */
    fun match(otherCards: Array<Card>): Int {
        var score = 0
        if (otherCards.size == 1) {
            val otherCard = otherCards[0]
            if (otherCard.rank == rank) {
                score = 4
            } else if (otherCard.suit == suit) {
                score = 1
            }
        }
        return score
    }
}
