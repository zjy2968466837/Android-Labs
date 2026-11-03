package cn.edu.sicnu.cs.zjy.lab4

import android.os.Bundle
import android.widget.GridLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import cn.edu.sicnu.cs.zjy.lab4.databinding.ActivityCardGameBinding
import cn.edu.sicnu.cs.zjy.lab4.model.CardMatchingGame
import cn.edu.sicnu.cs.zjy.lab4.model.PlayerRepository
import cn.edu.sicnu.cs.zjy.lab4.view.CardView

/**
 * 二级 Activity② / Controller：翻牌游戏。
 *
 * MVC 分工：
 * - Model：[CardMatchingGame]（含 [cn.edu.sicnu.cs.zjy.lab4.model.Card]、[cn.edu.sicnu.cs.zjy.lab4.model.Deck]）只算分数和配对状态；
 * - View：4×4 共 [CARD_COUNT] 个 [CardView]，只按 Model 状态画牌；
 * - Controller：本类接收点击、调用 `chooseCardAtIndex()`、再把结果同步给所有 CardView 和得分。
 *
 * 点“重置游戏”会用一副新牌重新开始（分数归零，最高分保留）。
 */
class CardGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCardGameBinding

    private lateinit var game: CardMatchingGame
    private val cardViews = mutableListOf<CardView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCardGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applyEdgeToEdgeInsets(binding.contentGame)

        buildCardGrid()

        binding.btnResetGame.setOnClickListener { startNewGame() }
        binding.btnExit.setOnClickListener { finish() }

        startNewGame()
    }

    /**
     * 创建 16 张牌的 View，并放进 4×4 的 GridLayout。
     * 每张牌的宽高都设为 0 + 权重 1，由 GridLayout 平分格子，保证牌阵规整、随屏宽自适应。
     */
    private fun buildCardGrid() {
        val margin = resources.getDimensionPixelSize(R.dimen.card_margin)
        for (index in 0 until CARD_COUNT) {
            val cardView = CardView(this)
            cardView.layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = 0
                rowSpec = GridLayout.spec(index / GRID_COLUMNS, 1f)
                columnSpec = GridLayout.spec(index % GRID_COLUMNS, 1f)
                setMargins(margin, margin, margin, margin)
            }
            cardView.setOnClickListener { onCardClicked(index) }
            binding.gridCards.addView(cardView)
            cardViews.add(cardView)
        }
    }

    /** 重新开一局：换一副新牌，分数归零 */
    private fun startNewGame() {
        game = CardMatchingGame(CARD_COUNT)
        renderGame()
    }

    private fun onCardClicked(index: Int) {
        game.chooseCardAtIndex(index)
        renderGame()
        updateHighScore()
    }

    /** 把 Model 的最新状态同步到界面：每张牌 + 得分 */
    private fun renderGame() {
        cardViews.forEachIndexed { index, cardView ->
            cardView.bind(game.cardAtIndex(index), index)
        }
        binding.tvScore.text = getString(R.string.score_format, game.score)
    }

    /** 破纪录时更新内存中的最高分，主界面返回后即可看到 */
    private fun updateHighScore() {
        val profile = PlayerRepository.profile
        if (game.score > profile.highScore) {
            profile.highScore = game.score
        }
    }

    private companion object {
        const val CARD_COUNT = 16
        const val GRID_COLUMNS = 4
    }
}
