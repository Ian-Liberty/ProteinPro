package com.example.proteinpro.user.signup
import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import com.example.proteinpro.R
import com.example.proteinpro.databinding.ActivityLoginBinding
import com.example.proteinpro.databinding.ActivityTermsOfServiceBinding
import com.example.proteinpro.util.User

class ActivityTermsOfService : AppCompatActivity() {
    // 변수 선언
        private lateinit var next_btn: Button
        private lateinit var termsOfService_cb: CheckBox
        private lateinit var fullAgreement_cb: CheckBox
        private lateinit var personalData_cb: CheckBox

        private lateinit var back_btn_lo : LinearLayout

        private lateinit var receivedIntent: Intent
        private lateinit var user: User

        // 전역 변수로 바인딩 객체 선언
        private var mBinding:
                ActivityTermsOfServiceBinding? =null
        // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
        private val binding get() = mBinding!!
        //!!는 Kotlin에서 Nullable 타입을 강제로 Non-nullable 타입으로 변환하는 것을 의미
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
    //        setContentView(R.layout.activity_terms_of_service)
            // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용해서
            // 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
            mBinding = ActivityTermsOfServiceBinding.inflate(layoutInflater)
            // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의
            // 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시 합니다.
            setContentView(binding.root)// < 기존의 setContentView 는 주석 처리해 주세요!

            receivedIntent = intent
            // 회원가입 유저 객체 상태 업데이트
            user = receivedIntent.getSerializableExtra("user") as User
            Log.i ("인텐트 테스트", ""+user)

            initViews()
            initListener()

        }

        private fun initViews(){

            // 뷰 초기화
            next_btn = binding.nextBTN
            termsOfService_cb = binding.termsOfServiceCB
            fullAgreement_cb = binding.fullAgreementCB
            personalData_cb = binding.personalDataCB

            back_btn_lo = binding.backBtnLo

        }
        private fun initListener(){
            // 리스너 초기화
            next_btn.setOnClickListener {

                val mIntent = Intent(getApplicationContext(), ActivityEmailInput::class.java)
                mIntent.putExtra("user", user)
                startActivity(mIntent)
            }

            // 전체 체크박스
            fullAgreement_cb.setOnCheckedChangeListener{_, isChecked ->
                // Checkbox 상태가 변할 때 호출되는 콜백 메서드입니다.

                if(isChecked){// 전체 체크박스가 활성화 되면
                    // 나머지 전체 활성화
                    termsOfService_cb.isChecked = isChecked
                    personalData_cb.isChecked = isChecked
                }else{// 비활성화 될 경우

                    if(termsOfService_cb.isChecked && personalData_cb.isChecked){
                        // 모두 켜져있는상태에서 꺼지는경우== 전체 선택 직접 클릭으로 선택 해제 하는경우
                        termsOfService_cb.isChecked = isChecked
                        personalData_cb.isChecked = isChecked
                    }else{
                        // 모두 켜져있지 않은 상태에서 꺼지는경우 == 어떤 값이 변함으로써 체크해제 하는경우이므로
                    // 다른 체크박스는 움직이지 않아야 함
                    }
                }
                next_btn.isEnabled = fullAgreement_cb.isChecked
            }

            termsOfService_cb.setOnCheckedChangeListener{compoundButton, isChecked ->

                if(isChecked){// 체크한 경우
                    // 나머지 체크박스를 검사한 후 모두 true 일 경우 전체 선택도 활성화
                    fullAgreement_cb.isChecked = personalData_cb.isChecked
                }else{
                    // 해제된 경우 전체선택 해제
                    fullAgreement_cb.isChecked = false
                }
                next_btn.isEnabled = fullAgreement_cb.isChecked
            }
            personalData_cb.setOnCheckedChangeListener{compoundButton, isChecked ->

                if(isChecked){// 체크한 경우
                        // 나머지 체크박스를 검사한 후 모두 true 일 경우 전체 선택도 활성화
                    fullAgreement_cb.isChecked = termsOfService_cb.isChecked

                }else{
                    // 해제된 경우 전체선택 해제
                    fullAgreement_cb.isChecked = false

                }

                next_btn.isEnabled = fullAgreement_cb.isChecked
            }

            back_btn_lo.setOnClickListener{
                finish()
            }

        }


}