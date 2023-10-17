package com.example.proteinpro.view.main.userInfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.example.proteinpro.R
import com.example.proteinpro.databinding.ActivityChangeUserInformationBinding
import com.example.proteinpro.util.Class.User
import com.example.proteinpro.util.PreferenceHelper
import com.example.proteinpro.util.Retrofit.RetrofitHelper
import com.example.proteinpro.view.main.MainActivity
import java.util.Calendar


class ActivityChangeUserInformation : AppCompatActivity() {

    private lateinit var preferenceHelper: PreferenceHelper
    private lateinit var retrofitHelper: RetrofitHelper

    private lateinit var user_birth: String
    private lateinit var user : User

    // 전역 변수로 바인딩 객체 선언
    private var mBinding:
            ActivityChangeUserInformationBinding? =null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!
    //!!는 Kotlin에서 Nullable 타입을 강제로 Non-nullable 타입으로 변환하는 것을 의미

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_user_information)

        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용해서
        // 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivityChangeUserInformationBinding.inflate(layoutInflater)
        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의
        // 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시 합니다.
        setContentView(binding.root)// < 기존의 setContentView 는 주석 처리해 주세요!

        binding.vDatePicker.init(
            binding.vDatePicker.year,
            binding.vDatePicker.month,
            binding.vDatePicker.dayOfMonth,
            DatePicker.OnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
                // 값이 변경될 때마다 체크 함수 호출
                checkIfUnder14YearsOld(year, monthOfYear, dayOfMonth)
            }
        )

        initUtils()
        initListener()
        initData()

    }

    private fun initListener(){
        // 리스너 초기화

        binding.nicknameET.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

                if(binding.nicknameET.text.toString() == user.nickname){
                    binding.nicknameWarningTv.isVisible = false
                    binding.updateBTN.isEnabled = true
                    return
                }

                if(isNicknameValid( binding.nicknameET.text.toString())){

                    //닉네임 형식 유효

                    retrofitHelper.checkNicknameDuplication(user.nickname){isSuccess ->
                        if(isSuccess){
                            // 중복되지 않음

                            binding.nicknameWarningTv.isVisible = false
                            binding.updateBTN.isEnabled = true

                        }else{
                            //중복된 닉네임

                            binding.nicknameWarningTv.setText("이미 존재하는 닉네임 입니다")
                            binding.nicknameWarningTv.isVisible = true

                            binding.updateBTN.isEnabled = false

                        }
                    }


                    binding.nicknameWarningTv.isVisible = false
                    binding.updateBTN.isEnabled = true

                }else{

                    //닉네임 형식이 유효하지 않으면
                    binding.nicknameWarningTv.setText("닉네임은 2~8자 사이의 영문이나 한글로 만들어해주세요! ")
                    binding.nicknameWarningTv.isVisible = true

                    binding.updateBTN.isEnabled = false
                }


            }

        })

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        binding.updateBTN.setOnClickListener{
            updateUserData()
        }

        binding.updateBTN.setOnClickListener {

            val genderCheckedId = binding.genderRG.checkedRadioButtonId
            val heightText = binding.heightET.text.toString().trim()
            val weightText = binding.weightET.text.toString().trim()
            val activityLevelCheckedId = binding.activityLevelRG.checkedRadioButtonId
            user.birthDate = user_birth

            // 키 몸무게
            user.height =heightText.toInt()
            user.weight =weightText.toInt()
            // 유저성별
            if(genderCheckedId == binding.male.id){
                user.gender = 1//남
            }else{
                user.gender = 0//여
            }

            // 유저활동량
            when (activityLevelCheckedId) {
                binding.neverRB.id -> {
                    user.activityLevel = User.ActivityLevel.SEDENTARY
                }
                binding.oneThreeRB.id -> {
                    user.activityLevel = User.ActivityLevel.LIGHTLY_ACTIVE
                }
                binding.fourFiveRB.id -> {
                    user.activityLevel = User.ActivityLevel.MODERATELY_ACTIVE
                }
                binding.sixSevenRB.id -> {
                    user.activityLevel = User.ActivityLevel.VERY_ACTIVE
                }
                binding.sevenHardRB.id -> {
                    user.activityLevel = User.ActivityLevel.EXTRA_ACTIVE
                }
                else -> {
                    user.activityLevel = User.ActivityLevel.SEDENTARY
                }
            }

            if (genderCheckedId == -1 || heightText.isEmpty() || weightText.isEmpty() || activityLevelCheckedId == -1) {
                // 빈값이 있을경우
                showWarningAlertDialog()
            } else {
                val token = preferenceHelper.get_jwt_Token().toString()

                retrofitHelper.userDataUpdate(user, token){isSuccess->
                    if(isSuccess){
                        finish()
                    }else{
                        Toast.makeText(getApplicationContext(), "데이터 저장에 실패했습니다. 관리자에게 문의하세요!",
                            Toast.LENGTH_SHORT).show()
                    }

                }

            }
        }
    }

    private fun updateUserData() {


    }

    private fun initUtils(){
        // 유틸 클래스 초기화
        preferenceHelper = PreferenceHelper(this)
        retrofitHelper = RetrofitHelper(this)

    }
    private fun initData() {
        user = preferenceHelper.getUser()!!


        if(user != null){
            // 닉네임
            binding.nicknameET.setText(user.nickname)

            // 성별
            if(user?.gender == 0){
                // 여성일경우
                binding.genderRG.check(R.id.female)
            }else{
                // 남자일 경우
                binding.genderRG.check(R.id.male)
            }

            // 몸무게
            if(user.weight == -1){

            }else{
                binding.weightET.setText(user.weight.toString())
            }

            // 키
            if(user.height== -1){

            }else{
                binding.heightET.setText(user.height.toString())
            }

            val birth = user.birthDate
            user_birth = birth
            val dateParts = birth.split("-")// 년 월 일 분리후 리스트로 만듦

            // 생일
            if(dateParts.size == 3) {// 정상분리될 경우 3이어야 함
                val year = dateParts[0].toInt()
                val month = dateParts[1].toInt() - 1 // 월은 0부터 시작하므로 1을 빼줌
                val day = dateParts[2].toInt()

                binding.vDatePicker.updateDate(year, month, day)// 날짜 업데이트

            }else{
                Log.d("계산기 초기설정","생일정보 오류")
            }

            // 활동레벨

            when(user.activityLevel){
                User.ActivityLevel.SEDENTARY -> binding.activityLevelRG.check(R.id.never_RB)
                User.ActivityLevel.LIGHTLY_ACTIVE -> binding.activityLevelRG.check(R.id.one_three_RB)
                User.ActivityLevel.MODERATELY_ACTIVE -> binding.activityLevelRG.check(R.id.four_five_RB)
                User.ActivityLevel.VERY_ACTIVE -> binding.activityLevelRG.check(R.id.six_seven_RB)
                User.ActivityLevel.EXTRA_ACTIVE -> binding.activityLevelRG.check(R.id.seven_hard_RB)
            }

        }

    }

    private fun checkIfUnder14YearsOld(year: Int, month: Int, dayOfMonth: Int) {
        // 현재 날짜를 구합니다.
        val currentDate = Calendar.getInstance()
        val currentYear = currentDate.get(Calendar.YEAR)
        val currentMonth = currentDate.get(Calendar.MONTH)
        val currentDayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH)

        // 입력된 생년월일과 현재 날짜를 기반으로 만 나이를 계산합니다.
        val age = currentYear - year - if (currentMonth < month || (currentMonth == month && currentDayOfMonth < dayOfMonth)) 1 else 0

        // 만 14세 미만 여부를 체크합니다.
        val isUnder14YearsOld = age < 14

        // 결과 출력
        if (isUnder14YearsOld) {
            // 만 14세 미만인 경우 처리
            // 여기에 원하는 동작을 추가합니다.
            // 예를 들면 경고 메시지 띄우기 등의 처리를 할 수 있습니다.
            // 예시: Toast.makeText(this, "만 14세 미만입니다.", Toast.LENGTH_SHORT).show()
            binding.warningTv.setText("만 14세 미만은 서비스 이용이 불가능합니다.")
            binding.warningTv.isVisible = true


        } else {
            // 만 14세 이상인 경우 처리
            // 여기에 원하는 동작을 추가합니다.

            // 월은 0 부터 시작함 ㅠ
            val selectMonth = month +1

            user_birth = "$year-$selectMonth-$dayOfMonth"

            binding.warningTv.isVisible = false// 경고 제거
        }
    }
    private fun showWarningAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)

        // 다이얼로그 제목과 메시지 설정
        alertDialogBuilder.setTitle("알림")
        alertDialogBuilder.setMessage("아직 입력하지 않은 정보가 남았어요! 그래도 입력을 건너뛰시겠어요?")

        // 확인 버튼에 대한 처리
        alertDialogBuilder.setPositiveButton("확인") { dialog, which ->
            // 확인 버튼이 눌렸을 때의 동작을 여기에 작성합니다.
            // 예를 들면 확인 버튼을 눌렀을 때 할 작업을 정의할 수 있습니다.

            val mIntent = Intent(this, MainActivity::class.java)
            startActivity(mIntent)

        }

        // 취소 버튼에 대한 처리 (선택사항)
        alertDialogBuilder.setNegativeButton("취소") { dialog, which ->
            // 취소 버튼이 눌렸을 때의 동작을 여기에 작성합니다.
            // 예를 들면 취소 버튼을 눌렀을 때 할 작업을 정의할 수 있습니다.
        }

        // 다이얼로그 생성 및 표시
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

    }

    fun isNicknameValid(nickname: String): Boolean {
        val regex = Regex("^[a-zA-Z가-힣]{2,8}$")
        return regex.matches(nickname) && !hasInvalidKoreanCharacter(nickname)
    }

    fun hasInvalidKoreanCharacter(nickname: String): Boolean {
        val invalidKoreanCharacters = setOf('ㄱ', 'ㄴ', 'ㄷ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅅ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ',
            'ㄲ', 'ㄸ', 'ㅃ', 'ㅆ', 'ㅉ', 'ㅏ', 'ㅓ', 'ㅗ', 'ㅜ', 'ㅡ', 'ㅣ')
        for (char in nickname) {
            if (char in invalidKoreanCharacters) {
                return true
            }
        }
        return false
    }


}