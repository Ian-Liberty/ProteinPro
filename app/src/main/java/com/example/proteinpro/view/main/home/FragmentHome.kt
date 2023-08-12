package com.example.proteinpro.view.main.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proteinpro.R
import com.example.proteinpro.databinding.FragmentHomeBinding
import com.example.proteinpro.util.Class.AdapterType
import com.example.proteinpro.util.Class.ViewType
import com.example.proteinpro.util.PreferenceHelper
import com.example.proteinpro.util.RecyclerView.FoodItem
import com.example.proteinpro.util.RecyclerView.FoodListAdapter
import com.example.proteinpro.util.Retrofit.FoodRetrofitHelper
import com.example.proteinpro.view.main.MainActivity
import com.google.gson.JsonArray
import com.google.gson.JsonObject

class FragmentHome : Fragment() {
    //컨텍스트 변수
    private lateinit var mainActivity: MainActivity

    // 뷰 객체

    private lateinit var binding: FragmentHomeBinding
    //
    private lateinit var protein_search_sv: androidx.appcompat.widget.SearchView

    //

    private lateinit var popularProtein_rv : RecyclerView
    private lateinit var valueForMoney_rv : RecyclerView
    private lateinit var recentProtein_rv : RecyclerView
    //
    private lateinit var popularProtein_tv : TextView
    private lateinit var valueForMoney_tv : TextView
    private lateinit var recentProtein_tv : TextView

    //
    private lateinit var popularProteinAdapter : FoodListAdapter
    private lateinit var valueForMoneyAdapter: FoodListAdapter
    private lateinit var recentProteinAdapter: FoodListAdapter

    //
    //유틸 클래스 선언
    private lateinit var foodRetrofitHelper: FoodRetrofitHelper
    private lateinit var preferenceHelper: PreferenceHelper
    //

    private var recentList = ArrayList<FoodItem>()
    private var popularList = ArrayList<FoodItem>()
    private var valueForMoneyList = ArrayList<FoodItem>()

    private var ramdom = Math.random()

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
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.i ("onViewCreated", "random: $ramdom")

        initUtils()
        initData()

        initViews()
        setRecyclerview()
        initListener()

        Log.i ("recentList.size", ""+recentList.size)

    }
    private fun initData() {

        if(recentList.size == 0){
            getSampleProteinList(1)
        }
        if(popularList.size == 0){
            getSampleProteinList(3)
        }
        if(valueForMoneyList.size == 0){
            getSampleProteinList(2)
        }

    }
    private fun setRecyclerview() {

            //1최근
            recentProtein_rv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            recentProteinAdapter = FoodListAdapter(mainActivity, recentList, AdapterType.RECENT_FOOD_LIST)
            recentProtein_rv.adapter = recentProteinAdapter

            //2가성비
            valueForMoney_rv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            valueForMoneyAdapter = FoodListAdapter(mainActivity, valueForMoneyList, AdapterType.VALUE_FOR_MONEY_LIST)
            valueForMoney_rv.adapter = valueForMoneyAdapter

            //3인기
            popularProtein_rv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            popularProteinAdapter = FoodListAdapter(mainActivity ,popularList, AdapterType.POPULAR_FOOD_LIST)
            popularProtein_rv.adapter = popularProteinAdapter

    }
    private fun getSampleProteinList(order: Int){

        if(order == 1){
            recentList.clear()

        }else if(order == 2){
            valueForMoneyList.clear()

        }else{// order ==3
            popularList.clear()

        }


        foodRetrofitHelper.searchMainFoodList(order, object : FoodRetrofitHelper.MainFoodListCallback {

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

                    val fooditem = FoodItem(key, name, taste, price,capacity, capacityUnit, image, costPerformance, quantity, brand, ViewType.FOOD_CARDVIEW_TYPE)

                    if(order == 1){

                        recentList.add(fooditem)

                    }else if(order == 2){

                        valueForMoneyList.add(fooditem)

                    }else{// order ==3

                        popularList.add(fooditem)

                    }

                }

                val more_btn_item = FoodItem(viewType = ViewType.MORE_BUTTON_TYPE)
                if(order==1){

                    recentList.add(more_btn_item)
                    recentProteinAdapter.setItem_list(recentList)
                }else if(order == 2){
                    valueForMoneyList.add(more_btn_item)
                    valueForMoneyAdapter.setItem_list(valueForMoneyList)
                }else {
                    popularList.add(more_btn_item)
                    popularProteinAdapter.setItem_list(popularList)
                }

            }

            override fun onFailure() {
                // 요청 실패 처리
                // 예시: 에러 메시지 표시 등
                Log.i ("initData", "데이터 받아오기 실패")

            }

        }
        )

    }

    private fun initViews(){
        // 뷰 초기화
        //리사이클러뷰
       popularProtein_rv= binding.popularProteinRV
       recentProtein_rv= binding.recentProteinRV
       valueForMoney_rv= binding.valueForMoneyRV

        //더보기 텍스트뷰
        popularProtein_tv = binding.popularProteinMoreTV
        recentProtein_tv = binding.recentProteinMoreTV
        valueForMoney_tv = binding.valueForMoneyMoreTV

        // 검색창
        protein_search_sv = binding.proteinSV

    }
    private fun initListener(){
        // 리스너 초기화

       val recent_rv_Listener = object : FoodListAdapter.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                Log.i ("recent_rv_Listener", "itemclick : "+position )
            }

            override fun onMoreClick(v: View?, position: Int) {
                Log.i ("recent_onMoreClick", "recentmore : "+v )
            }
        }
        val popular_rv_Listener = object  : FoodListAdapter.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                Log.i ("popular_rv_Listener", "itemclick : "+position )
            }

            override fun onMoreClick(v: View?, position: Int) {
                Log.i ("popular_onMoreClick", "recentmore : "+v )
            }

        }
        val valueForMoney_rv_Listener = object  : FoodListAdapter.OnItemClickListener{
            override fun onItemClick(v: View?, position: Int) {
                Log.i ("valueForMoney_rv_Listener", "itemclick : "+position )
            }

            override fun onMoreClick(v: View?, position: Int) {
                Log.i ("valueForMoney_onMoreClick", "recentmore : "+v )
            }

        }
        recentProteinAdapter.setOnItemClickListener(recent_rv_Listener)
        valueForMoneyAdapter.setOnItemClickListener(valueForMoney_rv_Listener)
        popularProteinAdapter.setOnItemClickListener(popular_rv_Listener)


//        protein_search_sv.setOnClickListener {
//            (activity as MainActivity?)!!.bottomNavigationView.selectedItemId = R.id.search_menu
//        }

        protein_search_sv.setOnSearchClickListener {

        }

        protein_search_sv.setOnQueryTextFocusChangeListener{ protein_search_sv, hasFocus ->
            if(hasFocus) {
                // 포커스 가지고 있는경우
                (activity as MainActivity?)!!.bottomNavigationView.selectedItemId = R.id.search_menu
                protein_search_sv.clearFocus()
            }
        }




    }
    private fun initUtils(){
        // 유틸 클래스 초기화
        foodRetrofitHelper = FoodRetrofitHelper(mainActivity)
        preferenceHelper = PreferenceHelper(mainActivity)

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