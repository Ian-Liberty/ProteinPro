package com.example.proteinpro.view.main.userInfo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.proteinpro.R
import com.example.proteinpro.databinding.ActivityAccountManagementBinding
import com.example.proteinpro.util.PreferenceHelper
import com.example.proteinpro.util.Retrofit.RetrofitHelper
import com.example.proteinpro.view.ActivityLogin
import kotlin.math.log

class ActivityAccountManagement : AppCompatActivity() {

    // 전역 변수로 바인딩 객체 선언
    private var mBinding:
            ActivityAccountManagementBinding? =null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!
    //!!는 Kotlin에서 Nullable 타입을 강제로 Non-nullable 타입으로 변환하는 것을 의미

    //utillclass
    private lateinit var preferenceHelper: PreferenceHelper
    private lateinit var retrofitHelper: RetrofitHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_account_management)

        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용해서
        // 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivityAccountManagementBinding.inflate(layoutInflater)
        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의
        // 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시 합니다.
        setContentView(binding.root)// < 기존의 setContentView 는 주석 처리해 주세요!

        initUtils()
        initViews()
        initListener()


    }

    private fun initViews(){

        var loginType : String
        if(preferenceHelper.getUser()?.type == 1){// 일반로그인
            loginType = "일반 로그인"
        }else if(preferenceHelper.getUser()?.type == 2){// 카카오 로그인
            loginType = "카카오 로그인"
        }else{
            loginType = ""
        }

        binding.loginTypeET.setText(loginType)

        binding.userNicknameET.setText(preferenceHelper.getUser()?.nickname)

    }

    private fun initListener(){
        // 리스너 초기화
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
        // 비밀번호 변경
        binding.changePasswordBTN.setOnClickListener {

            val mIntent = Intent(this, ActivityChangePassword::class.java)

            startActivity(mIntent)

        }

        // 로그아웃
        binding.logOutBTN.setOnClickListener {
            preferenceHelper.logout()

            val mIntent = Intent(this, ActivityLogin::class.java)

            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(mIntent)

        }

        //회원 탈퇴
        binding.withdrawalBTN.setOnClickListener {

            showWithdrawDialog(this) { userInput ->
                if (userInput.isNotEmpty()) {
                    // 비밀번호가 입력되었을 때의 처리
                    // 예: 비밀번호 검증, 탈퇴 로직 실행 등
                    // 탈퇴 로직 처리

                    retrofitHelper.userWithdraw(userInput, preferenceHelper.get_jwt_Token().toString()){

                        if(it){
                            preferenceHelper.logout()

                            val mIntent = Intent(this, ActivityLogin::class.java)

                            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(mIntent)
                            Toast.makeText(getApplicationContext(), "정상적으로 회원 탈퇴 처리 되었습니다.",Toast.LENGTH_SHORT).show()
                        }else{
                            showInvalidPasswordDialog(this)
                        }

                    }
                } else {
                    // 비밀번호가 입력되지 않았을 때의 처리
                    // 예: 사용자에게 경고 메시지 표시


                }
            }


        }

    }

    private fun initUtils(){
        // 유틸 클래스 초기화

        preferenceHelper = PreferenceHelper(this)
        retrofitHelper = RetrofitHelper(this)

    }

    fun showWithdrawDialog(context: Context, callback: (String) -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("탈퇴하기")
        val input = EditText(context)
        input.hint = "비밀번호를 입력하세요"
        builder.setView(input)

        // 초기에는 확인 버튼 비활성화
        val dialog = builder.setPositiveButton("확인", null).create()

        // 입력값이 변경될 때마다 호출되는 리스너
        input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val userInput = input.text.toString()
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = userInput.isNotEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "확인") { _, _ ->
            val userInput = input.text.toString()
            callback(userInput)
        }

        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "취소") { dialog, _ ->
            dialog.cancel()
        }

        dialog.show()
    }

    fun showInvalidPasswordDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("오류")
        builder.setMessage("비밀번호가 올바르지 않습니다.")
        builder.setPositiveButton("확인") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }



}