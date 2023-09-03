package com.example.proteinpro.view.main
import android.util.Log

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.proteinpro.view.main.anotherContents.FragmentAnother_Contents
import com.example.proteinpro.view.main.home.FragmentHome
import com.example.proteinpro.view.main.userInfo.FragmentUserInfo
import com.example.proteinpro.view.main.calculator.Fragment_Calculator
import com.example.proteinpro.R
import com.example.proteinpro.databinding.ActivityMainBinding
import com.example.proteinpro.util.PreferenceHelper
import com.example.proteinpro.util.Retrofit.RetrofitHelper
import com.example.proteinpro.view.main.search.FragmentSearch
import com.example.proteinpro.view.main.search.FragmentSearch_result
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Stack


class MainActivity : AppCompatActivity() {


    private var fragmentHome = FragmentHome()
    // 검색 관련 프래그먼트
    private var fragmentSearch = FragmentSearch()

    private var fragmentCalculator = Fragment_Calculator()
    private var fragmentAnotherContents = FragmentAnother_Contents()
    private var fragmentUserInfo = FragmentUserInfo()

    lateinit var bottomNavigationView: BottomNavigationView
    private val fm = supportFragmentManager


    // util함수
    private lateinit var preferenceHelper: PreferenceHelper
    private lateinit var retrofitHelper: RetrofitHelper


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
        //유저 정보 불러와서 쉐어드에 유저 정보로 등록

//        setBottomNavi()

        bottomNavigationView = binding.mainBNV

        // 초기 플래그먼트 설정
        supportFragmentManager.beginTransaction().replace(R.id.fl_container, fragmentHome, "fragmentHome").commitAllowingStateLoss()

        bottomNavigationView.setOnItemSelectedListener {

            Log.i ("프래그먼트", ""+it.itemId)

            when (it.itemId) {
                    R.id.home_menu -> {
                        Log.i ("프래그먼트", "home_menu")
                        switchFragment(fragmentHome,"fragmentHome" )

                }
                    R.id.search_menu -> {
                        Log.i ("프래그먼트", "search_menu")
                        switchFragment(fragmentSearch,"fragmentSearch" )

                }
                    R.id.calculate_menu -> {
                        Log.i ("프래그먼트", "calculate_menu")
                        switchFragment(fragmentCalculator,"fragmentCalculator" )

                }
                    R.id.etc_menu -> {
                        Log.i ("프래그먼트", "etc_menu")
                        switchFragment(fragmentAnotherContents,"fragmentAnotherContents" )

                }
                    R.id.user_menu -> {
                        Log.i ("프래그먼트", "user_menu")
                        switchFragment(fragmentUserInfo,"fragmentUserInfo" )
                }

            }


            true
        }


        initUtils()

        if(preferenceHelper.getUser() == null){
            getUserData()
        }


    }

    private fun getUserData() {

        val token = preferenceHelper.get_jwt_Token()

        if (!token.isNullOrEmpty()) {
            retrofitHelper.getUserInfo(token) { user, success ->
                if (success) {
                    if (user != null) {
                        // 사용자 정보를 가져온 경우
                        // 이곳에서 가져온 user 객체를 활용하여 필요한 작업 수행
                        // 예: 유저 정보를 화면에 표시하는 등의 동작

                        preferenceHelper.saveUser(user)

                    } else {
                        // 사용자 정보를 가져오지 못한 경우
                        // 예: 로그인 정보가 만료되었거나 오류가 발생한 경우
                        Log.i ("정보태그", "사용자 정보 가져오기 실패/ 토큰만료?/ 혹은 오류")
                    }
                } else {
                    // API 호출이 실패한 경우
                    // 예: 네트워크 연결이 안 되었거나 서버 오류가 발생한 경우
                    Log.e("오류태그", "네트워크 연결 안됨 or 서버오류")
                }
            }
        } else {
            // 토큰이 없는 경우
            // 예: 로그인되어 있지 않은 상태
            Log.e("오류태그", "로그인되어 있지 않은 상태/ 토큰이 없음")
        }

    }

    fun switchFragment(fragment: Fragment , tag: String) {
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.fl_container, fragment, tag)
        transaction.addToBackStack(tag)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
    }

    private fun initViews(){
        // 뷰 초기화

    }
    private fun initListener(){
        // 리스너 초기화
    }
    private fun initUtils(){
        // 유틸 클래스 초기화
        preferenceHelper = PreferenceHelper(this)
        retrofitHelper = RetrofitHelper(this)
    }


//    fun switchFragment(fragment: Fragment, selectedItemId: Int? = null) {
////        fragmentManager.beginTransaction().apply {
////            activeFragment?.let { hide(it) }
////            if (!fragment.isAdded) {
////                add(R.id.fl_container, fragment)
////
////            }
////
////            show(fragment)
////            commit()
////        }
//        activeFragment = fragment
//        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.fl_container, fragment).commit()
//
//        selectedItemId?.let{bottomNavigationView.selectedItemId = it}
//    }

    private fun updateBottomMenu(navigation: BottomNavigationView) {
        Log.i ("updateBottomMenu", "updateBottomMenu")

        val tag1: Fragment? = supportFragmentManager.findFragmentByTag("fragmentHome")
        val tag2: Fragment? = supportFragmentManager.findFragmentByTag("fragmentSearch")
        val tag3: Fragment? = supportFragmentManager.findFragmentByTag("fragmentCalculator")
        val tag4: Fragment? = supportFragmentManager.findFragmentByTag("fragmentAnotherContents")
        val tag5: Fragment? = supportFragmentManager.findFragmentByTag("fragmentUserInfo")
        val tag6: Fragment? = supportFragmentManager.findFragmentByTag("fragment_search_result")
        val tag7: Fragment? = supportFragmentManager.findFragmentByTag("fragment_information_contents")
        val tag8: Fragment? = supportFragmentManager.findFragmentByTag("fragment_mbti_contents")


        if(tag1 != null && tag1.isVisible) {navigation.menu.findItem(R.id.home_menu).isChecked = true }
        if(tag2 != null && tag2.isVisible) {navigation.menu.findItem(R.id.search_menu).isChecked = true }
        if(tag3 != null && tag3.isVisible) {navigation.menu.findItem(R.id.calculate_menu).isChecked = true }
        if(tag4 != null && tag4.isVisible) {navigation.menu.findItem(R.id.etc_menu).isChecked = true }
        if(tag5 != null && tag5.isVisible) {navigation.menu.findItem(R.id.user_menu).isChecked = true }
        if(tag6 != null && tag6.isVisible) {navigation.menu.findItem(R.id.search_menu).isChecked = true }
        if(tag7 != null && tag7.isVisible) {navigation.menu.findItem(R.id.etc_menu).isChecked = true }
        if(tag8 != null && tag8.isVisible) {navigation.menu.findItem(R.id.etc_menu).isChecked = true }
    }
    override fun onBackPressed() {
        Log.i ("백버튼", "onBackPressed")
        super.onBackPressed()

        updateBottomMenu(bottomNavigationView)
    }
}