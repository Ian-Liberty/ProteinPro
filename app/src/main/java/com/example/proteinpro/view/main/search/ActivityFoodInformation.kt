package com.example.proteinpro.view.main.search
import android.content.Context
import android.util.Log

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import com.bumptech.glide.Glide
import com.example.proteinpro.R
import com.example.proteinpro.databinding.ActivityFoodInformationBinding
import com.example.proteinpro.util.Class.food.AdditiveItem
import com.example.proteinpro.util.Class.food.FoodInformationItem
import com.example.proteinpro.util.PreferenceHelper
import com.example.proteinpro.util.Retrofit.FoodRetrofitHelper
import com.example.proteinpro.util.Retrofit.ServerData
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

        binding.scrollView.visibility = View.GONE
        val Activity = this

        initUtils()
        initData(Activity)



    }

    private fun initData(Activity: ActivityFoodInformation) {
        val key =  intent.getStringExtra("food_key").toString()
        val token = preferenceHelper.get_jwt_Token()

            foodRetrofitHelper.getFoodData(key,token, object : FoodRetrofitHelper.FoodDataCallback {
                override fun onSuccess(foodData: FoodInformationItem) {
                    Log.i ("getFoodData", ""+foodData)


                    binding.scrollView.visibility = View.VISIBLE
                    viewPager2Adapter = ViewPager2Adapter(Activity, foodData)
                    binding.viewPager.adapter = viewPager2Adapter

                    setData(foodData)
                    initViews()

                }

                override fun onFailure() {

                    val alertDialogBuilder = AlertDialog.Builder(this@ActivityFoodInformation)
                    alertDialogBuilder.setTitle("알림")
                    alertDialogBuilder.setMessage("해당 제품에 대한 정보가 없습니다.")
                    alertDialogBuilder.setPositiveButton("확인") { dialog, _ ->
                        dialog.dismiss() // 확인 버튼 누르면 알림창 닫기
                        // 뒤로 가기 액션 추가
                        onBackPressed()
                    }
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()

                }

            })
    }

    fun setData(foodData: FoodInformationItem) {
        //title
        binding.foodNameTV.setText(foodData.name)

        //protein_box
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


            for (text in foodData.proteinTypeList) {

                val inflatedLayout = inflater.inflate(R.layout.protein_type_box, binding.typeBoxLL, false)
                val textView = inflatedLayout.findViewById<TextView>(R.id.typebox_TV)
                textView.text = text
                binding.typeBoxLL.addView(inflatedLayout)

            }
        //메인 이미지
        Glide.with(this).load(ServerData.img_URL+"셀렉스프로틴복숭아.JPG").into(binding.foodImgIV)

//        Glide.with(this).load(ServerData.img_URL+foodData.image).into(binding.foodImgIV)
        //태그

        var tag :String = ""
        for(i in foodData.tagList){
            Log.i ("foodData.tagList", ""+foodData.tagList+ "i $i")
            tag = tag+" #$i"

        }

        binding.nutrientsTagTV.setText(tag)
        binding.companyTV.setText("회사명: ${foodData.company}")
        binding.brandTV.setText("브랜드: ${foodData.brand}")
        binding.capacityTV.setText("용량: ${foodData.capacity} ${foodData.capacityUnit}")
        binding.numberTV.setText("개수: ${foodData.quantity}개")
        binding.priceTV.setText("가격: ${foodData.price}원")
        binding.pricePerGramOfProteinTV.setText("단백질 1g 당 ${foodData.costPerformance}원")

        // 첨가물 계산
        val additiveList = foodData.additiveList

        if(additiveList.size != 0){
            binding.countTv.setText("${additiveList.size} 개")
        }else{
            binding.countTv.setText("없음")
        }

        var nullCount =0
        var LowHazardCount =0
        var ModerateHazardCount = 0
        var HighHazard = 0

        for(item in additiveList){
            var gradeStr =  AdditiveItem.getEwgGradeText(item.ewgGrade)
            when(gradeStr) {
                "미등록"-> nullCount ++
                "안전" -> LowHazardCount++
                "주의" -> ModerateHazardCount++
                "위험" -> HighHazard++
            }
        }

        fun setCount(count : Int, TV: TextView, IV: ImageView){
            if(count != 0){

                TV.setText(count.toString())
                IV.visibility= View.VISIBLE
                TV.visibility= View.VISIBLE
            }else{

                IV.visibility= View.GONE
                TV.visibility= View.GONE

            }
        }

        setCount(nullCount, binding.nullCountTV, binding.nullCountIV)
        setCount(LowHazardCount, binding.LowHazardCountTV, binding.LowHazardCountIV)
        setCount(ModerateHazardCount, binding.ModerateHazardCountTV, binding.ModerateHazardCountIV)
        setCount(HighHazard, binding.HighHazardTV, binding.HighHazardIV)

        //알레르기


        if(foodData.allergyListStr =="null"){

        }else{
            binding.allergyListTV.setText(foodData.allergyListStr)
        }

        // ai 리뷰
        val goodWordList = foodData.positiveReviewWords.split(",")
        val badWordList = foodData.positiveReviewWords.split(",")
        for (word in goodWordList) {
            createAndAddTextView(word, 0)
        }
        for (word in badWordList) {
            createAndAddTextView(word, 1)
        }

    }

    private fun setViewPager(){


    }
    private fun initViews(){
        // 뷰 초기화

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
    }
    private fun initListener(){
        // 리스너 초기화
    }
    private fun initUtils(){
        // 유틸 클래스 초기화

        foodRetrofitHelper = FoodRetrofitHelper(this)
        preferenceHelper = PreferenceHelper(this)

    }

    /***
     * @param word
     * 단어
     *
     * @param emotion
     * 감정 상태
     * 0 은 긍정
     * 1 은 부정
     *
     * 각 상태에 따라 다른 색으로 표현
     *
     */
    private fun createAndAddTextView(word: String, emotion: Int ) {
        val context: Context = this
        lateinit var layout: ViewGroup

        // 새로운 TextView 생성
        val textView = TextView(context)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(Math.round(5*resources.displayMetrics.density))// 마진값 dp 단위
        textView.layoutParams = layoutParams

        if(emotion == 0){
            // 긍정
            layout = binding.goodFBL
            textView.setBackgroundResource(R.drawable.round_background_border_green)
            layout.addView(textView)

        }else if(emotion == 1){
            //부정
            layout = binding.badFBL
            textView.setBackgroundResource(R.drawable.round_background_border_red)
            layout.addView(textView)
        }else{
            // 값 없음
        }

        textView.setPadding(Math.round(5*resources.displayMetrics.density))// 패딩값 dp 단위
        textView.text = word

    }


}