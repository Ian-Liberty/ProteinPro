package com.example.proteinpro.view.main.search

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.proteinpro.R
import com.example.proteinpro.databinding.FragmentMaterialBinding
import com.example.proteinpro.util.Class.food.AdditiveItem
import com.example.proteinpro.util.Class.food.AdditiveItem.Companion.getEwgGradeText
import com.example.proteinpro.util.Class.food.FoodInformationItem
import com.example.proteinpro.view.main.MainActivity


class FragmentMaterial(private val foodData: FoodInformationItem) : Fragment() {

    //컨텍스트 변수
    private lateinit var mainActivity: MainActivity

    private lateinit var foodInformationActivity: ActivityFoodInformation

    // 변수 입력
    private lateinit var binding: FragmentMaterialBinding
    // 뷰 객체

    // 유틸 클래스

    override fun onAttach(context: Context) {
        super.onAttach(context)

        foodInformationActivity =  context as ActivityFoodInformation

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMaterialBinding.inflate(inflater, container, false)

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

        binding.IntakeHelpIV.setOnClickListener {
            showPopupMessage(foodInformationActivity, "EWG 등급이란?", "미국 비영리 환경시민단체로 자체적으로 화장품 원료의 유해성 조사를 한 후 고시하고 점수를 매겨서 유해성을 표시, 1~10까지 유해도 등급을 설정하여 각 성분의 유해성 등급에 대한 정보를 제공하고 있어요.")
        }

    }

    // 현재 프래그먼트에서 필요한 데이터 받아오기
    private fun initData() {

        val additiveList = foodData.additiveList

        var nullCount =0
        var LowHazardCount =0
        var ModerateHazardCount = 0
        var HighHazard = 0

        val materialListDataLL = binding.materialListDataLL

        // 등급별로 아이템을 분류하기 위한 맵을 생성
        val gradeMap = mutableMapOf<String, MutableList<AdditiveItem>>()

        // 각 등급별 리스트 초기화
        for (grade in listOf("미등록", "안전", "주의", "위험")) {
            gradeMap[grade] = mutableListOf()
        }

        // 등급별로 아이템을 분류
        for (item in additiveList) {
            gradeMap[getEwgGradeText(item.ewgGrade)]?.add(item)
        }

        // 등급에 따라 정렬된 아이템 리스트 생성
        val sortedItems = mutableListOf<AdditiveItem>()
        for (grade in listOf("위험","주의","안전","미등록")) {
            sortedItems.addAll(gradeMap[grade] ?: emptyList())
        }


        for(item in sortedItems){
            val itemView = layoutInflater.inflate(R.layout.additive_item, null)
            var gradeStr =  AdditiveItem.getEwgGradeText(item.ewgGrade)
            var name = item.name
            var usage = item.purpose

            // 아이템 뷰 내부의 뷰들을 가져오기
            val gradeImageView = itemView.findViewById<ImageView>(R.id.EWG_grade_IV)
            val nameTextView = itemView.findViewById<TextView>(R.id.name_TV)
            val usageTextView = itemView.findViewById<TextView>(R.id.usage_TV)

            var imageResource :Int = R.drawable.round_material_icon_gray

            when(gradeStr) {
                "미등록"-> {nullCount ++
                            imageResource = R.drawable.round_material_icon_gray}
                "안전" -> {LowHazardCount++
                    imageResource = R.drawable.round_material_icon_green}
                "주의" -> {ModerateHazardCount++
                    imageResource = R.drawable.round_material_icon_yellow}
                "위험" -> {HighHazard++
                    imageResource = R.drawable.round_material_icon_red}
            }

            gradeImageView.setImageResource(imageResource)
            nameTextView.text = item.name
            usageTextView.text = item.purpose

            // 아이템 뷰를 레이아웃에 추가
            materialListDataLL.addView(itemView)

        }

        fun setCount(count : Int, countTV: TextView, textTV: TextView){
            if(count != 0){

                countTV.setText(count.toString())
                countTV.visibility= View.VISIBLE
                textTV.visibility= View.VISIBLE

            }else{

                countTV.visibility= View.GONE
                textTV.visibility= View.GONE

            }
        }

        setCount(nullCount, binding.unmarkedCountTV, binding.unmarkedTV)
        setCount(LowHazardCount, binding.safeCountTV, binding.safeTV)
        setCount(ModerateHazardCount, binding.noteCountTV, binding.noteTV)
        setCount(HighHazard, binding.dangerCountTV, binding.dangerTV)

        binding.materialListTV.setText(foodData.rowMaterials)
        binding.originListTV.setText(foodData.originMaterials)

    }
    // 유틸 클래스 할당
    private fun initUtils() {

    }

    private fun setAdditive(item : AdditiveItem) {



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