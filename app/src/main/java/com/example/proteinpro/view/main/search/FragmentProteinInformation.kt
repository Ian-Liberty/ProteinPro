package com.example.proteinpro.view.main.search
import android.util.Log

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.proteinpro.R
import com.example.proteinpro.databinding.FragmentProteinInformationBinding
import com.example.proteinpro.util.Class.food.FoodInformationItem
import com.example.proteinpro.util.PreferenceHelper
import com.example.proteinpro.util.Retrofit.FoodRetrofitHelper


class FragmentProteinInformation(private val foodData: FoodInformationItem) : Fragment() {
    private lateinit var foodInformationActivity: ActivityFoodInformation

    // 변수 입력
    private lateinit var binding: FragmentProteinInformationBinding
    // 뷰 객체

    // 유틸 클래스

    private lateinit var preferenceHelper: PreferenceHelper
    private lateinit var FoodRetrofitHelper : FoodRetrofitHelper

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
        binding = FragmentProteinInformationBinding.inflate(inflater, container, false)

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

        val buttonContainer = binding.proteinDataFBL

        if(foodData.proteinList.isEmpty()){

        }else{
            val firstItem = foodData.proteinList.get(0)

            binding.titleTV.setText("💡 ${firstItem.name}")
            binding.descTV.setText("${firstItem.explanation}")
        }


        for(item in foodData.proteinList){

            val button = AppCompatButton(foodInformationActivity)
            val layoutParams = LinearLayout.LayoutParams(
                180.dpToPx(), // 가로 크기를 180dp로 설정
                40.dpToPx()   // 세로 크기를 40dp로 설정
            )

            layoutParams.setMargins(5, 5, 5, 5)
            button.layoutParams = layoutParams
            button.background = ContextCompat.getDrawable(foodInformationActivity, R.drawable.btn_ripple)
            button.text = item.name
            button.setTextColor(ContextCompat.getColorStateList(foodInformationActivity, R.drawable.txt_color))
            button.textSize = 16f // sp 단위이므로 f를 붙여줍니다
            button.setPadding(10.dpToPx(), 0, 10.dpToPx(), 0) // 가로 패딩을 추가
            button.setOnClickListener {
                // 버튼이 클릭되었을 때의 동작을 정의
                // 예를 들어, 해당 버튼에 대한 상세 정보를 표시하거나 다른 동작을 수행할 수 있습니다
                Log.i ("${item.name}", "버튼 클릭!")
                binding.titleTV.setText("💡 ${item.name}")
                binding.descTV.setText("${item.explanation}")

            }

            button.layoutParams = layoutParams // 크기 조절이 적용된 LayoutParams를 버튼에 다시 설정

            buttonContainer.addView(button)



        }



    }
    // 유틸 클래스 할당
    private fun initUtils() {

        preferenceHelper = PreferenceHelper(foodInformationActivity)
        FoodRetrofitHelper = FoodRetrofitHelper(foodInformationActivity)

    }

    // dp를 px로 변환하는 확장 함수
    fun Int.dpToPx(): Int {
        return (this * Resources.getSystem().displayMetrics.density).toInt()
    }


}