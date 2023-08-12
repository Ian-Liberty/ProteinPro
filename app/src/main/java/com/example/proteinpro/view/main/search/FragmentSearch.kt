package com.example.proteinpro.view.main.search
import android.util.Log

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import com.example.proteinpro.view.main.MainActivity
import com.example.proteinpro.databinding.FragmentSearchBinding
import com.example.proteinpro.util.Class.CategorySettings
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

/**
검색 들어오면서 서치뷰 커서 넣어주기
 */
class FragmentSearch : Fragment() {
    //컨텍스트 변수
    private lateinit var mainActivity: MainActivity

    // 변수 입력
    private lateinit var binding: FragmentSearchBinding

    // 뷰 객체
    private lateinit var protein_search_sv: androidx.appcompat.widget.SearchView
    private lateinit var back_btn: ImageButton
    private lateinit var cancle_tv: TextView

    private lateinit var category_cg : ChipGroup

    private lateinit var supplement_tv: TextView
    private lateinit var Protein_beverage_tv: TextView
    private lateinit var proteinBar_tv: TextView
    private lateinit var ALL_tv: TextView
    private lateinit var chickenBreast_tv: TextView
    private lateinit var proteinSausage_tv: TextView
    private lateinit var chickenBrestStake_tv: TextView
    private lateinit var ETC_tv: TextView

// 유틸 클래스

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
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUtils()
        initData()
        initViews()
        initListener()

    }

    // 리스너 선언
    private fun initListener() {

        protein_search_sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                Log.i ("검색 시작", "검색어 : ${query}")

                var fragment_search_result = FragmentSearch_result()
                mainActivity.switchFragment(fragment_search_result)

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {


                return true
            }

        })
    }

    // 뷰 객체 할당
    private fun initViews() {
        category_cg = binding.CategoryCG
        chipGeneration()

        protein_search_sv =binding.proteinSV

    }

    // 현재 프래그먼트에서 필요한 데이터 받아오기
    private fun initData() {

    }

    // 유틸 클래스 할당
    private fun initUtils() {

    }

    private fun chipGeneration(){

        val categorylist = CategorySettings()
        val categorys = categorylist.getCategoryItemsForSearch()// 카테고리 아이템 담겨있는 arraylist

        for(category in categorys) {

            val chip = Chip(mainActivity)

            chip.text = category.name
            chip.textSize = 16F

            chip.setOnClickListener {
                Log.i ("칩 클릭 이벤트", "이 Chip은 ${category} Chip 입니다.")

                val bundle = Bundle()
                bundle.putString("categoryKey", category.Key)

                var fragment_search_result = FragmentSearch_result()
                fragment_search_result.arguments = bundle

                mainActivity.switchFragment(fragment_search_result)

            }

            category_cg.addView(chip)
        }

    }
}