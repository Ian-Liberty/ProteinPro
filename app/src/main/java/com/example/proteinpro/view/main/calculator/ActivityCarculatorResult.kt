package com.example.proteinpro.view.main.calculator

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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

    private lateinit var help_iv : ImageView
    private lateinit var bmiHelp_iv : ImageView
    private lateinit var intakeHelp_iV : ImageView



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

        intakeHelp_iV = binding.IntakeHelpIV
        bmiHelp_iv = binding.BMIHelpIV
        help_iv = binding.helpIV

    }
    private fun initListener(){
        // 리스너 초기화

        help_iv.setOnClickListener {
            showPopupMessage(this, "",
            "하루 탄단지 섭취량 계산은  Harris-Benedict(해리스-베네딕트 방정식)을 이용하여 기초대사량(BMR)을 계산과 활동량을 토대로 탄단지 비율에 맞게 계산됩니다."
                )
        }
        bmiHelp_iv.setOnClickListener {
            showPopupMessage(this, "",
                "BMI(Body Mass Index)란?\n" +
                        "인간의 비만도를 나타내는 지수로, 체중과 키의 관계로 계산됩니다."
            )

        }
        intakeHelp_iV.setOnClickListener {
            showPopupMessage(this, "",
                "● 탄수화물을 적게 섭취하면 지방이 에너지원으로 사용되어 효과적으로 체지방을 줄일 수 있어요.\n" +"\n"+
                        "● 단백질은 장기간 과잉 섭취했을 때 대사질환 발생 위험이 높아질 수 있어요. 체중2배(kg당 2g)이상을 목표로 잡는 경우 매년 검진으로 신과 간기능, 대사질환 관련 수치를 확인하고 조절해주세요.\n" +"\n"+
                        "● 지방은 탄수화물, 단백질에 비해 칼로리가 높아 체지방으로 쌓이기 쉬우니 20% 정도로 조절하는 좋아요."
            )

        }

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

    fun showPopupMessage(context: Context, title: String, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("확인") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}