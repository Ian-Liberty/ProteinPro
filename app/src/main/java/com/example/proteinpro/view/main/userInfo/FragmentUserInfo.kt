package com.example.proteinpro.view.main.userInfo
import android.app.Activity
import android.content.Intent

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
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
import com.example.proteinpro.view.ActivityPrivacyPolicy
import com.example.proteinpro.view.ActivityTermsOfUse
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

    // token
    private lateinit var token:String

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
        initListener()
        initData()
    }

    // 리스너 선언
    private fun initListener() {

        // 유저 타이틀

        //
        binding.editIV.setOnClickListener{
            Log.i ("editIV", "정보수정 버튼")

            val mIntent = Intent(mainActivity, ActivityChangeUserInformation::class.java)
            startActivity(mIntent)

        }
        binding.favoriteIV.setOnClickListener{
            Log.i ("favoriteIV", "나의 관심")
            val mIntent = Intent(mainActivity, ActivityFavoriteFoodList::class.java)
            startActivity(mIntent)

        }
        binding.reviewIV.setOnClickListener {
            Log.i ("reviewIV", "리뷰 관리")
            val mIntent = Intent(mainActivity, ActivityMyReview::class.java)
            startActivity(mIntent)
        }

        // 설정관리

        binding.alarmSettingLL.setOnClickListener {

            Log.i ("alarmSettingLL", "알림 설정")
            val mIntent = Intent(mainActivity, ActivityNotificationSetting::class.java)
            startActivity(mIntent)

        }

        // 고객 지원

        binding.termsOfServiceLL.setOnClickListener {
            Log.i ("termsOfServiceLL", "서비스 이용약관")
            val mIntent = Intent(mainActivity, ActivityTermsOfUse::class.java)
            startActivity(mIntent)
        }
        binding.securitySettingLL.setOnClickListener {
            Log.i ("securitySettingLL", "개인정보 처리 방침")
            val mIntent = Intent(mainActivity, ActivityPrivacyPolicy::class.java)
            startActivity(mIntent)
        }
        binding.settingLL.setOnClickListener {
            Log.i ("settingLL", "내 계정 관리하기")

            val mIntent = Intent(mainActivity, ActivityAccountManagement::class.java)
            startActivity(mIntent)


        }

    }

    // 뷰 객체 할당
    private fun initViews() {

        val user = preferenceHelper.getUser()


        var content : String = "${user?.nickname} 님 \n오늘도 프프하세요."
        Log.i ("userInfo", ""+content)
        var word : String = user?.nickname.toString()

        var start: Int = content.indexOf(word)
        var end = start+ word.length

        var spannableString : SpannableString = SpannableString(content)

        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#007AFF")),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.userInfoTitleTV.setText(spannableString)

    }

    // 현재 프래그먼트에서 필요한 데이터 받아오기
    private fun initData() {

        token = preferenceHelper.get_jwt_Token().toString()

        val token = preferenceHelper.get_jwt_Token()

        if (!token.isNullOrEmpty()) {
            retrofitHelper.getUserInfo(token) { user, success ->
                if (success) {
                    if (user != null) {
                        // 사용자 정보를 가져온 경우
                        // 이곳에서 가져온 user 객체를 활용하여 필요한 작업 수행
                        // 예: 유저 정보를 화면에 표시하는 등의 동작

                        preferenceHelper.saveUser(user)
                        initViews()

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

//        textView.setText(userInfo)

    }

    // 유틸 클래스 할당
    private fun initUtils() {

        preferenceHelper = PreferenceHelper(mainActivity)
        retrofitHelper = RetrofitHelper(mainActivity)

    }

}