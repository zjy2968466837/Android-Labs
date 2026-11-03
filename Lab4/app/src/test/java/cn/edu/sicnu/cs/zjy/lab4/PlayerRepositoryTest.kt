package cn.edu.sicnu.cs.zjy.lab4

import cn.edu.sicnu.cs.zjy.lab4.model.Gender
import cn.edu.sicnu.cs.zjy.lab4.model.Hobby
import cn.edu.sicnu.cs.zjy.lab4.model.PlayerProfile
import cn.edu.sicnu.cs.zjy.lab4.model.PlayerRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

/** Model 单元测试：玩家资料的内存仓库，三个 Activity 共享同一份数据。 */
class PlayerRepositoryTest {

    @Test
    fun setMakesProfileAvailable() {
        val profile = PlayerProfile(name = "张三", gender = Gender.MALE)

        PlayerRepository.set(profile)

        assertSame(profile, PlayerRepository.profile)
    }

    @Test
    fun updateReplacesWholeProfile() {
        val first = PlayerProfile(name = "张三")
        val second = PlayerProfile(
            name = "李四",
            gender = Gender.FEMALE,
            studentId = "2023080544",
            hobbies = mutableSetOf(Hobby.TRAVEL, Hobby.SHOPPING),
            highScore = 42
        )

        PlayerRepository.set(first)
        PlayerRepository.update(second)

        assertSame(second, PlayerRepository.profile)
        assertEquals("李四", PlayerRepository.profile.name)
        assertEquals(Gender.FEMALE, PlayerRepository.profile.gender)
        assertEquals(setOf(Hobby.TRAVEL, Hobby.SHOPPING), PlayerRepository.profile.hobbies)
        assertEquals(42, PlayerRepository.profile.highScore)
    }
}
