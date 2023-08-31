package com.example.proteinpro.view.main.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proteinpro.R
import com.example.proteinpro.databinding.ActivityMoreListBinding
import com.example.proteinpro.util.Class.AdapterType
import com.example.proteinpro.util.Class.ViewType
import com.example.proteinpro.util.PreferenceHelper
import com.example.proteinpro.util.RecyclerView.FoodItem
import com.example.proteinpro.util.RecyclerView.FoodListAdapter
import com.example.proteinpro.util.Retrofit.FoodRetrofitHelper
import com.example.proteinpro.util.Retrofit.RetrofitHelper
import com.example.proteinpro.view.main.search.ActivityFoodInformation
import com.google.gson.JsonArray
import com.google.gson.JsonObject

class ActivityMoreList : AppCompatActivity() {

    //유틸 클래스 선언
    private lateinit var foodretrofitHelper: FoodRetrofitHelper
    private lateinit var preferenceHelper: PreferenceHelper

    //

    private var foodDataList = ArrayList<FoodItem>()
    private lateinit var adater : FoodListAdapter

    // 전역 변수로 바인딩 객체 선언
    private var mBinding:
            ActivityMoreListBinding? =null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!
    //!!는 Kotlin에서 Nullable 타입을 강제로 Non-nullable 타입으로 변환하는 것을 의미

    private lateinit var title:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_more_list)

        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용해서
        // 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivityMoreListBinding.inflate(layoutInflater)
        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의
        // 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시 합니다.
        setContentView(binding.root)// < 기존의 setContentView 는 주석 처리해 주세요!


        initUtils()
        initData()
        initViews()
        initListener()


    }

    private fun initData() {

        title = intent.getStringExtra("category").toString()

        foodDataList.clear()

        foodretrofitHelper.getHomeFoodList(title, object : FoodRetrofitHelper.MainFoodListCallback{
            override fun onSuccess(foodList: JsonArray) {
                // 받아온 리스트를 이용하여 작업 수행
                // 예시: RecyclerView에 리스트를 바인딩하는 등
                Log.i ("initData",  ""+foodList.size()+" 개의 데이터 가져오기 성공 : $foodList")

                for(jsonElement in foodList){
                    Log.i ("데이터 파싱중", ""+jsonElement)
                    val item = jsonElement.asJsonObject

                    val key = getStringFromJson(item, "키")
                    val name = getStringFromJson(item, "이름")
                    val taste = getStringFromJson(item, "맛")
                    val price = getIntFromJson(item, "가격")
                    val capacity = getIntFromJson(item, "용량")
                    val capacityUnit = getStringFromJson(item, "단위")
                    val image = getStringFromJson(item, "이미지")
                    val costPerformance = getFloatFromJson(item, "가성비")
                    val quantity = getIntFromJson(item, "수량")
                    val brand = getStringFromJson(item, "브랜드")

                    val fooditem = FoodItem(key, name, taste, price,capacity, capacityUnit, image, costPerformance, quantity, brand, ViewType.FOOD_LISTVIEW_TYPE)

                    foodDataList.add(fooditem)

                }
                adater.setItem_list(foodDataList)
            }

            override fun onFailure() {


            }


        })

    }

    private fun initViews(){

        binding.title.setText(title)

        var adapterType :Int =AdapterType.RECENT_FOOD_LIST

        if(title=="최근 등록된 단백질"){
            adapterType = AdapterType.RECENT_FOOD_LIST
        }else if(title == "가성비 단백질"){
            adapterType == AdapterType.VALUE_FOR_MONEY_LIST
        }else if(title == "인기 단백질"){
            adapterType == AdapterType.POPULAR_FOOD_LIST
        }else{
            adapterType = AdapterType.RECENT_FOOD_LIST
        }

        binding.moreDataRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adater = FoodListAdapter(this, foodDataList, adapterType)
        binding.moreDataRV.adapter = adater

    }
    private fun initListener(){
        // 리스너 초기화
        val rv_listener = object : FoodListAdapter.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int, item: FoodItem) {
                Log.i ("recent_rv_Listener", "itemclick : "+position )

                val mIntent = Intent(getApplicationContext(), ActivityFoodInformation::class.java)
                val key = item.key
                mIntent.putExtra("food_key", key)
                startActivity(mIntent)
            }

            override fun onMoreClick(v: View?, position: Int, moreAdapter: Int) {

            }
        }

        adater.setOnItemClickListener(rv_listener)

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

    }
    private fun initUtils(){
        // 유틸 클래스 초기화

        preferenceHelper = PreferenceHelper(this)
        foodretrofitHelper = FoodRetrofitHelper(this)

    }

    private fun getStringFromJson(jsonObject: JsonObject, key: String): String {
        return if (jsonObject.has(key) && !jsonObject.get(key).isJsonNull) {
            jsonObject.get(key).asString
        } else {
            ""
        }
    }

    private fun getIntFromJson(jsonObject: JsonObject, key: String): Int {
        return if (jsonObject.has(key) && !jsonObject.get(key).isJsonNull) {
            jsonObject.get(key).asInt
        } else {
            0
        }
    }

    private fun getFloatFromJson(jsonObject: JsonObject, key: String): Float {
        return if (jsonObject.has(key) && !jsonObject.get(key).isJsonNull) {
            jsonObject.get(key).asFloat
        } else {
            0.0f
        }
    }

}