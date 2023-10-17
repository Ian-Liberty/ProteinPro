package com.example.proteinpro.view
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.proteinpro.R
import com.example.proteinpro.databinding.ActivityTermsOfUseBinding

class ActivityTermsOfUse : AppCompatActivity() {

    // 전역 변수로 바인딩 객체 선언
    private var mBinding:
            ActivityTermsOfUseBinding? =null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!
    //!!는 Kotlin에서 Nullable 타입을 강제로 Non-nullable 타입으로 변환하는 것을 의미
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_of_use)

       // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용해서
       // 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
       mBinding = ActivityTermsOfUseBinding.inflate(layoutInflater)
       // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의
       // 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시 합니다.
       setContentView(binding.root)// < 기존의 setContentView 는 주석 처리해 주세요!

        binding.backBtn.setOnClickListener {
            Log.i ("정보태그", "backBtnLo")
            onBackPressed()
        }

    }
}