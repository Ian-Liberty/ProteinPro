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
import com.example.proteinpro.databinding.ActivityNicknameInputBinding
import com.example.proteinpro.util.PreferenceHelper
import com.example.proteinpro.util.Retrofit.RetrofitHelper
import com.example.proteinpro.util.Class.User


class ActivityNicknameInput : AppCompatActivity() {
    //view 객체 선언
    private lateinit var nickname_et:EditText
    private lateinit var warning_tv: TextView
    private lateinit var next_btn: Button

    private lateinit var back_btn_lo : LinearLayout

    //유틸 클래스 선언
    private lateinit var retrofitHelper: RetrofitHelper
    private lateinit var preferenceHelper: PreferenceHelper

    private lateinit var receivedIntent: Intent
    private lateinit var user: User
    // 전역 변수로 바인딩 객체 선언
    private var mBinding:
            ActivityNicknameInputBinding? =null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!
    //!!는 Kotlin에서 Nullable 타입을 강제로 Non-nullable 타입으로 변환하는 것을 의미
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_nickname_input)

        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용해서
        // 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivityNicknameInputBinding.inflate(layoutInflater)
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

    private  fun initUtils(){
        preferenceHelper = PreferenceHelper(this)
        retrofitHelper = RetrofitHelper(this)
    }
    private fun initViews(){
        // 뷰 초기화
        next_btn = binding.nextBTN
        nickname_et = binding.nicknameET
        warning_tv = binding.warningTv
        back_btn_lo = binding.backBtnLo
    }
    private fun initListener(){


        val mIntent = Intent(this, ActivityPasswordInput::class.java)
        // 리스너 초기화
        back_btn_lo.setOnClickListener {
            finish()
        }

        next_btn.setOnClickListener {

            user.nickname = nickname_et.text.toString()
            mIntent.putExtra("user", user)

                    startActivity(mIntent)
                    overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit)

//                    retrofitHelper.signUp(user){isSuccess ->
//                        if(isSuccess){
//                            retrofitHelper.login(this,user.email,user.password){
//                                if(it){
//                                    startActivity(mIntent)
//                                    overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit)
//                                }else{
//                                    Toast.makeText(getApplicationContext(), "로그인에 실패 했습니다.",Toast.LENGTH_SHORT).show()
//                                }
//                            }
//                        }else{
//                            Toast.makeText(getApplicationContext(), "회원가입에 실패했습니다. 관리자에게 문의해 주세요", Toast.LENGTH_SHORT).show()
//                        }
//
//                    }


        }

        nickname_et.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

                if(isNicknameValid(nickname_et.text.toString())){

                    //닉네임 형식 유효

                    retrofitHelper.checkNicknameDuplication(user.nickname){isSuccess ->
                        if(isSuccess){
                            // 중복되지 않음

                            warning_tv.isVisible = false
                            next_btn.isEnabled = true

                        }else{
                            //중복된 닉네임

                            warning_tv.setText("이미 존재하는 닉네임 입니다")
                            warning_tv.isVisible = true

                            next_btn.isEnabled = false

                        }
                    }


                    warning_tv.isVisible = false
                    next_btn.isEnabled = true

                }else{

                    //닉네임 형식이 유효하지 않으면
                    warning_tv.setText("닉네임은 2~8자 사이의 영문이나 한글로 만들어해주세요! ")
                    warning_tv.isVisible = true

                    next_btn.isEnabled = false
                }


            }

        })

    }

    fun isNicknameValid(nickname: String): Boolean {
        val regex = Regex("^[a-zA-Z가-힣]{2,8}$")
        return regex.matches(nickname) && !hasInvalidKoreanCharacter(nickname)
    }

    fun hasInvalidKoreanCharacter(nickname: String): Boolean {
        val invalidKoreanCharacters = setOf('ㄱ', 'ㄴ', 'ㄷ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅅ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ',
            'ㄲ', 'ㄸ', 'ㅃ', 'ㅆ', 'ㅉ', 'ㅏ', 'ㅓ', 'ㅗ', 'ㅜ', 'ㅡ', 'ㅣ')
        for (char in nickname) {
            if (char in invalidKoreanCharacters) {
                return true
            }
        }
        return false
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if(isFinishing()){
            //back 버튼으로 종료시 동작

            overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit)

        }

    }
}