package com.example.proteinpro

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.proteinpro.databinding.ActivityLoginBinding
import com.example.proteinpro.user.ActivityFindPassword_InputEmail
import com.example.proteinpro.user.signup.ActivityBirthInput
import com.example.proteinpro.util.PreferenceHelper
import com.example.proteinpro.util.Retrofit.ApiClient
import com.example.proteinpro.util.Retrofit.UserDataInterface
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 시작 페이지
// 로그인 및 회원가입 비밀번호 찾기 등 접근 가능
//
class ActivityLogin : AppCompatActivity() {

    private lateinit var preferenceHelper: PreferenceHelper

    // 인텐트 변수
    private lateinit var login_btn: Button
    private lateinit var kakaoLogin_btn: Button

    private lateinit var signUp_tv: TextView
    private lateinit var findPassword_tv: TextView
    private lateinit var email_et : EditText
    private lateinit var password_et: EditText


    // 전역 변수로 바인딩 객체 선언
    private var mBinding: ActivityLoginBinding? =null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!
    //!!는 Kotlin에서 Nullable 타입을 강제로 Non-nullable 타입으로 변환하는 것을 의미

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i ("ActivityLogin", "onCreate")
        // 기존 setContentView 를 제거해주시고..
//        setContentView(R.layout.activity_login)

        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용해서
        // 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivityLoginBinding.inflate(layoutInflater)

        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의
        // 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시 합니다.
        setContentView(binding.root)

        // 프리퍼런스 헬퍼 선언
        preferenceHelper = PreferenceHelper(this)

        initViews()
        initListener()

    }

    private fun initViews(){
        // 뷰 초기화

        login_btn = binding.loginBTN
        kakaoLogin_btn = binding.kakaoLoginBTN
        signUp_tv = binding.signUpTV
        findPassword_tv = binding.findPasswordTV

        email_et = binding.emailET
        password_et = binding.passwordET

    }
    private fun initListener(){
        // 리스너 초기화

        // 로그인 버튼 클릭 이벤트
        login_btn.setOnClickListener {
            Log.i ("loginBTN", "로그인 버튼")

            login(this, email_et.text.toString(), password_et.text.toString())

        }

        kakaoLogin_btn.setOnClickListener{
            Log.i ("kakaoLoginBTN", "카카오 로그인 버튼")
            // 카카오 로그인 버튼 클릭 이벤트
        }

        signUp_tv.setOnClickListener{
            Log.i ("signUpTV", "회원가입 클릭")
            val mIntent = Intent(this, ActivityBirthInput::class.java)
            startActivity(mIntent)
        }

        findPassword_tv.setOnClickListener{
            Log.i ("findPasswordTV", "비밀번호 찾기")
            val mIntent = Intent(this, ActivityFindPassword_InputEmail::class.java)
            startActivity(mIntent)
        }
    }

    private fun login(context: Context, email : String, password: String) {

        Log.i ("login", "로그인 실행")

        val retrofit = ApiClient.getApiClient()
        val userService =retrofit.create(UserDataInterface::class.java)

        val request = UserDataInterface.LoginRequest(email, password)

        val call = userService.login(request)

        call?.enqueue(object : Callback<JsonElement?> {

            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {
                Log.i("onSuccess", response.body().toString())

                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.asJsonObject

                    if (jsonResponse != null) {
                        val loginCheck = jsonResponse.get("결과").asString
                        if (loginCheck == "1") {
                            val jwt_token = jsonResponse.get("토큰").asString
                            preferenceHelper.setIsLogin(jwt_token)
                            val mIntent = Intent(context, MainActivity::class.java)
                            startActivity(mIntent)
                        } else {
                            showLoginFailureDialog(context)
                        }
                    }else{
                        Log.i("onFailure", "응답이 올바르지 않음")
                    }
                }else{
                    Log.i("onFailure", "로그인 실패")
                }

            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                Log.i("onFailure", t.toString())
            }

        })

    }

    /***
     * 로그인 실패시 실행 함수
     */
    fun showLoginFailureDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("로그인 실패")
            .setMessage("로그인에 실패했습니다. 이메일이나 비밀번호를 확인해 주세요!")
            .setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }

        val dialog = builder.create()
        dialog.show()
    }


}
