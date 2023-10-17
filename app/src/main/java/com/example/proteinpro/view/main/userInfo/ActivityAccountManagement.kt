package com.example.proteinpro.view.main.userInfo

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.proteinpro.R
import com.example.proteinpro.databinding.ActivityAccountManagementBinding
import com.example.proteinpro.util.Class.User
import com.example.proteinpro.util.Class.WalletDataItem
import com.example.proteinpro.util.PreferenceHelper
import com.example.proteinpro.util.Retrofit.RetrofitHelper
import com.example.proteinpro.view.ActivityLogin
import com.example.proteinpro.view.user.signup.ActivityBirthInput
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
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
        initData()


    }

    private fun initData() {

        val token = preferenceHelper.get_jwt_Token()

    }

    private fun initViews(){

        Log.i ("로그인 정보", "preferenceHelper.getUser()?.type"+ preferenceHelper.getUser()?.type)

        var loginType : String
        if(preferenceHelper.getUser()?.type == 1){// 일반로그인
            loginType = "일반 로그인"
            binding.changePasswordBTN.visibility = View.VISIBLE
        }else if(preferenceHelper.getUser()?.type == 2){// 카카오 로그인
            loginType = "카카오 로그인"
            binding.changePasswordBTN.visibility = View.GONE
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


            if(preferenceHelper.getUser()?.type == 1){// 일반로그인

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
                        Toast.makeText(getApplicationContext(), "비밀번호가 입력되지 않았습니다.",Toast.LENGTH_SHORT).show()

                    }
                }

            }else if(preferenceHelper.getUser()?.type == 2){// 카카오 로그인

                checkKakao()


            }else{

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

    fun checkKakao(){

        // 로그인 조합 예제

// 카카오계정으로 로그인 공통 callback 구성
// 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(ContentValues.TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")

                UserApiClient.instance.me{ user, error ->
                    if (error != null) {
                        Log.e(ContentValues.TAG, "사용자 정보 요청 실패", error)

                        Toast.makeText(getApplicationContext(), "카카오 인증에 실패 했습니다.",Toast.LENGTH_SHORT).show()

                    }
                    else if (user != null) {
                        Log.i(
                            ContentValues.TAG, "사용자 정보 요청 성공" +
                                "\n회원번호: ${user.id}" +
                                "\n이메일: ${user.kakaoAccount?.email}" +
                                "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")


                        retrofitHelper.userWithdraw("", preferenceHelper.get_jwt_Token().toString()){

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

                    }

                }
            }
        }

// 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e(ContentValues.TAG, "카카오톡으로 로그인 실패", error)
                    Toast.makeText(getApplicationContext(), "카카오 인증에 실패 했습니다.",Toast.LENGTH_SHORT).show()
                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                    Log.i(ContentValues.TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    UserApiClient.instance.me{ user, error ->
                        if (error != null) {
                            Log.e(ContentValues.TAG, "사용자 정보 요청 실패", error)
                        }
                        else if (user != null) {
                            Log.i(
                                ContentValues.TAG, "사용자 정보 요청 성공" +
                                    "\n회원번호: ${user.id}" +
                                    "\n이메일: ${user.kakaoAccount?.email}" +
                                    "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                    "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")



                        }
                    }
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }

    }



}