package com.example.proteinpro.view.main.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.example.proteinpro.R
import com.example.proteinpro.databinding.ActivityCarculatorResultBinding
import com.example.proteinpro.util.Class.HarrisBenedict1919Calculator

class ActivityCarculatorResult : AppCompatActivity() {


    lateinit var kcal_tv : TextView
    lateinit var carbohydrate_tv : TextView
    lateinit var protein_tv : TextView
    lateinit var fat_tv : TextView
    lateinit var bmi_tv : TextView
    //
    lateinit var intakeHelp_iv : ImageView
    lateinit var bmiHelp_iv : ImageView


    // 전역 변수로 바인딩 객체 선언
    private var mBinding:
            ActivityCarculatorResultBinding? =null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!
    //!!는 Kotlin에서 Nullable 타입을 강제로 Non-nullable 타입으로 변환하는 것을 의미



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carculator_result)

        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용해서
        // 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivityCarculatorResultBinding.inflate(layoutInflater)
        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의
        // 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시 합니다.
        setContentView(binding.root)// < 기존의 setContentView 는 주석 처리해 주세요!

        val gender : String = intent.getStringExtra("gender").toString()
        val userBirth = intent.getStringExtra("userBirth").toString()
        val heightCm = intent.getIntExtra("heightCm",-1)
        val weightKg = intent.getIntExtra("weightKg" ,-1)
        val activityLevel = HarrisBenedict1919Calculator.ActivityLevel.valueOf(intent.getStringExtra("activityLevel").toString())

        val calculator = HarrisBenedict1919Calculator(gender, userBirth, heightCm, weightKg, activityLevel)

        val Calories = calculator.calculateRecommendedCalories()

        val (carbs, protein, fat) = calculator.calculateRecommendedNutrients()

        val bmi = calculator.calculateBMI()
        val bmiClassification = calculator.classifyBMI()

        Log.i ("계산결과", "전체 칼로리: ${Calories}, 탄수화물: ${carbs}g, 단백질:${protein}g, 지방:${fat}g, BMI: ${bmi}, $bmiClassification")


        initViews()
        initListener()
        initUtils()
        initData(Calories, carbs, protein, fat, bmi, bmiClassification)

    }

    private fun initViews(){
        // 뷰 초기화

        kcal_tv = binding.kcalTV
        carbohydrate_tv = binding.carbohydrateTV
        protein_tv = binding.proteinTV
        fat_tv = binding.fatTV
        bmi_tv = binding.BMITV

        intakeHelp_iv = binding.IntakeHelpIV
        bmiHelp_iv = binding.BMIHelpIV

    }
    private fun initListener(){
        // 리스너 초기화

    }
    private fun initUtils(){
        // 유틸 클래스 초기화

    }
    private fun initData(Calories:Int, carbs:Int, protein:Int, fat:Int, bmi:Double, bmiClassification:String) {
        Log.i ("계산결과", "전체 칼로리: ${Calories}, 탄수화물: ${carbs}g, 단백질:${protein}g, 지방:${fat}g, BMI: ${bmi}, $bmiClassification")

        // 칼로리 표시


        val kcal_str = "하루 섭취 칼로리 "
        val kcal_str2 = "${Calories}Kcal"
        val kcal_spannable = spanning(kcal_str,kcal_str2)

        kcal_tv.setText(kcal_spannable, TextView.BufferType.SPANNABLE)

        // 탄단지 표시

        val carb_str1 = "탄수화물 "
        val pro_str1 = "단백질 "
        val fat_str1 = "지방 "

        val carb_str2 = "${carbs}g"
        val pro_str2 = "${protein}g"
        val fat_str2 = "${fat}g"

        carbohydrate_tv.setText(spanning(carb_str1,carb_str2), TextView.BufferType.SPANNABLE)
        protein_tv.setText(spanning(pro_str1,pro_str2), TextView.BufferType.SPANNABLE)
        fat_tv.setText(spanning(fat_str1,fat_str2), TextView.BufferType.SPANNABLE)


        // BMI 지수 표시

        val bmi_str1 = "BMI 지수 "
        val bmi_str2 = "${bmi}(${bmiClassification})"

        bmi_tv.setText(spanning(bmi_str1,bmi_str2), TextView.BufferType.SPANNABLE)

    }

    private fun spanning(str1:String, str2:String) : SpannableString{
        val color = getColor(R.color.blue)

        val spannable = SpannableString("$str1$str2")
        spannable.setSpan(ForegroundColorSpan(color), str1.length, str1.length + str2.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        return spannable

    }
}