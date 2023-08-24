package com.example.proteinpro.view.main.search

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.proteinpro.R
import com.example.proteinpro.databinding.FragmentMaterialBinding
import com.example.proteinpro.util.Class.food.AdditiveItem
import com.example.proteinpro.util.Class.food.FoodInformationItem


class FragmentMaterial(private val foodData: FoodInformationItem) : Fragment() {
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

    }

    // 현재 프래그먼트에서 필요한 데이터 받아오기
    private fun initData() {

        val additiveList = foodData.additiveList

        var nullCount =0
        var LowHazardCount =0
        var ModerateHazardCount = 0
        var HighHazard = 0

        val materialListDataLL = binding.materialListDataLL


        for(item in additiveList){
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
}