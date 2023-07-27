package com.example.proteinpro.user.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.proteinpro.R
import com.example.proteinpro.databinding.ActivityAdditionalInfoInputBinding

class ActivityAdditionalInfoInput : AppCompatActivity() {

    // 전역 변수로 바인딩 객체 선언
    private var mBinding:
    ActivityAdditionalInfoInputBinding? =null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!
    //!!는 Kotlin에서 Nullable 타입을 강제로 Non-nullable 타입으로 변환하는 것을 의미
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_additional_info_input)
    }
}