package com.example.proteinpro.view.main.search
import android.util.Log

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.proteinpro.databinding.ActivityFoodInformationBinding
import com.example.proteinpro.util.Class.food.FoodInformationItem
import com.example.proteinpro.util.PreferenceHelper
import com.example.proteinpro.util.Retrofit.FoodRetrofitHelper
import com.example.proteinpro.util.ViewPager2Adapter.ViewPager2Adapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ActivityFoodInformation : AppCompatActivity() {

    private lateinit var viewPager2Adapter: ViewPager2Adapter

    // 전역 변수로 바인딩 객체 선언
    private var mBinding:
    ActivityFoodInformationBinding? =null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!
    //!!는 Kotlin에서 Nullable 타입을 강제로 Non-nullable 타입으로 변환하는 것을 의미

    // 유틸 클래스
    private lateinit var foodRetrofitHelper: FoodRetrofitHelper
    private lateinit var preferenceHelper: PreferenceHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_food_information)

        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용해서
        // 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivityFoodInformationBinding.inflate(layoutInflater)
        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의
        // 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시 합니다.
        setContentView(binding.root)// < 기존의 setContentView 는 주석 처리해 주세요!

        viewPager2Adapter = ViewPager2Adapter(this)
        binding.viewPager.adapter = viewPager2Adapter



        binding.tabLayout.addOnTabSelectedListener(object  : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }


        })

        TabLayoutMediator(binding.tabLayout, binding.viewPager) {tab, position ->
            when(position){
                0 -> tab.text = "영양성분"
                1 -> tab.text = "단백질정보"
                2 -> tab.text = "원료정보"
            }
        }.attach()

        initUtils()
        initData()
    }

    private fun initData() {
      val key =  intent.getStringExtra("food_key").toString()

            foodRetrofitHelper.getFoodData(key, object : FoodRetrofitHelper.FoodDataCallback {
                override fun onSuccess(foodData: FoodInformationItem) {
                    Log.i ("getFoodData", ""+foodData)

                }

                override fun onFailure() {

                }


            })
    }

    private fun setViewPager(){


    }
    private fun initViews(){
        // 뷰 초기화
    }
    private fun initListener(){
        // 리스너 초기화
    }
    private fun initUtils(){
        // 유틸 클래스 초기화

        foodRetrofitHelper = FoodRetrofitHelper(this)

    }


}