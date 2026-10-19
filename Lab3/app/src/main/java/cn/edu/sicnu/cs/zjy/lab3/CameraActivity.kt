package cn.edu.sicnu.cs.zjy.lab3

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cn.edu.sicnu.cs.zjy.lab3.databinding.ActivityCameraBinding

/**
 * 二级 Activity ③：采用「隐式 Intent」调用系统相机拍照，拍完在本界面显示照片。
 *
 * 参考附件《采用隐式Intent调用相机拍照功能》：
 * 1) 清单文件里声明 CAMERA 权限；
 * 2) 布局需要 ImageView + Button；
 * 3) 代码里隐式调用相机并处理返回结果（缩略图放在返回 Intent 的 extras["data"] 中）。
 */
@Suppress("DEPRECATION", "OVERRIDE_DEPRECATION")
class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applyEdgeToEdgeInsets(binding.contentCamera)

        binding.btnTakePhoto.setOnClickListener {
            // 运行时权限：已授权则直接拍照，否则先申请 CAMERA 权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED
            ) {
                dispatchTakePictureIntent()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA_PERMISSION
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {
                Toast.makeText(this, R.string.toast_camera_permission_denied, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /** 隐式 Intent：只声明动作 ACTION_IMAGE_CAPTURE，由系统匹配相机应用来处理 */
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // resolveActivity 做可用性检查；Android 11+ 需配合清单里的 <queries> 才不会返回 null
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } else {
            Toast.makeText(this, R.string.toast_no_camera_app, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // 系统相机把拍好的缩略图放在返回 Intent 的 extras["data"] 里
            val imageBitmap = data?.extras?.get(EXTRA_DATA_KEY) as? Bitmap
            if (imageBitmap != null) {
                binding.ivPhoto.setImageBitmap(imageBitmap)
            }
        }
    }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val REQUEST_CAMERA_PERMISSION = 2

        /** 相机返回缩略图时 extras 中的固定 Key */
        private const val EXTRA_DATA_KEY = "data"
    }
}
