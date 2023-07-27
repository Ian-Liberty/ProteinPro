package com.example.proteinpro
import android.util.Log
import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.proteinpro.databinding.ActivityLoginBinding
import com.example.proteinpro.user.ActivityFindPassword
import com.example.proteinpro.user.signup.ActivityBirthInput
import com.example.proteinpro.user.signup.ActivityTermsOfService

// 시작 페이지
// 로그인 및 회원가입 비밀번호 찾기 등 접근 가능
//
class ActivityLogin : AppCompatActivity() {

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

        // 로그인 버튼 클릭 이벤트
        binding.loginBTN.setOnClickListener {
            Log.i ("loginBTN", "로그인 버튼")
            val mIntent = Intent(this, MainActivity::class.java)
            startActivity(mIntent)
        }

        binding.kakaoLoginBTN.setOnClickListener{
            Log.i ("kakaoLoginBTN", "카카오 로그인 버튼")
            //
            // 카카오 로그인 버튼 클릭 이벤트
        }

        binding.signUpTV.setOnClickListener{
            Log.i ("signUpTV", "회원가입 클릭")
            val mIntent = Intent(this, ActivityTermsOfService::class.java)
            startActivity(mIntent)
        }

        binding.findPasswordTV.setOnClickListener{
            Log.i ("findPasswordTV", "비밀번호 찾기")
            val mIntent = Intent(this, ActivityFindPassword::class.java)
            startActivity(mIntent)
        }

    }
}