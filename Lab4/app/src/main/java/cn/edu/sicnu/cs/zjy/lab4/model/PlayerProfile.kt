package cn.edu.sicnu.cs.zjy.lab4.model

/** 性别（Model 层枚举，界面文字由 strings.xml 提供） */
enum class Gender { MALE, FEMALE }

/** 爱好（Model 层枚举，界面文字由 strings.xml 提供） */
enum class Hobby { TRAVEL, READING, GAME, SHOPPING }

/**
 * 当前玩家的个人信息（Model 层）。
 *
 * 作业要求“修改后的信息无需保存到文件中”，所以整份资料只驻留在内存里：
 * 主界面、信息修改界面、翻牌游戏界面通过 [PlayerRepository] 共享同一个实例。
 */
class PlayerProfile(
    var name: String = "",
    var gender: Gender = Gender.MALE,
    var studentId: String = "",
    var phone: String = "",
    var email: String = "",
    val hobbies: MutableSet<Hobby> = mutableSetOf(),
    /** 头像图片的 content:// 地址，null 表示使用默认占位头像 */
    var avatarUri: String? = null,
    /** 游戏最高分：由翻牌游戏界面在得分超过它时更新 */
    var highScore: Int = 0
)
