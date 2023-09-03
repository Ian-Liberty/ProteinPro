package com.example.proteinpro.view.main.anotherContents

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.proteinpro.R
import com.example.proteinpro.databinding.FragmentAnotherContentsBinding
import com.example.proteinpro.view.main.MainActivity

class FragmentAnother_Contents : Fragment() {

    //컨텍스트 변수
    private lateinit var mainActivity: MainActivity

    // 변수 입력
    private lateinit var binding: FragmentAnotherContentsBinding
    // 뷰 객체

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
        binding = FragmentAnotherContentsBinding.inflate(inflater, container, false)

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

        // 유튜브 영상 컨텐츠
        binding.proteinInformationLL.setOnClickListener {

            var fragmentInformationContents = FragmentInformationContents().apply {

                arguments = Bundle().apply{
                    // 전달할 데이터 있으면 여기에!
                }
            }
            mainActivity.switchFragment(fragmentInformationContents, "fragment_information_contents")

        }

        // MBTI 컨텐츠
        binding.MBTILL.setOnClickListener {

            var fragmentMbtiContents = FragmentMbtiContents().apply {

                arguments = Bundle().apply{
                    // 전달할 데이터 있으면 여기에!
                }
            }
            mainActivity.switchFragment(fragmentMbtiContents, "fragment_mbti_contents")
        }
    }

    // 뷰 객체 할당
    private fun initViews() {

    }

    // 현재 프래그먼트에서 필요한 데이터 받아오기
    private fun initData() {

    }
    // 유틸 클래스 할당
    private fun initUtils() {

    }

}