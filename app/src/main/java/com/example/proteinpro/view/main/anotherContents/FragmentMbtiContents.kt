package com.example.proteinpro.view.main.anotherContents

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proteinpro.R
import com.example.proteinpro.databinding.FragmentMbtiContentsBinding
import com.example.proteinpro.view.main.MainActivity


class FragmentMbtiContents : Fragment() {
   // 변수 입력
   private lateinit var binding: FragmentMbtiContentsBinding
   // 뷰 객체

   // 유틸 클래스

    //컨텍스트 변수
    private lateinit var mainActivity: MainActivity

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
       binding = FragmentMbtiContentsBinding.inflate(inflater, container, false)

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