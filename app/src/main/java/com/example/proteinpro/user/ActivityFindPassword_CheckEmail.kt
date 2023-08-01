package com.example.proteinpro.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.proteinpro.R
import com.example.proteinpro.databinding.ActivityEmailInputBinding
import com.example.proteinpro.databinding.ActivityFindPasswordCheckEmailBinding
import com.example.proteinpro.user.signup.ActivityPasswordInput
import com.example.proteinpro.util.PreferenceHelper
import com.example.proteinpro.util.Retrofit.RetrofitHelper
import com.example.proteinpro.util.User

class ActivityFindPassword_CheckEmail : AppCompatActivity() {

    //변수 선언
    private lateinit var next_btn: Button
    private lateinit var user_email_tv:TextView
    private lateinit var resend_tv:TextView
    private lateinit var limit_time_tv:TextView
    private lateinit var countDownTimer: CountDownTimer
    private val initialMillis: Long = 10 * 60 * 1000 // 10:00

    private lateinit var back_btn_lo : LinearLayout

    // 인텐트 변수
    private lateinit var receivedIntent: Intent
    private lateinit var user: User

    //유틸 클래스 선언
    private lateinit var retrofitHelper: RetrofitHelper
    private lateinit var preferenceHelper: PreferenceHelper

    //6블록 인증번호 입력
    private var cert: String = ""
    private lateinit var certNum: Array<EditText>
    private lateinit var sign_certNum_1 :EditText
    private lateinit var sign_certNum_2 :EditText
    private lateinit var sign_certNum_3 :EditText
    private lateinit var sign_certNum_4 :EditText
    private lateinit var sign_certNum_5 :EditText
    private lateinit var sign_certNum_6 :EditText
    // 전역 변수로 바인딩 객체 선언
    private var mBinding:
            ActivityFindPasswordCheckEmailBinding? =null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!
    //!!는 Kotlin에서 Nullable 타입을 강제로 Non-nullable 타입으로 변환하는 것을 의미

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_find_password_check_email)

        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용해서
        // 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivityFindPasswordCheckEmailBinding.inflate(layoutInflater)
        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의
        // 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시 합니다.
        setContentView(binding.root)// < 기존의 setContentView 는 주석 처리해 주세요!

        // 인텐트 초기화
        receivedIntent = intent
        // 회원가입 유저 객체 상태 업데이트
        user = receivedIntent.getSerializableExtra("user") as User
        Log.i ("인텐트 테스트", ""+user)

        //유틸 인터페이스 초기화

        initUtils()
        initViews()
        initListener()
        // 함수 실행

        startCountdown(initialMillis)

        for(editText in certNum) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    checkAllEditTextFilled()
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })
        }

    }


    private fun initUtils(){
        retrofitHelper = RetrofitHelper(this)
        preferenceHelper = PreferenceHelper(this)
    }

    private fun initViews(){
        // 뷰 초기화
        next_btn = binding.nextBTN
        back_btn_lo = binding.backBtnLo

        limit_time_tv = binding.limitTimeTv

        user_email_tv = binding.userEmailTV
        user_email_tv.setText(user.email)

        resend_tv = binding.reSendTV

        // 6칸 edittext
        sign_certNum_1 = binding.signCertNum1
        sign_certNum_2 = binding.signCertNum2
        sign_certNum_3 = binding.signCertNum3
        sign_certNum_4 = binding.signCertNum4
        sign_certNum_5 = binding.signCertNum5
        sign_certNum_6 = binding.signCertNum6

        certNum = arrayOf(
            sign_certNum_1,
            sign_certNum_2,
            sign_certNum_3,
            sign_certNum_4,
            sign_certNum_5,
            sign_certNum_6
        )

    }
    private fun initListener(){
        // 리스너 초기화
        setCertNumOnTextChangedListener()
        onDelKeyListener()
        next_btn.setOnClickListener(onClickButtonListener())
        resend_tv.setOnClickListener{
            //리스너 초기화
            retrofitHelper.requestCertNum(user.email){isSuccess ->
                if (isSuccess){
                    // 인증번호 요청 성공
                    Toast.makeText(getApplicationContext(), "인증번호를 다시 보내드렸어요!", Toast.LENGTH_SHORT).show()
                    restartCountdown()
                }
                else{
                    // 인증번호 요청 실패
                }

            }

        }
        back_btn_lo.setOnClickListener {
            finish()
        }

    }
    /***
     * TODO
     * API 연결 전이기에 검사하지 않고 6칸 차있으면 바로 넘어감
     * API 연결 후 인증번호 인증 후 반환값에 맞게 동작하도록 수정 필요
     */
    private fun onClickButtonListener(): View.OnClickListener? {
        return View.OnClickListener {
            cert = "" // 버튼을 클릭할 때마다 cert 변수 초기화
            for(i in 0..5) cert += certNum[i].text.toString()// 각 certNum EditText의 값을 가져와서 더함
            if (cert.length != 6)
                Toast.makeText(applicationContext, "인증번호를 전부 입력해주세요.", Toast.LENGTH_SHORT).show()
            else {
                Log.i ("cert", ""+cert)

                //토큰값 받아오기
                val token = preferenceHelper.get_jwt_Token().toString()

                retrofitHelper.checkAuthnum(cert, token){isSuccess ->
                    if(isSuccess){
                        // 인증번호 확인 완료
                        val mIntent = Intent(this, ActivityFindPassword_NewPassword::class.java)

                        mIntent.putExtra("user", user)

                        startActivity(mIntent)

                    }else{
                        // 인증 실패



                    }

                }

            }
        }
    }

    private fun onDelKeyListener() {
        Log.i ("onDelKeyListener", "")
        for (idx in 0..5) {
            certNum[idx].setOnKeyListener { view: View, i: Int, keyEvent: KeyEvent ->
                if (i == KeyEvent.KEYCODE_DEL && certNum[idx].text.isEmpty() && idx > 0) {
                    // 이전 EditText로 포커스 이동
                    certNum[idx - 1].requestFocus()
                }
                false
            }
        }
    }

    private fun setCertNumOnTextChangedListener() {
        for (idx in 0 until certNum.size - 1) certNum[idx].addTextChangedListener {
            if (certNum[idx].length() == 1) {
                certNum[idx + 1].requestFocus()
                certNum[idx + 1].text = null
            }
        }
    }

    private fun startCountdown(totalMillis: Long) {
        countDownTimer = object : CountDownTimer(totalMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = (millisUntilFinished / 1000) % 60
                val timeString = String.format("%02d:%02d", minutes, seconds)
                limit_time_tv.text = timeString
            }

            override fun onFinish() {
                limit_time_tv.text = "00:00"
            }
        }

        countDownTimer.start()
    }

    //
    private fun restartCountdown() {
        countDownTimer.cancel() // 기존 타이머 취소
        limit_time_tv.text = "10:00" // TextView를 03:00으로 초기화
        startCountdown(initialMillis) // 타이머 재시작
    }

    private fun checkAllEditTextFilled() {
        var allFilled = true
        for(editText in certNum){
            if(editText.text.isBlank()){
                allFilled = false
                break
            }

        }

        next_btn.isEnabled = allFilled
    }
}