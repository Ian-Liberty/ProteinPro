package com.example.proteinpro.view.user.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.proteinpro.R
import com.example.proteinpro.databinding.ActivityPasswordInputBinding
import com.example.proteinpro.util.PreferenceHelper
import com.example.proteinpro.util.Retrofit.RetrofitHelper
import com.example.proteinpro.util.Class.User

class ActivityPasswordInput : AppCompatActivity() {
    // 변수 선언
    private lateinit var next_btn:Button
    private lateinit var  password_et:EditText
    private lateinit var warning_tv:TextView

    private lateinit var back_btn_lo : LinearLayout

    // 유틸 함수 선언
    private lateinit var preferenceHelper: PreferenceHelper
    private lateinit var retrofitHelper: RetrofitHelper


    private lateinit var receivedIntent: Intent
    private lateinit var user: User

    // 전역 변수로 바인딩 객체 선언
    private var mBinding:
            ActivityPasswordInputBinding? =null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!
    //!!는 Kotlin에서 Nullable 타입을 강제로 Non-nullable 타입으로 변환하는 것을 의미
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_password_input)

        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용해서
        // 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivityPasswordInputBinding.inflate(layoutInflater)
        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의
        // 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시 합니다.
        setContentView(binding.root)// < 기존의 setContentView 는 주석 처리해 주세요!

        // 인텐트 초기화
        receivedIntent = intent
        // 회원가입 유저 객체 상태 업데이트
        user = receivedIntent.getSerializableExtra("user") as User
        Log.i ("인텐트 테스트", ""+user)
        initUtils()
        initViews()
        initListener()

    }
    private fun initUtils(){
        preferenceHelper= PreferenceHelper(this)
        retrofitHelper = RetrofitHelper(this)
    }

    private fun initViews(){
        // 뷰 초기화

        next_btn = binding.nextBTN
        password_et = binding.passwordET
        warning_tv = binding.warningTv
        back_btn_lo =binding.backBtnLo

    }
    private fun initListener(){
        // 리스너 초기화

        back_btn_lo.setOnClickListener {
            finish()
        }

        next_btn.setOnClickListener {

            val mIntent = Intent(getApplicationContext(), ActivityAdditionalInfoInput::class.java)

            user.password = password_et.text.toString()

            mIntent.putExtra("user", user)

            retrofitHelper.signUp(user){isSuccess ->
                        if(isSuccess){
                            retrofitHelper.login(this,user.email,user.password){
                                if(it){
                                    startActivity(mIntent)
                                    overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit)
                                }else{
                                    Toast.makeText(getApplicationContext(), "로그인에 실패 했습니다.",Toast.LENGTH_SHORT).show()
                                }
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "회원가입에 실패했습니다. 관리자에게 문의해 주세요", Toast.LENGTH_SHORT).show()
                        }

                    }
        }

        password_et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 값이 변경되기전에 호출
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 값이 변경 될때 호출

            }

            override fun afterTextChanged(s: Editable?) {
                // 값이 변경된 후에 호출
                // 비밀번호 인풋 값 검사
                if(isPasswordValid(password_et.getText().toString())){
                    //비밀번호 형식 유효
                    warning_tv.isVisible = false

                    next_btn.isEnabled = true

                }else{

                    //비밀번호 형식 유효하지 않으면
                    warning_tv.setText("비밀번호는 알파벳 과 숫자 그리고 하나이상의 특수문자가 포함된 \n8이상 15자 이하의 조합으로 구성해 주세요!")
                    warning_tv.isVisible = true

                    next_btn.isEnabled = false

                }


            }

        })
    }

    fun isPasswordValid(password: String): Boolean {
        val passwordRegex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#\$%^*+=-]).{8,15}\$".toRegex()
        return password.matches(passwordRegex)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if(isFinishing()){
            //back 버튼으로 종료시 동작

            overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit)

        }

    }
}