package cn.edu.sicnu.cs.zjy.lab4.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import cn.edu.sicnu.cs.zjy.lab4.R
import cn.edu.sicnu.cs.zjy.lab4.databinding.ViewCardBinding
import cn.edu.sicnu.cs.zjy.lab4.model.Card

/**
 * 一张扑克牌的 View（MVC 里的 V）。
 *
 * 只负责把 [Card] 的当前状态画出来，不含任何游戏规则：
 * - 未翻开 → 紫色牌背 + 问号；
 * - 已翻开 → 白底 + 左上角点数 + 中央花色，红桃/方块用红色；
 * - 等待配对 → 琥珀色粗边框；已配对 → 整体半透明。
 *
 * 点击事件由控制器（CardGameActivity）设置，本类不处理游戏逻辑。
 */
class CardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    // view_card.xml 的根是 <merge>，生成的 inflate 只有 (inflater, parent) 两个参数
    private val binding = ViewCardBinding.inflate(LayoutInflater.from(context), this)

    init {
        isClickable = true
        isFocusable = true
    }

    /**
     * 根据 Model 状态刷新牌面。
     *
     * @param card     模型里的那张牌
     * @param position 在牌阵中的下标（从 0 开始），用于生成无障碍描述
     */
    fun bind(card: Card, position: Int) {
        val faceUp = card.isChosen || card.isMatched

        binding.cardBack.visibility = if (faceUp) View.GONE else View.VISIBLE
        binding.cardFace.visibility = if (faceUp) View.VISIBLE else View.GONE

        if (faceUp) {
            val colorRes = if (card.isRedSuit) R.color.card_rank_red else R.color.card_rank_black
            val textColor = ContextCompat.getColor(context, colorRes)
            binding.tvRank.text = card.rankString
            binding.tvRank.setTextColor(textColor)
            binding.tvSuit.text = card.suitString
            binding.tvSuit.setTextColor(textColor)

            binding.cardFace.setBackgroundResource(
                if (card.isChosen) R.drawable.bg_card_face_selected else R.drawable.bg_card_face
            )
        }

        // 已配对的牌淡出，但仍占位，保持牌阵整齐
        alpha = if (card.isMatched) MATCHED_ALPHA else 1f

        contentDescription = when {
            card.isMatched -> context.getString(
                R.string.card_desc_matched, position + 1, card.toString()
            )
            faceUp -> context.getString(
                R.string.card_desc_front, position + 1, card.toString()
            )
            else -> context.getString(R.string.card_desc_back, position + 1)
        }
    }

    private companion object {
        const val MATCHED_ALPHA = 0.45f
    }
}
