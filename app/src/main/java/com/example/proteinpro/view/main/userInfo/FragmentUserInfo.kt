package com.example.proteinpro.view.main.userInfo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.proteinpro.databinding.FragmentUserInfoBinding
import com.example.proteinpro.util.Class.User
import com.example.proteinpro.util.PreferenceHelper
import com.example.proteinpro.util.Retrofit.RetrofitHelper
import com.example.proteinpro.view.ActivityLogin
import com.example.proteinpro.view.main.MainActivity

class FragmentUserInfo : Fragment() {
    // 변수 입력
    private lateinit var binding: FragmentUserInfoBinding

    // 뷰 객체
//컨텍스트 변수
    private lateinit var mainActivity: MainActivity

    // 유틸 함수 선언
    private lateinit var preferenceHelper: PreferenceHelper
    private lateinit var retrofitHelper: RetrofitHelper

    // 테스트 요소들
    private lateinit var  logout_btn :Button
    private lateinit var textView: TextView

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserInfoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUtils()
        initViews()
        initListener()
        initData()
    }

    // 리스너 선언
    private fun initListener() {

        logout_btn.setOnClickListener {

            preferenceHelper.logout()

            val mIntent = Intent(mainActivity, ActivityLogin::class.java)

            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(mIntent)

        }

    }

    // 뷰 객체 할당
    private fun initViews() {

        logout_btn = binding.button
        textView = binding.textView

    }

    // 현재 프래그먼트에서 필요한 데이터 받아오기
    private fun initData() {

        val user = preferenceHelper.getUser()
        var activitylevel = user?.activityLevel

        val nickname = user?.nickname
        val height = user?.height
        val gender = user?.gender
        val activitylevelString = activitylevel?.let { User.getActivitylevelString(it) }
        val weight = user?.weight
        val birth = user?.birthDate

        val userInfo = """
    Nickname: $nickname
    Height: $height
    Gender: $gender
    Activity Level: $activitylevelString
    Weight: $weight
    Birth Date: $birth
""".trimIndent()

        textView.setText(userInfo)

    }

    // 유틸 클래스 할당
    private fun initUtils() {

        preferenceHelper = PreferenceHelper(mainActivity)
        retrofitHelper = RetrofitHelper(mainActivity)

    }

}