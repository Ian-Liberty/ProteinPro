package com.example.proteinpro.view.main.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.proteinpro.databinding.ChipBinding
import com.example.proteinpro.databinding.FragmentSearchFilterBinding
import com.example.proteinpro.util.Class.CategorySettings
import com.example.proteinpro.util.Class.FilterSettings
import com.example.proteinpro.view.main.MainActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class FragmentSearch_filter : Fragment() {
    // TODO: Rename and change types of parameters
    //컨텍스트 변수
    private lateinit var mainActivity: MainActivity

    private lateinit var binding: FragmentSearchFilterBinding

    private lateinit var effect_cg : ChipGroup
    private lateinit var type_cg : ChipGroup
    private lateinit var allergy_cg : ChipGroup
    private lateinit var taste_cg : ChipGroup
    private lateinit var etc_cg : ChipGroup

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity// 컨텍스트 받아오기

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchFilterBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUtils()
        initData()
        initViews()
        initListener()

    }

    private fun initListener() {

    }

    private fun initViews() {

        effect_cg = binding.effectsFilterCG
        type_cg = binding.typeFilterCG
        allergy_cg = binding.allergyFilterCG
        taste_cg = binding.tasteFilterCG
        etc_cg = binding.etcFilterCG

        chipGeneration()

    }

    private fun initData() {

    }

    private fun initUtils() {

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


}