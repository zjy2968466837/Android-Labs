package cn.edu.sicnu.cs.zjy.lab5

import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.core.net.toUri
import cn.edu.sicnu.cs.zjy.lab5.model.Gender
import cn.edu.sicnu.cs.zjy.lab5.model.Hobby

/** 性别枚举 → 字符串资源：界面不直接写“男/女”，一律走 strings.xml */
@StringRes
fun Gender.labelRes(): Int = when (this) {
    Gender.MALE -> R.string.gender_male
    Gender.FEMALE -> R.string.gender_female
}

/** 爱好枚举 → 字符串资源 */
@StringRes
fun Hobby.labelRes(): Int = when (this) {
    Hobby.TRAVEL -> R.string.hobby_travel
    Hobby.READING -> R.string.hobby_reading
    Hobby.GAME -> R.string.hobby_game
    Hobby.SHOPPING -> R.string.hobby_shopping
}

/**
 * 显示头像：没有选过头像就显示内置占位图；
 * 选的图片读不到（例如进程重启后 content:// 权限失效）时也回退到占位图，避免崩溃。
 */
fun ImageView.showAvatar(uriString: String?) {
    if (uriString.isNullOrBlank()) {
        setImageResource(R.drawable.ic_avatar_placeholder)
        return
    }
    runCatching { setImageURI(uriString.toUri()) }
        .onFailure { setImageResource(R.drawable.ic_avatar_placeholder) }
}
