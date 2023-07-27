package com.example.proteinpro.user.signup
import android.util.Log
import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.proteinpro.R
import com.example.proteinpro.databinding.ActivityEmailInputBinding
import com.example.proteinpro.user.util.User

//이메일 입력
class ActivityEmailInput : AppCompatActivity() {
    // 변수 선언
    private lateinit var next_btn: Button
    private lateinit var email_et: EditText
    private lateinit var warning_tv: TextView
    private lateinit var receivedIntent: Intent
    private lateinit var user: User

    // 전역 변수로 바인딩 객체 선언
    private var mBinding:
    ActivityEmailInputBinding? =null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!
    //!!는 Kotlin에서 Nullable 타입을 강제로 Non-nullable 타입으로 변환하는 것을 의미
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i ("ActivityEmailInput", "onCreate")
//        setContentView(R.layout.activity_email_input)

        mBinding = ActivityEmailInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 인텐트 초기화
        receivedIntent = intent
        // 회원가입 유저 객체 상태 업데이트
        user = receivedIntent.getSerializableExtra("user") as User
        Log.i ("인텐트 테스트", ""+user)

        initViews()
        initListener()

    }
    private fun initViews(){

        next_btn = binding.nextBTN
        email_et = binding.emailET
        warning_tv = binding.warningTv

    }
    private fun initListener(){

        next_btn.setOnClickListener {
            val mIntent = Intent(this, ActivityEmailCheck::class.java)
            user.email = email_et.getText().toString()
            mIntent.putExtra("user", user)
            startActivity(mIntent)
        }

        email_et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 값이 변경되기전에 호출
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 값이 변경 될때 호출

            }

            override fun afterTextChanged(s: Editable?) {
                // 값이 변경된 후에 호출

                //만약 입력된 값이 이메일 형식에 부합 할 경우
                if(isValidEmail(email_et.getText().toString())){
                    warning_tv.isVisible = false// 경고 제거
                    next_btn.isEnabled = true// 버튼 활성화
                }else{
                    warning_tv.setText("올바른 형식의 이메일을 작성해 주세요!")
                    warning_tv.isVisible = true

                    next_btn.isEnabled = false// 버튼 비활성화
                }
            }

        })


    }

    /***
     * 이메일 형식 검사 함수
     */
    fun isValidEmail(email: String): Boolean {
        // 이메일 형식에 맞는 정규표현식
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")

        // 입력된 이메일이 정규표현식과 일치하는지 검사하여 결과 반환
        return email.matches(emailRegex)
    }


}