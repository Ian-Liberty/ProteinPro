package com.example.proteinpro.view.main.search
import android.util.Log

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proteinpro.R
import com.example.proteinpro.databinding.FragmentNutrientBinding
import com.example.proteinpro.util.Class.food.FoodInformationItem
import com.example.proteinpro.util.Class.food.NutritionFacts
import com.example.proteinpro.view.main.MainActivity
import kotlin.math.pow


/***
 * 영양 성분 표시 프래그먼(ActivityFoodInformation 에서 ViewPager2Adapter 이용해서 사용중)
 */
class FragmentNutrient(private val foodData: FoodInformationItem) : Fragment() {
   // 변수 입력
   private lateinit var binding: FragmentNutrientBinding
    private lateinit var foodInformationActivity: ActivityFoodInformation
   // 뷰 객체

   // 유틸 클래스

    override fun onAttach(context: Context) {
        super.onAttach(context)
        foodInformationActivity = context as ActivityFoodInformation

    }

   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)

   }

   override fun onCreateView(
       inflater: LayoutInflater, container: ViewGroup?,
       savedInstanceState: Bundle?
   ): View? {
       binding = FragmentNutrientBinding.inflate(inflater, container, false)

       return binding.root

   }
   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       super.onViewCreated(view, savedInstanceState)

       initUtils()
       initData()
       initListener()

   }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

   // 리스너 선언
   private fun initListener() {

   }

   // 현재 프래그먼트에서 필요한 데이터 받아오기
   private fun initData() {

       val nutritionFacts = foodData.nutritionFacts

       binding.tagTV.setText(getNutritionTag(nutritionFacts))

       setNutritionData(nutritionFacts)

   }
   // 유틸 클래스 할당
   private fun initUtils() {

   }

    fun getNutritionTag(nutritionFacts: NutritionFacts) : String {

        var strData : String = ""

        if(nutritionFacts.sodium == 0.0){
            strData = strData+"#NO_나트륨 "
        }
        if(nutritionFacts.carbohydrates == 0.0){
            strData = strData+"#NO_탄수화물 "
        }
        if(nutritionFacts.sugars == 0.0){
            strData = strData+"#NO_당류 "
        }
        if(nutritionFacts.fat == 0.0){
            strData = strData+"#NO_지방 "
        }
        if(nutritionFacts.transFat == 0.0){
            strData = strData+"#NO_트랜스지방 "
        }
        if(nutritionFacts.saturatedFat == 0.0){
            strData = strData+"#NO_포화지방 "
        }
        if(nutritionFacts.cholesterol == 0.0){
            strData = strData+"#NO_콜레스테롤 "
        }

        Log.i ("getNutritionTag", "getNutritionTag: $strData")
        return strData

    }

    fun setNutritionData(nutritionFacts: NutritionFacts){
        // 1일권장 영양소 기준 퍼센트 계산
        Log.i ("setNutritionData", "NutritionFacts: $nutritionFacts")

        val productValues = NutritionalValues(
            nutritionFacts.protein,
            nutritionFacts.sodium,
            nutritionFacts.carbohydrates,
            nutritionFacts.sugars,
            nutritionFacts.fat,
            nutritionFacts.transFat,
            nutritionFacts.saturatedFat,
            nutritionFacts.cholesterol
        )
        val percentageValues = calculateNutrientPercentages(productValues)

        // 제공량
        binding.servingSizeTV.setText("1회 제공량(${nutritionFacts.servingSize.toInt()}${foodData.capacityUnit})")
        // 칼로리
        binding.kcalTV.setText("${nutritionFacts.calories.round(1)} Kcal")

        // 각 성분 그램수 표시
        binding.proteinTV.setText("단백질(${nutritionFacts.protein}g)")
        binding.sodiumTV.setText("나트륩(${nutritionFacts.sodium}mg)")
        binding.carbohTV.setText("탄수화물(${nutritionFacts.carbohydrates}g)")
        binding.sugarTV.setText("당류(${nutritionFacts.sugars}g)")
        binding.fatTV.setText("지방(${nutritionFacts.fat}g)")
        binding.transFatTV.setText("트랜스지방(${nutritionFacts.transFat}g)")
        binding.saturatedFatTV.setText("포화지방(${nutritionFacts.saturatedFat}g)")
        binding.cholesterolTV.setText("콜레스테롤(${nutritionFacts.cholesterol}mg)")

        // 각 성분 프로그래스 바 표시
        binding.proteinPB.progress = (percentageValues.protein * 10).toInt()
        binding.sodiumPB.progress = (percentageValues.sodium * 10).toInt()
        binding.carbohPB.progress = (percentageValues.carbohydrates * 10).toInt()
        binding.sugarPB.progress = (percentageValues.sugars * 10).toInt()
        binding.fatPB.progress = (percentageValues.fat * 10).toInt()
        binding.transFatPB.progress = (percentageValues.transFat * 10).toInt()
        binding.saturatedFatPB.progress = (percentageValues.saturatedFat * 10).toInt()
        binding.cholesterolPB.progress = (percentageValues.cholesterol * 10).toInt()

        // 각 성분 퍼센트 입력
        binding.proteinPercentTV.setText("${percentageValues.protein}%")
        binding.sodiumPercentTV.setText("${percentageValues.sodium}%")
        binding.carbohPercentTV.setText("${percentageValues.carbohydrates}%")
        binding.sugarPercentTV.setText("${percentageValues.sugars}%")
        binding.fatPercentTV.setText("${percentageValues.fat}%")
        binding.transFatPercentTV.setText("${percentageValues.transFat}%")
        binding.saturatedFatPercentTV.setText("${percentageValues.saturatedFat}%")
        binding.cholesterolPercentTV.setText("${percentageValues.cholesterol}%")

    }

    data class NutritionalValues(
        val protein: Double,
        val sodium: Double,
        val carbohydrates: Double,
        val sugars: Double,
        val fat: Double,
        val transFat: Double,
        val saturatedFat: Double,
        val cholesterol: Double
    )

    fun calculateNutrientPercentages(values: NutritionalValues): NutritionalValues {
        // 1일 영양성분 기준치당 퍼센트 계산
        val baseCalories = 2000.0 // 기준 칼로리 (2000kcal)

        val proteinPercentage = ((values.protein / 55.0) * 100.0).round(1)
        val sodiumPercentage = ((values.sodium / 2000.0) * 100.0).round(1)
        val carbohydratesPercentage = ((values.carbohydrates / 324.0) * 100.0).round(1)
        val sugarsPercentage = ((values.sugars / 100.0) * 100.0).round(1)
        val fatPercentage = ((values.fat / 54.0) * 100.0).round(1)
        val transFatPercentage = ((values.transFat / 2000.0) * 100.0).round(1)
        val saturatedFatPercentage = ((values.saturatedFat / 15.0) * 100.0).round(1)
        val cholesterolPercentage = ((values.cholesterol / 300.0) * 100.0).round(1)

        return NutritionalValues(
            proteinPercentage,
            sodiumPercentage,
            carbohydratesPercentage,
            sugarsPercentage,
            fatPercentage,
            transFatPercentage,
            saturatedFatPercentage,
            cholesterolPercentage
        )
    }

    fun Double.round(decimals: Int): Double {
        val multiplier = 10.0.pow(decimals.toDouble())
        return kotlin.math.round(this * multiplier) / multiplier
    }
}