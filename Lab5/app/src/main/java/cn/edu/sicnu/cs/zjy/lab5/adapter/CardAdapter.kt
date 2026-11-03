package cn.edu.sicnu.cs.zjy.lab5.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.edu.sicnu.cs.zjy.lab5.R
import cn.edu.sicnu.cs.zjy.lab5.model.Card
import cn.edu.sicnu.cs.zjy.lab5.model.CardMatchingGame
import cn.edu.sicnu.cs.zjy.lab5.view.CardView

/**
 * 翻牌游戏的 RecyclerView 适配器（实验五要求 (b)）。
 *
 * 职责：把 Model 里的每一张牌绑定到一个 [CardView] 上，并把点击事件通过
 * [OnCardClickListener] 回调给控制器（CardGameActivity）。适配器自己不改游戏状态。
 */
class CardAdapter(
    private val listener: OnCardClickListener
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    /** 点击回调：由 Activity 实现，收到的是被点击牌在牌阵中的下标 */
    interface OnCardClickListener {
        fun onCardClicked(position: Int)
    }

    private var game: CardMatchingGame? = null

    /** 换一局（初始化或重置时调用），整块刷新 */
    fun submitGame(game: CardMatchingGame) {
        this.game = game
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = game?.cardCount ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentGame = game ?: return
        holder.bind(currentGame.cardAtIndex(position), position)
    }

    /** 一个格子对应一张牌：绑定 Model 状态并处理点击回调 */
    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val cardView: CardView = itemView as CardView

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onCardClicked(position)
                }
            }
        }

        fun bind(card: Card, position: Int) {
            cardView.bind(card, position)
        }
    }
}
