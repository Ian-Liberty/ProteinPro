package com.example.proteinpro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.proteinpro.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView



class MainActivity : AppCompatActivity() {

    private var fragmentHome = FragmentHome()
    private var fragmentSearch = FragmentSearch()
    private var fragmentCalculator = Fragment_Calculator()
    private var fragmentanotherContents = FragmentAnother_Contents()
    private var fragmentUserInfo = FragmentUserInfo()

    private lateinit var bottomNavigationView: BottomNavigationView
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

        setBottomNavi()

    }
    private fun setBottomNavi(){
        bottomNavigationView = binding.mainBNV
        bottomNavigationView.run { setOnItemSelectedListener {
            when(it.itemId){
              R.id.home_menu -> {
                  fragmentManager.beginTransaction().replace(R.id.fl_container, fragmentHome).commit()
              }
                R.id.search_menu -> {
                    fragmentManager.beginTransaction().replace(R.id.fl_container, fragmentSearch).commit()
                }
                R.id.calculate_menu -> {
                    fragmentManager.beginTransaction().replace(R.id.fl_container, fragmentCalculator).commit()
                }
                R.id.etc_menu -> {
                    fragmentManager.beginTransaction().replace(R.id.fl_container, fragmentanotherContents).commit()
                }
                R.id.user_menu -> {
                    fragmentManager.beginTransaction().replace(R.id.fl_container, fragmentUserInfo).commit()
                }
              else -> {}
            }
            true
        }
            selectedItemId = R.id.home_menu
        }


    }

    private fun switchFragment(fragment: Fragment) {
        fragmentManager.beginTransaction().hide(activeFragment).show(fragment).commit()
        activeFragment = fragment
    }

    private fun initViews(){
        // 뷰 초기화



    }
    private fun initListener(){
        // 리스너 초기화
    }
    private fun initUtils(){
        // 유틸 클래스 초기화
    }
}