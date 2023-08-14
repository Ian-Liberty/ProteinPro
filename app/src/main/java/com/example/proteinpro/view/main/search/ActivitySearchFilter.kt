package com.example.proteinpro.view.main.search

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import com.example.proteinpro.R
import com.example.proteinpro.databinding.ActivitySearchFilterBinding
import com.example.proteinpro.databinding.ChipBinding
import com.example.proteinpro.util.Class.FilterSettings
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class ActivitySearchFilter : AppCompatActivity() {

    private lateinit var effect_cg : ChipGroup
    private lateinit var type_cg : ChipGroup
    private lateinit var allergy_cg : ChipGroup
    private lateinit var taste_cg : ChipGroup
    private lateinit var etc_cg : ChipGroup
    private val selectedChips = ArrayList<String>()

    private lateinit var exit_btn : ImageButton

    // 전역 변수로 바인딩 객체 선언
    private var mBinding:
            ActivitySearchFilterBinding? =null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!
    //!!는 Kotlin에서 Nullable 타입을 강제로 Non-nullable 타입으로 변환하는 것을 의미

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_filter)

        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용해서
        // 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivitySearchFilterBinding.inflate(layoutInflater)
        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의
        // 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시 합니다.
        setContentView(binding.root)// < 기존의 setContentView 는 주석 처리해 주세요!

        initViews()
        initListener()
        initUtils()

    }

    private fun initViews(){
        // 뷰 초기화
        exit_btn = binding.exitButton

        effect_cg = binding.effectsFilterCG
        type_cg = binding.typeFilterCG
        allergy_cg = binding.allergyFilterCG
        taste_cg = binding.tasteFilterCG
        etc_cg = binding.etcFilterCG

        chipGeneration()
    }
    private fun initListener(){
        // 리스너 초기화

        exit_btn.setOnClickListener{
            collectSelectedChips()
            val resultIntent = Intent()
            resultIntent.putStringArrayListExtra("selectedChips", selectedChips)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

    }
    private fun initUtils(){
        // 유틸 클래스 초기화
    }

    private fun chipGeneration(){

        val filter_list = FilterSettings()
        val effect_list = filter_list.effectFilterItems
        val type_list = filter_list.kindFilterItems
        val allergy_list = filter_list.allergyFilterItems
        val taste_list = filter_list.tasteFilterItems
        val etc_list = filter_list.etcFilterItems

        for(filter in effect_list) {

            val chip = createFilterChip(filter)

            effect_cg.addView(chip)
        }
        for(filter in type_list) {

            val chip = createFilterChip(filter)


            type_cg.addView(chip)
        }
        for(filter in allergy_list) {

            val chip = createFilterChip(filter)


            allergy_cg.addView(chip)
        }
        for(filter in taste_list) {

            val chip = createFilterChip(filter)


            taste_cg.addView(chip)
        }
        for(filter in etc_list) {

            val chip = createFilterChip(filter)

            etc_cg.addView(chip)
        }

    }

    private fun createFilterChip(filter: FilterSettings.FilterItem): Chip {

        val chip = ChipBinding.inflate(layoutInflater).root
        chip.text = filter.Key
        chip.textSize = 16F


        chip.setOnClickListener {

            Log.i ("칩 클릭 이벤트", "이 Chip은 ${filter.Key} Chip 입니다. 요청할때 값은 ${filter.name} 입니다. child 값은 ${filter.child} 입니다.")

        }

        return chip
    }

    private fun collectSelectedChips() {
        selectedChips.clear()

        collectChipsFromGroup(effect_cg)
        collectChipsFromGroup(type_cg)
        collectChipsFromGroup(allergy_cg)
        collectChipsFromGroup(taste_cg)
        collectChipsFromGroup(etc_cg)
    }

    private fun collectChipsFromGroup(chipGroup: ChipGroup) {
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as? Chip
            //chipGroup라는 ChipGroup에 속한 자식 뷰들 중에서 i번째 위치에 있는 자식 뷰를 가져오는 코드입니다. getChildAt 메서드는 해당 위치에 있는 자식 뷰를 반환합니다.
            if (chip != null && chip.isChecked) {
                selectedChips.add(chip.text.toString())
            }
        }
    }
}