package cn.edu.sicnu.cs.zjy.lab4.model

/**
 * 玩家资料的唯一数据源（Model 层）。
 *
 * 三个 Activity 自己不持有玩家数据，都从这里读写：
 * 主界面显示、信息修改界面保存、翻牌游戏界面刷新最高分。
 * 数据只在内存中，进程结束即丢失——这正是作业允许的做法。
 */
object PlayerRepository {

    @Volatile
    private var current: PlayerProfile? = null

    /** 当前玩家资料；必须先由 Application 调用 [set] 初始化 */
    val profile: PlayerProfile
        get() = current ?: error("PlayerRepository 尚未初始化")

    fun set(profile: PlayerProfile) {
        current = profile
    }

    /** 用修改后的资料替换当前资料（信息修改界面“保存”时调用） */
    fun update(newProfile: PlayerProfile) {
        current = newProfile
    }
}
