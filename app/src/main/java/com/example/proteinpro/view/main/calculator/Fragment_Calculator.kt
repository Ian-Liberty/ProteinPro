package com.example.proteinpro.view.main.calculator
import android.util.Log

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

import com.example.proteinpro.R
import com.example.proteinpro.databinding.FragmentCalculatorBinding
import com.example.proteinpro.util.Class.HarrisBenedict1919Calculator
import com.example.proteinpro.util.Class.MifflinStJeorCalculator
import com.example.proteinpro.util.Class.User
import com.example.proteinpro.util.PreferenceHelper
import com.example.proteinpro.util.Retrofit.RetrofitHelper
import com.example.proteinpro.view.main.MainActivity

class Fragment_Calculator : Fragment() {

    //컨텍스트 변수
    private lateinit var mainActivity: MainActivity

    // 변수 입력
    private lateinit var binding: FragmentCalculatorBinding
    // 뷰 객체

    //view 선언
    //제출 번튼
    private lateinit var next_btn: Button

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

    //생년월일
    private lateinit var datePicker: DatePicker
    private lateinit var user_birth: String

    //

    private lateinit var help_iv : ImageView



    // 유틸 클래스
    // 유틸 함수 선언
    private lateinit var preferenceHelper: PreferenceHelper
    private lateinit var retrofitHelper: RetrofitHelper

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalculatorBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUtils()
        initViews()
        initListener()
        initData()
    }

    // 리스너 선언
    private fun initListener() {

        help_iv.setOnClickListener{
            showPopupMessage(mainActivity, "","하루 탄단지 섭취량 계산은  Harris-Benedict(해리스-베네딕트 방정식)을 이용하여 기초대사량(BMR)을 계산과 활동량을 토대로 탄단지 비율에 맞게 계산됩니다.")
        }

        next_btn.setOnClickListener {

            if(areAllFieldsFilled()){
                val genderCheckedId = genderRadioGroup.checkedRadioButtonId
                val heightCm = height_et.text.toString().trim().toInt()
                val weightKg = weight_et.text.toString().trim().toInt()
                val activityLevelCheckedId = activityLevel_rg.checkedRadioButtonId

                val year = datePicker.year
                val selectMonth = datePicker.month+1
                val dayOfMonth = datePicker.dayOfMonth
                val userBirth = "$year-$selectMonth-$dayOfMonth"

                val gender: String
                val activityLevel: HarrisBenedict1919Calculator.ActivityLevel

                // 키 몸무게

                // 유저성별
                if(genderCheckedId == male_rb.id){
                    //남
                    gender = "male"
                }else{
                    //여
                    gender = "female"
                }

                // 유저활동량
                when (activityLevelCheckedId) {
                    never_rb.id -> {
                        activityLevel = HarrisBenedict1919Calculator.ActivityLevel.SEDENTARY
                    }
                    one_three_rb.id -> {
                        activityLevel = HarrisBenedict1919Calculator.ActivityLevel.LIGHTLY_ACTIVE
                    }
                    four_five_rb.id -> {
                        activityLevel = HarrisBenedict1919Calculator.ActivityLevel.MODERATELY_ACTIVE
                    }
                    six_seven_rb.id -> {
                        activityLevel = HarrisBenedict1919Calculator.ActivityLevel.VERY_ACTIVE
                    }
                    seven_hard.id -> {
                        activityLevel = HarrisBenedict1919Calculator.ActivityLevel.EXTRA_ACTIVE
                    }
                    else -> {
                        activityLevel = HarrisBenedict1919Calculator.ActivityLevel.SEDENTARY
                    }
                }

                val calculator = HarrisBenedict1919Calculator(gender, userBirth, heightCm, weightKg, activityLevel)

                val Calories = calculator.calculateRecommendedCalories()

                val (carbs, protein, fat) = calculator.calculateRecommendedNutrients()

                val bmi = calculator.calculateBMI()
                val bmiClassification = calculator.classifyBMI()

                Log.i ("계산결과", "전체 칼로리: ${Calories}, 탄수화물: ${carbs}g, 단백질:${protein}g, 지방:${fat}g, BMI: ${bmi}, $bmiClassification")

                val mIntent = Intent(mainActivity, ActivityCarculatorResult::class.java)

                mIntent.putExtra("gender",gender)
                mIntent.putExtra("userBirth", userBirth)
                mIntent.putExtra("heightCm", heightCm)
                mIntent.putExtra("weightKg",weightKg)
                mIntent.putExtra("activityLevel",activityLevel.name)

                startActivity(mIntent)

            }else{
                showWarningAlertDialog()// 빈값 있음 알림
            }

        }

    }

    // 뷰 객체 할당
    private fun initViews() {

        // view 객체 초기화
        datePicker = binding.vDatePicker

        next_btn = binding.nextBTN

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

        help_iv = binding.helpIV

    }

    // 현재 프래그먼트에서 필요한 데이터 받아오기
    private fun initData() {
        // 정보가 존재하는 유저일 경우 초기값 넣어주기
        val user = preferenceHelper.getUser()
        if(user != null){

            // 성별
            if(user?.gender == 0){
                // 여성일경우
                genderRadioGroup.check(R.id.female)
            }else{
                // 남자일 경우
                genderRadioGroup.check(R.id.male)
            }

            // 몸무게
            if(user.weight == -1){

            }else{
                weight_et.setText(user.weight.toString())
            }

            // 키
            if(user.height== -1){

            }else{
                height_et.setText(user.height.toString())
            }

            val birth = user.birthDate
            val dateParts = birth.split("-")// 년 월 일 분리후 리스트로 만듦

            // 생일
            if(dateParts.size == 3) {// 정상분리될 경우 3이어야 함
                val year = dateParts[0].toInt()
                val month = dateParts[1].toInt() - 1 // 월은 0부터 시작하므로 1을 빼줌
                val day = dateParts[2].toInt()

                datePicker.updateDate(year, month, day)// 날짜 업데이트

            }else{
                Log.d("계산기 초기설정","생일정보 오류")
            }

            // 활동레벨

            when(user.activityLevel){
              User.ActivityLevel.SEDENTARY -> activityLevel_rg.check(R.id.never_RB)
                User.ActivityLevel.LIGHTLY_ACTIVE -> activityLevel_rg.check(R.id.one_three_RB)
                User.ActivityLevel.MODERATELY_ACTIVE -> activityLevel_rg.check(R.id.four_five_RB)
                User.ActivityLevel.VERY_ACTIVE -> activityLevel_rg.check(R.id.six_seven_RB)
                User.ActivityLevel.EXTRA_ACTIVE -> activityLevel_rg.check(R.id.seven_hard_RB)
            }

        }

    }
    // 유틸 클래스 할당
    private fun initUtils() {

        preferenceHelper = PreferenceHelper(mainActivity)

    }

    fun areAllFieldsFilled(): Boolean {

        val genderSelected = genderRadioGroup.checkedRadioButtonId != -1
        val heightValue = height_et.text.toString().isNotEmpty()
        val weightValue = weight_et.text.toString().isNotEmpty()
        val activityLevelSelected = activityLevel_rg.checkedRadioButtonId != -1

        return genderSelected && heightValue && weightValue && activityLevelSelected
    }

    private fun showWarningAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(mainActivity)

        // 다이얼로그 제목과 메시지 설정
        alertDialogBuilder.setTitle("알림")
        alertDialogBuilder.setMessage("입력하지 않은 정보가 있어요! 모든 정보를 입력해 주세요!")

        // 확인 버튼에 대한 처리
        alertDialogBuilder.setPositiveButton("확인") { dialog, which ->
            // 확인 버튼이 눌렸을 때의 동작을 여기에 작성합니다.
            // 예를 들면 확인 버튼을 눌렀을 때 할 작업을 정의할 수 있습니다.

        }

        // 다이얼로그 생성 및 표시
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

    }

    fun showPopupMessage(context: Context, title: String, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}