package com.example.proteinpro.view.user.signup
import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import com.example.proteinpro.R
import com.example.proteinpro.databinding.ActivityBirthInputBinding
import com.example.proteinpro.util.Class.User
import java.util.Calendar

// 생일 데이터 받는 화면

class ActivityBirthInput : AppCompatActivity() {
    // 변수 선언
    private lateinit var datePicker: DatePicker
    private lateinit var nextButton: Button
    private lateinit var warning_tv: TextView
    private lateinit var user_birth: String

    private lateinit var back_btn_lo: LinearLayout

    // 전역 변수로 바인딩 객체 선언
    private var mBinding: ActivityBirthInputBinding? =null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!
    //!!는 Kotlin에서 Nullable 타입을 강제로 Non-nullable 타입으로 변환하는 것을 의미

    private val TAG = this.javaClass.simpleName

    override fun onBackPressed() {
        super.onBackPressed()

        if(isFinishing()){
            //back 버튼으로 종료시 동작

            overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit)

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_birth_input)

        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용해서
        // 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivityBirthInputBinding.inflate(layoutInflater)

        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의
        // 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시 합니다.
        setContentView(binding.root)

        // view 객체 초기화
        datePicker = binding.vDatePicker
        nextButton = binding.nextBTN
        warning_tv = binding.warningTv
        back_btn_lo = binding.backBtnLo

        //인텐트에서 유저정보 받아오기

        // 현재 날짜 기준으로 만 14세 미만인지 체크하고 결과를 출력하는 함수 호출
        checkIfUnder14YearsOld(datePicker.year, datePicker.month, datePicker.dayOfMonth)

        // DatePicker 값 변경 리스너 등록
        datePicker.init(
            datePicker.year,
            datePicker.month,
            datePicker.dayOfMonth,
            DatePicker.OnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
                // 값이 변경될 때마다 체크 함수 호출
                checkIfUnder14YearsOld(year, monthOfYear, dayOfMonth)
            }
        )

        nextButton.setOnClickListener {

            val mIntent = Intent(getApplicationContext(), ActivityTermsOfService::class.java)
            var getUser = intent.getSerializableExtra("user") as? User


            var user=
            if(getUser != null){
                getUser
            }else{
                User(birthDate =user_birth)
            }

            user.birthDate = user_birth

            mIntent.putExtra("user", user)

            startActivity(mIntent)
            overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit)
        }

        back_btn_lo.setOnClickListener{

            onBackPressed()
        }

    }


    /**
     * 현재 날짜를 Calendar 객체에서 받아와서 파라미터에 입력된 값을 기준으로 14세 미만인 경우를 체크해 줍니다.
     */
    private fun checkIfUnder14YearsOld(year: Int, month: Int, dayOfMonth: Int) {
        // 현재 날짜를 구합니다.
        val currentDate = Calendar.getInstance()
        val currentYear = currentDate.get(Calendar.YEAR)
        val currentMonth = currentDate.get(Calendar.MONTH)
        val currentDayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH)

        // 입력된 생년월일과 현재 날짜를 기반으로 만 나이를 계산합니다.
        val age = currentYear - year - if (currentMonth < month || (currentMonth == month && currentDayOfMonth < dayOfMonth)) 1 else 0

        // 만 14세 미만 여부를 체크합니다.
        val isUnder14YearsOld = age < 14

        // 결과 출력
        if (isUnder14YearsOld) {
            // 만 14세 미만인 경우 처리
            // 여기에 원하는 동작을 추가합니다.
            // 예를 들면 경고 메시지 띄우기 등의 처리를 할 수 있습니다.
            // 예시: Toast.makeText(this, "만 14세 미만입니다.", Toast.LENGTH_SHORT).show()
            warning_tv.setText("만 14세 미만은 가입이 불가능합니다.")
            warning_tv.isVisible = true

            nextButton.isEnabled = false// 버튼 비활성화

        } else {
            // 만 14세 이상인 경우 처리
            // 여기에 원하는 동작을 추가합니다.

            // 월은 0 부터 시작함 ㅠ
            val selectMonth = month +1

            user_birth = "$year-$selectMonth-$dayOfMonth"

            warning_tv.isVisible = false// 경고 제거
            nextButton.isEnabled = true// 버튼 활성화
        }
    }

}