package com.example.proteinpro.view.main.userInfo
import android.content.Intent
import android.net.Uri
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationManagerCompat
import com.example.proteinpro.R
import com.example.proteinpro.databinding.ActivityNotificationSettingBinding

class ActivityNotificationSetting : AppCompatActivity() {

    // 전역 변수로 바인딩 객체 선언
    private var mBinding:
            ActivityNotificationSettingBinding? =null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!
    //!!는 Kotlin에서 Nullable 타입을 강제로 Non-nullable 타입으로 변환하는 것을 의미

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_notification_setting)

        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용해서
        // 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivityNotificationSettingBinding.inflate(layoutInflater)
        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의
        // 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시 합니다.
        setContentView(binding.root)// < 기존의 setContentView 는 주석 처리해 주세요!

        initViews()
        initUtils()
        initListener()

    }

    override fun onResume() {
        super.onResume()
        if(NotificationManagerCompat.from(this).areNotificationsEnabled()){
            Log.i ("NotificationManagerCompat", "NotificationManagerCompat.from(this).areNotificationsEnabled()"+NotificationManagerCompat.from(this).areNotificationsEnabled())

            binding.notificationOffLL.visibility = View.GONE
            binding.notificationSwitch.isChecked = true

        }else{

            Log.i ("NotificationManagerCompat", "NotificationManagerCompat.from(this).areNotificationsEnabled()"+NotificationManagerCompat.from(this).areNotificationsEnabled())
            binding.notificationOffLL.visibility = View.VISIBLE
            binding.notificationSwitch.isChecked = false

        }
    }
    private fun initViews(){


    }

    private fun initListener(){
        // 리스너 초기화
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        binding.notificationSwitch.setOnClickListener{

            if(NotificationManagerCompat.from(this).areNotificationsEnabled() == false){
                binding.notificationSwitch.isChecked = false
                val message = "알림 권한이 현재 비활성화되어 있습니다.\n앱의 설정으로 이동하여 권한을 변경할 수 있습니다."
                val alertDialog = AlertDialog.Builder(this)
                    .setTitle("알림")
                    .setMessage(message)
                    .setPositiveButton("설정으로 이동") { _, _ ->
                        // 앱 설정 화면으로 이동하는 코드를 작성합니다.
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                    .setNegativeButton("취소", null)
                    .create()

                alertDialog.show()
            }else{
                binding.notificationSwitch.isChecked = true
                val message = "앱의 설정으로 이동하여 권한을 변경할 수 있습니다."
                val alertDialog = AlertDialog.Builder(this)
                    .setTitle("알림")
                    .setMessage(message)
                    .setPositiveButton("설정으로 이동") { _, _ ->
                        // 앱 설정 화면으로 이동하는 코드를 작성합니다.
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                    .setNegativeButton("취소", null)
                    .create()

                alertDialog.show()

            }

        }

        binding.permissionBTN.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }

    }
    private fun initUtils(){
        // 유틸 클래스 초기화

    }


}