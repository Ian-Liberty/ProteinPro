package com.example.proteinpro.view.main.userInfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.proteinpro.R
import com.example.proteinpro.databinding.ActivityChangePasswordBinding
import com.example.proteinpro.util.PreferenceHelper
import com.example.proteinpro.util.Retrofit.FoodRetrofitHelper
import com.example.proteinpro.util.Retrofit.RetrofitHelper

class ActivityChangePassword : AppCompatActivity() {

    // 유틸 함수 선언
    private lateinit var preferenceHelper: PreferenceHelper
    private lateinit var retrofitHelper: RetrofitHelper

    // 전역 변수로 바인딩 객체 선언
    private var mBinding:
            ActivityChangePasswordBinding? =null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!
    //!!는 Kotlin에서 Nullable 타입을 강제로 Non-nullable 타입으로 변환하는 것을 의미

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_change_password)

        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용해서
        // 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivityChangePasswordBinding.inflate(layoutInflater)
        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의
        // 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시 합니다.
        setContentView(binding.root)// < 기존의 setContentView 는 주석 처리해 주세요!

        initUtils()
        initListener()

    }

    private fun initListener(){
        // 리스너 초기화

        binding.changeTV.setOnClickListener {

            val password = binding.passwordET.text.toString()
            val newPassword = binding.newPasswordET.toString()

            if(password.isNotEmpty()&&newPassword.isNotEmpty()){
                // 빈칸이 있는 경우
                Toast.makeText(getApplicationContext(), "비어있는 값이 있습니다. 빈칸을 모두 작성해 주세요",Toast.LENGTH_SHORT).show()

            }else{
                val token = preferenceHelper.get_jwt_Token().toString()

                retrofitHelper.changePassword(password, newPassword, token){isSuccess ->
                    if(isSuccess){
                        Toast.makeText(getApplicationContext(), "비밀번호가 변경 되었습니다.",Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        Toast.makeText(getApplicationContext(), "비밀번호 변경에 실패 하였습니다. 비밀번호를 확인해 주세요!",Toast.LENGTH_SHORT).show()
                    }

                }
            }

        }

        binding.newPasswordET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 값이 변경되기전에 호출

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 값이 변경 될때 호출

            }

            override fun afterTextChanged(s: Editable?) {
                // 값이 변경된 후에 호출
                // 비밀번호 인풋 값 검사
                if(isPasswordValid(binding.newPasswordET.getText().toString())){
                    //비밀번호 형식 유효
                    binding.warningTv.isVisible = false

                }else{

                    //비밀번호 형식 유효하지 않으면
                    binding.warningTv.setText("비밀번호는 알파벳 과 숫자 그리고 하나이상의 특수문자가 포함된 \n8이상 15자 이하의 조합으로 구성해 주세요!")
                    binding.warningTv.isVisible = true

                }

            }

        })

    }
    private fun initUtils(){
        // 유틸 클래스 초기화

        preferenceHelper = PreferenceHelper(this)
        retrofitHelper = RetrofitHelper(this)

    }

    fun isPasswordValid(password: String): Boolean {
        val passwordRegex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#\$%^*+=-]).{8,15}\$".toRegex()
        return password.matches(passwordRegex)
    }
}