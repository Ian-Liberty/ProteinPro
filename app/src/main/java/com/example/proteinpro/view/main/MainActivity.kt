package com.example.proteinpro.view.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.proteinpro.view.main.anotherContents.FragmentAnother_Contents
import com.example.proteinpro.view.main.home.FragmentHome
import com.example.proteinpro.view.main.userInfo.FragmentUserInfo
import com.example.proteinpro.view.main.calculator.Fragment_Calculator
import com.example.proteinpro.R
import com.example.proteinpro.databinding.ActivityMainBinding
import com.example.proteinpro.view.main.search.FragmentSearch
import com.example.proteinpro.view.main.search.FragmentSearch_filter
import com.example.proteinpro.view.main.search.FragmentSearch_result
import com.google.android.material.bottomnavigation.BottomNavigationView



class MainActivity : AppCompatActivity() {

    private var fragmentHome = FragmentHome()
    // 검색 관련 프래그먼트
    private var fragmentSearch = FragmentSearch()
    private var fragmentsearchResult =FragmentSearch_result()
    private var fragmentsearchFilter = FragmentSearch_filter()

    private var fragmentCalculator = Fragment_Calculator()
    private var fragmentAnotherContents = FragmentAnother_Contents()
    private var fragmentUserInfo = FragmentUserInfo()

    lateinit var bottomNavigationView: BottomNavigationView
    private val fragmentManager = supportFragmentManager
    private var activeFragment: Fragment = fragmentHome

    // 전역 변수로 바인딩 객체 선언
    private var mBinding:
    ActivityMainBinding? =null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!
    //!!는 Kotlin에서 Nullable 타입을 강제로 Non-nullable 타입으로 변환하는 것을 의미
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용해서
        // 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의
        // 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시 합니다.
        setContentView(binding.root)// < 기존의 setContentView 는 주석 처리해 주세요!

//        setBottomNavi()
        bottomNavigationView = binding.mainBNV
        bottomNavigationView.setOnItemSelectedListener {
            val newFragment = when (it.itemId) {
                R.id.home_menu -> fragmentHome
                R.id.search_menu -> fragmentSearch
                R.id.calculate_menu -> fragmentCalculator
                R.id.etc_menu -> fragmentAnotherContents
                R.id.user_menu -> fragmentUserInfo
                else -> null

            }

            if (newFragment != null && newFragment != activeFragment) {
                switchFragment(newFragment)
            }

            true
        }

        if (savedInstanceState == null) {
            switchFragment(fragmentHome)
        } else {
            // 복원 로직이 있다면 여기에서 상태를 복원
        }

    }

//    private fun switchFragment(fragment: Fragment) {
//        fragmentManager.beginTransaction().hide(activeFragment).show(fragment).commit()
//        activeFragment = fragment
//    }

    private fun initViews(){
        // 뷰 초기화

    }
    private fun initListener(){
        // 리스너 초기화
    }
    private fun initUtils(){
        // 유틸 클래스 초기화
    }


    fun switchFragment(fragment: Fragment, selectedItemId: Int? = null) {
        fragmentManager.beginTransaction().apply {
            activeFragment?.let { hide(it) }
            if (!fragment.isAdded) {
                add(R.id.fl_container, fragment)
            }
            show(fragment)
            commit()
        }
        activeFragment = fragment

        selectedItemId?.let{bottomNavigationView.selectedItemId = it}
    }

    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}