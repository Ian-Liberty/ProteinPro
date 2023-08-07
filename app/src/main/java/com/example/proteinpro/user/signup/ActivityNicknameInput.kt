package com.example.proteinpro.user.signup

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
import androidx.core.view.isVisible
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
        // 리스너 초기화
        back_btn_lo.setOnClickListener {
            finish()
        }

        next_btn.setOnClickListener {
            user.nickname = nickname_et.text.toString()
            val mIntent = Intent(this, ActivityAdditionalInfoInput::class.java)

            mIntent.putExtra("user", user)
            retrofitHelper.checkNicknameDuplication(user.nickname){isSuccess ->
                if(isSuccess){
                    retrofitHelper.userDataUpdate(user){isSuccess->
                        if(isSuccess){

                            startActivity(mIntent)

                        }else{

                        }

                    }

                }else{
                    //중복된 닉네임
                }
            }

        }

        nickname_et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

                if(isNicknameValid(nickname_et.text.toString())){

                    //닉네임 형식 유효
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

}