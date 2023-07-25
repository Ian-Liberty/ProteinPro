package com.example.proteinpro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.proteinpro.databinding.ActivityLoginBinding

// 시작 페이지
// 로그인
//
class ActivityLogin : AppCompatActivity() {


    // 전역 변수로 바인딩 객체 선언
    private var mBinding: ActivityLoginBinding? =null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!
    //!!는 Kotlin에서 Nullable 타입을 강제로 Non-nullable 타입으로 변환하는 것을 의미

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 기존 setContentView 를 제거해주시고..
//        setContentView(R.layout.activity_login)

        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용해서
        // 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivityLoginBinding.inflate(layoutInflater)

        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의
        // 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시 합니다.
        setContentView(binding.root)


    }
}