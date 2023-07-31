package com.example.proteinpro.user.signup
import android.widget.Toast

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.proteinpro.MainActivity
import com.example.proteinpro.R
import com.example.proteinpro.databinding.ActivityAdditionalInfoInputBinding
import com.example.proteinpro.user.util.PreferenceHelper
import com.example.proteinpro.user.util.Retrofit.RetrofitHelper
import com.example.proteinpro.user.util.User

class ActivityAdditionalInfoInput : AppCompatActivity() {
    // 인텐트 변수
    private lateinit var receivedIntent: Intent
    private lateinit var user: User

    // 유틸 함수 선언
    private lateinit var preferenceHelper: PreferenceHelper
    private lateinit var retrofitHelper: RetrofitHelper

    //view 선언
    //제출 번튼
    private lateinit var next_btn: Button

    private lateinit var skip_tv : TextView

    // 성별
    private lateinit var genderRadioGroup: RadioGroup
    private lateinit var male_rb: RadioButton
    private lateinit var female_rb: RadioButton

    //키/몸무게
    private lateinit var height_et : EditText
    private lateinit var weight_et : EditText

    // 활동량
    private lateinit var activityLevel_rg: RadioGroup
    private lateinit var never_rb : RadioButton
    private lateinit var one_three_rb : RadioButton
    private lateinit var four_five_rb : RadioButton
    private lateinit var six_seven_rb : RadioButton
    private lateinit var seven_hard : RadioButton



    // 전역 변수로 바인딩 객체 선언
    private var mBinding:
    ActivityAdditionalInfoInputBinding? =null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!
    //!!는 Kotlin에서 Nullable 타입을 강제로 Non-nullable 타입으로 변환하는 것을 의미
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//      setContentView(R.layout.activity_additional_info_input)

        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용해서
        // 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivityAdditionalInfoInputBinding.inflate(layoutInflater)
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
        preferenceHelper=PreferenceHelper(this)
        retrofitHelper = RetrofitHelper(this)
    }

    private fun initViews(){
        // 뷰 초기화

        next_btn = binding.nextBTN

        skip_tv = binding.skipTV

        genderRadioGroup = binding.genderRG
        male_rb = binding.male
        female_rb = binding.female

        height_et = binding.heightET
        weight_et = binding.weightET

        activityLevel_rg = binding.activityLevelRG
        never_rb = binding.neverRB
        one_three_rb = binding.oneThreeRB
        four_five_rb = binding.fourFiveRB
        six_seven_rb = binding.sixSevenRB
        seven_hard = binding.sevenHardRB

    }
    private fun initListener(){
        // 리스너 초기화
        next_btn.setOnClickListener {
            val genderCheckedId = genderRadioGroup.checkedRadioButtonId
            val heightText = height_et.text.toString().trim()
            val weightText = weight_et.text.toString().trim()
            val activityLevelCheckedId = activityLevel_rg.checkedRadioButtonId

            // 키 몸무게
            user.height =heightText.toInt()
            user.weight =weightText.toInt()
            // 유저성별
            if(genderCheckedId == male_rb.id){
                user.gender = 1//남
            }else{
                user.gender = 0//여
            }

            // 유저활동량
            when (activityLevelCheckedId) {
                never_rb.id -> {
                    user.activityLevel = User.ActivityLevel.SEDENTARY
                }
                one_three_rb.id -> {
                    user.activityLevel = User.ActivityLevel.LIGHTLY_ACTIVE
                }
                four_five_rb.id -> {
                    user.activityLevel = User.ActivityLevel.MODERATELY_ACTIVE
                }
                six_seven_rb.id -> {
                    user.activityLevel = User.ActivityLevel.VERY_ACTIVE
                }
                seven_hard.id -> {
                    user.activityLevel = User.ActivityLevel.EXTRA_ACTIVE
                }
                else -> {
                    user.activityLevel = User.ActivityLevel.SEDENTARY
                }
            }

            if (genderCheckedId == -1 || heightText.isEmpty() || weightText.isEmpty() || activityLevelCheckedId == -1) {
                // 빈값이 있을경우
                showWarningAlertDialog()
            } else {

                retrofitHelper.userDataUpdate(user){isSuccess->
                    if(isSuccess){
                        val mIntent = Intent(getApplicationContext(), MainActivity::class.java)
                        startActivity(mIntent)

                        Toast.makeText(getApplicationContext(), "${user.nickname} 님 환영합니다!",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(getApplicationContext(), "데이터 저장에 실패했습니다. 관리자에게 문의하세요!",Toast.LENGTH_SHORT).show()
                    }

                }



            }
        }

        skip_tv.setOnClickListener{

            showSkipAlertDialog()
        }

    }

    private fun showSkipAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)

        // 다이얼로그 제목과 메시지 설정
        alertDialogBuilder.setTitle("알림")
        alertDialogBuilder.setMessage("상세정보 입력을 건너뛰시겠어요? \n (추가정보는 마이페이지에서 나중에 입력 및 수정 가능합니다.)")

        // 확인 버튼에 대한 처리
        alertDialogBuilder.setPositiveButton("확인") { dialog, which ->
            // 확인 버튼이 눌렸을 때의 동작을 여기에 작성합니다.
            // 예를 들면 확인 버튼을 눌렀을 때 할 작업을 정의할 수 있습니다.

            val mIntent = Intent(this, MainActivity::class.java)
            startActivity(mIntent)

        }

        // 취소 버튼에 대한 처리 (선택사항)
        alertDialogBuilder.setNegativeButton("취소") { dialog, which ->
            // 취소 버튼이 눌렸을 때의 동작을 여기에 작성합니다.
            // 예를 들면 취소 버튼을 눌렀을 때 할 작업을 정의할 수 있습니다.
        }

        // 다이얼로그 생성 및 표시
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showWarningAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)

        // 다이얼로그 제목과 메시지 설정
        alertDialogBuilder.setTitle("알림")
        alertDialogBuilder.setMessage("아직 입력하지 않은 정보가 남았어요! 그래도 입력을 건너뛰시겠어요?")

        // 확인 버튼에 대한 처리
        alertDialogBuilder.setPositiveButton("확인") { dialog, which ->
            // 확인 버튼이 눌렸을 때의 동작을 여기에 작성합니다.
            // 예를 들면 확인 버튼을 눌렀을 때 할 작업을 정의할 수 있습니다.

            val mIntent = Intent(this, MainActivity::class.java)
            startActivity(mIntent)

        }

        // 취소 버튼에 대한 처리 (선택사항)
        alertDialogBuilder.setNegativeButton("취소") { dialog, which ->
            // 취소 버튼이 눌렸을 때의 동작을 여기에 작성합니다.
            // 예를 들면 취소 버튼을 눌렀을 때 할 작업을 정의할 수 있습니다.
        }

        // 다이얼로그 생성 및 표시
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}