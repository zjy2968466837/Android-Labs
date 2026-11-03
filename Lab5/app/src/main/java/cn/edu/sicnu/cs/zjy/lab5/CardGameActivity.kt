package cn.edu.sicnu.cs.zjy.lab5

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import cn.edu.sicnu.cs.zjy.lab5.adapter.CardAdapter
import cn.edu.sicnu.cs.zjy.lab5.databinding.ActivityCardGameBinding
import cn.edu.sicnu.cs.zjy.lab5.model.CardMatchingGame
import cn.edu.sicnu.cs.zjy.lab5.model.PlayerRepository

/**
 * 二级 Activity② / Controller：翻牌游戏（实验五改用 RecyclerView）。
 *
 * MVC 分工：
 * - Model：[CardMatchingGame] 只算分数与配对状态（三张牌玩法，见 model 层注释）；
 * - View：[cn.edu.sicnu.cs.zjy.lab5.view.CardView] 只按 Model 状态画一张牌；
 * - Adapter：[CardAdapter] 负责把 Model 的牌绑到 RecyclerView 的格子上，并通过
 *   [CardAdapter.OnCardClickListener] 把点击回调给本类；
 * - Controller：本类接收回调、调用 `chooseCardAtIndex()`，再刷新列表与得分。
 *
 * 牌数与列数都从资源读取：竖屏 24 张 / 4 列，横屏 18 张 / 6 列
 * （`values/integers.xml` 与 `values-land/integers.xml`），旋转屏幕后自动重建为新牌局。
 */
class CardGameActivity : AppCompatActivity(), CardAdapter.OnCardClickListener {

    private lateinit var binding: ActivityCardGameBinding
    private lateinit var game: CardMatchingGame
    private lateinit var adapter: CardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCardGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applyEdgeToEdgeInsets(binding.contentGame)

        // 列数来自资源：竖屏 4 列、横屏 6 列
        val spanCount = resources.getInteger(R.integer.card_span_count)
        binding.rvCards.layoutManager = GridLayoutManager(this, spanCount)
        adapter = CardAdapter(this)
        binding.rvCards.adapter = adapter

        binding.btnResetGame.setOnClickListener { resetGame() }
        binding.btnExit.setOnClickListener { finish() }

        startNewGame()
    }

    /** 开一局：牌数来自资源（竖屏 24、横屏 18） */
    private fun startNewGame() {
        game = CardMatchingGame(resources.getInteger(R.integer.card_count))
        adapter.submitGame(game)
        renderScore()
    }

    /** “重置游戏”：直接复用 Model 的 reset()，重新发牌、分数归零 */
    private fun resetGame() {
        game.reset()
        adapter.submitGame(game)
        renderScore()
    }

    /** 适配器的点击回调：交给 Model 处理，再同步界面 */
    override fun onCardClicked(position: Int) {
        game.chooseCardAtIndex(position)
        // 一次点击可能同时改变 3 张牌的状态，整块刷新最稳妥
        adapter.notifyDataSetChanged()
        renderScore()
        updateHighScore()
    }

    private fun renderScore() {
        binding.tvScore.text = getString(R.string.score_format, game.score)
    }

    /** 破纪录时更新内存中的最高分，主界面返回后即可看到 */
    private fun updateHighScore() {
        val profile = PlayerRepository.profile
        if (game.score > profile.highScore) {
            profile.highScore = game.score
        }
    }
}
