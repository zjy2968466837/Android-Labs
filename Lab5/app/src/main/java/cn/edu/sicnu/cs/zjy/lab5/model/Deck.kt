package cn.edu.sicnu.cs.zjy.lab5.model

import java.util.Random

/**
 * 一副 52 张的扑克牌（Model 层，取自教师附件 model.zip）。
 *
 * 每次 [drawRandomCard] 从剩余的牌里随机抽一张并移除，保证一局游戏里不会出现重复的牌。
 * 抽完后再抽返回 null。
 */
class Deck {
    private val cards = mutableListOf<Card>()
    private val r = Random()

    init {
        for (suit in Card.validSuits) {
            for (rank in Card.rankStrings) {
                val card = Card(suit = suit, rank = rank)
                cards.add(card)
            }
        }
    }

    /** 剩余的牌数（单元测试用） */
    val size: Int get() = cards.size

    fun drawRandomCard(): Card? {
        var randomCard: Card? = null
        if (cards.size > 0) {
            randomCard = cards.removeAt(r.nextInt(cards.size))
        }
        return randomCard
    }
}
