package com.example.proteinpro.view.main.search
import android.util.Log
import android.app.Activity.RESULT_OK
import android.content.Intent

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.proteinpro.databinding.FragmentSearchResultBinding
import com.example.proteinpro.util.Class.AdapterType
import com.example.proteinpro.util.Class.FilterSettings
import com.example.proteinpro.util.Class.FoodQuery
import com.example.proteinpro.util.Class.ViewType
import com.example.proteinpro.util.PreferenceHelper
import com.example.proteinpro.util.RecyclerView.FoodItem
import com.example.proteinpro.util.RecyclerView.FoodListAdapter
import com.example.proteinpro.util.Retrofit.FoodRetrofitHelper
import com.example.proteinpro.view.main.MainActivity
import com.example.proteinpro.view.main.home.ActivityMoreList
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.gson.JsonArray
import com.google.gson.JsonObject

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentSearch_result.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentSearch_result : Fragment() {
    private lateinit var getResult: ActivityResultLauncher<Intent>

    //컨텍스트 변수
    private lateinit var mainActivity: MainActivity

    private lateinit var selectedChips: ArrayList<String>
    private lateinit var categoryKey : String
    private lateinit var searchQuery : String

    private val allNames = StringBuilder()
    private val kindNames = StringBuilder()
    private val allergyNames = StringBuilder()
    private val tasteNames = StringBuilder()
    private val etcNames = StringBuilder()

    private var all : String =""
    private var kind : String =""
    private var allergy : String =""
    private var taste : String=""
    private var etc : String=""

    // 변수 입력
    private lateinit var binding: FragmentSearchResultBinding

    // 뷰 객체

    private lateinit var protein_search_sv : SearchView
    private lateinit var filter_btn: Button
    private lateinit var filter_cg: ChipGroup

    private lateinit var back_btn: ImageButton

    // 리사이클러뷰
    private lateinit var result_rv: RecyclerView
    private lateinit var resultAdapter: FoodListAdapter
    private  var resultList = ArrayList<FoodItem>()
    private  var skip: Int =1

    // 유틸 클래스
    private lateinit var foodRetrofitHelper: FoodRetrofitHelper
    private lateinit var preferenceHelper: PreferenceHelper

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity// 컨텍스트 받아오기

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                // 필터에서 받아온 값이 있으면
                if (data != null) {

                    selectedChips = data.getStringArrayListExtra("selectedChips") as ArrayList<String>
                    Log.i("result_filter", "결과값: $selectedChips")
                    chipGeneration()
                    setFilter()

                }
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchResultBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryKey = arguments?.getString("categoryKey") ?: "0"
        searchQuery = arguments?.getString("searchQuery") ?: ""

        initUtils()
        initData()
        initViews()
        setRecyclerview()
        initListener()

    }

    private fun setRecyclerview() {

        //1최근
        result_rv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        resultAdapter = FoodListAdapter(mainActivity, resultList, AdapterType.SEARCH_RESULT_FOOD_LIST)
        result_rv.adapter = resultAdapter

    }

    // 리스너 선언
    private fun initListener() {
        back_btn.setOnClickListener {
            mainActivity.onBackPressed()

        }

        filter_btn.setOnClickListener {

            val mIntent = Intent(mainActivity, ActivitySearchFilter::class.java)

            getResult.launch(mIntent)

        }

        result_rv.setOnScrollChangeListener{ v, scrollX, scrollY, oldScrollX, oldScrollY ->
            // 스크롤 변경 사항이 감지될 때 호출되는 코드
            val layoutManager = result_rv.layoutManager as LinearLayoutManager
            val totalItemCount = layoutManager.itemCount
            val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

            if (lastVisibleItem == totalItemCount - 1) {
                // 스크롤이 최하단에 도달했을 때 처리할 코드
                // 추가 데이터 로드 작업 등을 수행

                skip ++
                initData()

            }
        }

        protein_search_sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                Log.i ("검색 시작", "검색어 : ${query}")

                var fragment_search_result = FragmentSearch_result().apply {
                    arguments = Bundle().apply {
                        putString("searchQuery", query)
                    }
                }
                mainActivity.switchFragment(fragment_search_result,"fragment_search_result")

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {


                return true
            }

        })

        val rv_Listener = object : FoodListAdapter.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int, item: FoodItem) {
                Log.i ("recent_rv_Listener", "itemclick : "+position )

                val mIntent = Intent(mainActivity, ActivityFoodInformation::class.java)
                val key = item.key
                mIntent.putExtra("food_key", key)
                startActivity(mIntent)
            }

            override fun onMoreClick(v: View?, position: Int, adapterType: Int) {

            }

        }

        resultAdapter.setOnItemClickListener(rv_Listener)


    }

    // 뷰 객체 할당
    private fun initViews() {

        filter_btn = binding.filterBTN
        filter_cg = binding.filterCG
        result_rv = binding.resultRV
        protein_search_sv = binding.proteinSV
        protein_search_sv.setQuery(searchQuery ,false)

        back_btn = binding.backBtn


    }

    // 현재 프래그먼트에서 필요한 데이터 받아오기
    private fun initData() {
        Log.i ("initData", "searchQuery.isNullOrEmpty()"+searchQuery+"categoryKey.isNullOrEmpty()"+categoryKey)

        if(searchQuery != ""){
            Log.i ("정보태그", "검색어 검색")

            var foodQuery = FoodQuery(categoryKey, searchQuery,skip,10,kind,etc, allergy, taste, all)
            searchWithQuery(foodQuery)

        }else if(categoryKey !=""){
            Log.i ("정보태그", "카테고리 검색")
            var foodQuery = FoodQuery(categoryKey, searchQuery,skip,10,kind,etc, allergy, taste, all)

            foodRetrofitHelper.getCategoryResult(foodQuery, object : FoodRetrofitHelper.MainFoodListCallback {
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

                        resultList.add(fooditem)

                    }

                    resultAdapter.setItem_list(resultList)

                }

                override fun onFailure() {

                }

            } )
        }else{

        }


        // test 코드임 지워줄것
//      resultList = getSampleProteinList()
    }
    // 유틸 클래스 할당
    private fun initUtils() {

        foodRetrofitHelper = FoodRetrofitHelper(context)

    }

    private fun chipGeneration(){

        val filterList = selectedChips

        for(filter in filterList) {

            val chip = Chip(mainActivity)

            chip.text = filter
            chip.textSize = 16F
            chip.isCloseIconVisible = true
            chip.setOnCloseIconClickListener{
                filter_cg.removeView(chip)
                selectedChips.remove(filter)
                Log.i ("칩 삭제 이벤트", "${filter} Chip 삭제")
                Log.i ("칩 삭제 이벤트", "selectedChips: $selectedChips ")
                setFilter()
            }

            chip.setOnClickListener {
                Log.i ("칩 클릭 이벤트", "이 Chip은 ${filter} Chip 입니다.")
            }

            filter_cg.addView(chip)
        }

    }

//    private fun getSampleProteinList(): ArrayList<FoodItem> {
//        // 임의의 단백질 제품 리스트 생성 (실제로는 서버에서 데이터를 받아와야 함)
//        val itemList = ArrayList<FoodItem>()
//        itemList.add(
//            FoodItem(
//                "P001",
//                "단백질 보충제 A",
//                "초코렛",
//                25000,
//                500,
//                "g",
//                "https://example.com/protein_a.jpg",
//                4.5f,
//                1,
//                "ABC Nutrition",
//                ViewType.FOOD_LISTVIEW_TYPE
//            )
//        )
//        itemList.add(
//            FoodItem(
//                "P002",
//                "단백질 보충제 B",
//                "바닐라",
//                30000,
//                600,
//                "g",
//                "https://example.com/protein_b.jpg",
//                4.2f,
//                1,
//                "XYZ Supplements",
//                ViewType.FOOD_LISTVIEW_TYPE
//
//            )
//        )
//        itemList.add(
//            FoodItem(
//                "P002",
//                "단백질 보충제 B",
//                "바닐라",
//                30000,
//                600,
//                "g",
//                "https://example.com/protein_b.jpg",
//                4.2f,
//                1,
//                "XYZ Supplements",
//                ViewType.FOOD_LISTVIEW_TYPE
//            )
//        )
//        itemList.add(
//            FoodItem(
//                "P002",
//                "단백질 보충제 B",
//                "바닐라",
//                30000,
//                600,
//                "g",
//                "https://example.com/protein_b.jpg",
//                4.2f,
//                1,
//                "XYZ Supplements",
//                ViewType.FOOD_LISTVIEW_TYPE
//            )
//        )
//        itemList.add(
//            FoodItem(
//                "P002",
//                "단백질 보충제 B",
//                "바닐라",
//                30000,
//                600,
//                "g",
//                "https://example.com/protein_b.jpg",
//                4.2f,
//                1,
//                "XYZ Supplements",
//                ViewType.FOOD_LISTVIEW_TYPE
//            )
//        )
//        itemList.add(
//            FoodItem(
//                "P002",
//                "단백질 보충제 B",
//                "바닐라",
//                30000,
//                600,
//                "g",
//                "https://example.com/protein_b.jpg",
//                4.2f,
//                1,
//                "XYZ Supplements",
//                ViewType.FOOD_LISTVIEW_TYPE
//            )
//        )
//        itemList.add(
//            FoodItem(
//                "P002",
//                "단백질 보충제 B",
//                "바닐라",
//                30000,
//                600,
//                "g",
//                "https://example.com/protein_b.jpg",
//                4.2f,
//                1,
//                "XYZ Supplements",
//                ViewType.FOOD_LISTVIEW_TYPE
//            )
//        )
//        itemList.add(
//            FoodItem(
//                "P002",
//                "단백질 보충제 B",
//                "바닐라",
//                30000,
//                600,
//                "g",
//                "https://example.com/protein_b.jpg",
//                4.2f,
//                1,
//                "XYZ Supplements",
//                ViewType.FOOD_LISTVIEW_TYPE
//            )
//        )
//        itemList.add(
//            FoodItem(
//                "P002",
//                "단백질 보충제 B",
//                "바닐라",
//                30000,
//                600,
//                "g",
//                "https://example.com/protein_b.jpg",
//                4.2f,
//                1,
//                "XYZ Supplements",
//                ViewType.FOOD_LISTVIEW_TYPE
//            )
//        )
//
//        // 추가적인 단백질 제품들을 이곳에 추가할 수 있음
//
//        return itemList
//    }

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

    private fun searchWithQuery(foodQuery: FoodQuery) {
        foodRetrofitHelper.getSearchFoodList(foodQuery, object : FoodRetrofitHelper.MainFoodListCallback{
            override fun onSuccess(foodList: JsonArray) {

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

                    resultList.add(fooditem)

                }

                resultAdapter.setItem_list(resultList)
            }

            override fun onFailure() {

            }

        })
    }

    private fun setFilter(){
        val filterSettings = FilterSettings()
        val foundItems =filterSettings.findFilterItemsByKeys(selectedChips)
        allNames.clear()
        kindNames.clear()
        allergyNames.clear()
        tasteNames.clear()
        etcNames.clear()

        all = ""
        kind = ""
        allergy = ""
        taste = ""
        etc = ""

        for(item in foundItems) {
            if(item.type == FilterSettings.FilterType.ALL){
                allNames.append("${item.name},")
            }
            if(item.type == FilterSettings.FilterType.KIND){
                kindNames.append("${item.name},")
            }
            if(item.type == FilterSettings.FilterType.ALLERGY){
                allergyNames.append("${item.name},")
            }
            if(item.type == FilterSettings.FilterType.TASTE){
                tasteNames.append("${item.name},")
            }
            if(item.type == FilterSettings.FilterType.ETC){
                etcNames.append("${item.name},")
            }
        }

        all = allNames.removeSuffix(",").toString()
        kind = kindNames.removeSuffix(",").toString()
        allergy = allergyNames.removeSuffix(",").toString()
        taste = tasteNames.removeSuffix(",").toString()
        etc = etcNames.removeSuffix(",").toString()

        Log.i ("정보태그", "all/{$all} kind/{$kind} allergy/{$allergy} taste/{$taste}  etc/$etc} ")


        // 초기화
        // data
        resultList.clear()
        skip = 1
        initData()

    }

    private fun resetData(){

    }
}
