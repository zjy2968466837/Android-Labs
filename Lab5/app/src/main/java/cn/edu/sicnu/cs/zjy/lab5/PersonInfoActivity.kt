package cn.edu.sicnu.cs.zjy.lab5

import android.content.ActivityNotFoundException
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import cn.edu.sicnu.cs.zjy.lab5.databinding.ActivityPersonInfoBinding
import cn.edu.sicnu.cs.zjy.lab5.model.Gender
import cn.edu.sicnu.cs.zjy.lab5.model.Hobby
import cn.edu.sicnu.cs.zjy.lab5.model.PlayerProfile
import cn.edu.sicnu.cs.zjy.lab5.model.PlayerRepository

/**
 * 二级 Activity① / Controller：信息修改。
 *
 * 打开时把当前资料填进控件；“保存”把新值写回 [PlayerRepository] 并返回，
 * “取消”或系统返回键则直接结束、不改动任何数据。
 * 头像通过系统图片选择器（Photo Picker，无需申请存储/相机权限）更换。
 */
class PersonInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonInfoBinding

    /** 本次编辑选中的头像地址；点“保存”前不会写回 Model */
    private var pickedAvatarUri: String? = null

    private val pickAvatar =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                pickedAvatarUri = uri.toString()
                binding.ivAvatar.setImageURI(uri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPersonInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applyEdgeToEdgeInsets(binding.contentEdit)

        fillForm(PlayerRepository.profile)

        binding.btnPickAvatar.setOnClickListener { openImagePicker() }
        binding.ivAvatar.setOnClickListener { openImagePicker() }
        binding.btnSave.setOnClickListener { saveAndFinish() }
        binding.btnCancel.setOnClickListener { finish() }
    }

    /** 用当前资料填充控件 */
    private fun fillForm(profile: PlayerProfile) = with(binding) {
        etName.setText(profile.name)
        etStudentId.setText(profile.studentId)
        etPhone.setText(profile.phone)
        etEmail.setText(profile.email)

        if (profile.gender == Gender.FEMALE) rbFemale.isChecked = true else rbMale.isChecked = true

        cbTravel.isChecked = Hobby.TRAVEL in profile.hobbies
        cbReading.isChecked = Hobby.READING in profile.hobbies
        cbGame.isChecked = Hobby.GAME in profile.hobbies
        cbShopping.isChecked = Hobby.SHOPPING in profile.hobbies

        pickedAvatarUri = profile.avatarUri
        ivAvatar.showAvatar(pickedAvatarUri)
    }

    /** 校验后把控件里的值写回内存中的玩家资料 */
    private fun saveAndFinish() {
        val name = binding.etName.text.toString().trim()
        if (name.isEmpty()) {
            Toast.makeText(this, R.string.toast_name_required, Toast.LENGTH_SHORT).show()
            return
        }

        // 作业允许不保存到文件，这里直接更新内存里的同一个 PlayerProfile 实例
        val profile = PlayerRepository.profile
        profile.name = name
        profile.studentId = binding.etStudentId.text.toString().trim()
        profile.phone = binding.etPhone.text.toString().trim()
        profile.email = binding.etEmail.text.toString().trim()
        profile.gender = if (binding.rbFemale.isChecked) Gender.FEMALE else Gender.MALE

        // 按枚举顺序重建爱好集合，主界面显示时顺序稳定
        profile.hobbies.clear()
        if (binding.cbTravel.isChecked) profile.hobbies.add(Hobby.TRAVEL)
        if (binding.cbReading.isChecked) profile.hobbies.add(Hobby.READING)
        if (binding.cbGame.isChecked) profile.hobbies.add(Hobby.GAME)
        if (binding.cbShopping.isChecked) profile.hobbies.add(Hobby.SHOPPING)

        profile.avatarUri = pickedAvatarUri
        PlayerRepository.update(profile)

        finish()
    }

    private fun openImagePicker() {
        try {
            pickAvatar.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, R.string.toast_no_image_picker, Toast.LENGTH_SHORT).show()
        }
    }
}
