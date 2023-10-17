package com.example.proteinpro.view

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.GestureDetector
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.proteinpro.R
import com.example.proteinpro.databinding.ActivityLoginBinding
import com.example.proteinpro.util.Class.User
import com.example.proteinpro.util.PreferenceHelper
import com.example.proteinpro.util.Retrofit.ApiClient
import com.example.proteinpro.util.Retrofit.RetrofitHelper
import com.example.proteinpro.util.Retrofit.UserDataInterface
import com.example.proteinpro.view.main.MainActivity
import com.example.proteinpro.view.user.ActivityFindPassword_InputEmail
import com.example.proteinpro.view.user.signup.ActivityBirthInput
import com.google.gson.JsonElement
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// 시작 페이지
// 로그인 및 회원가입 비밀번호 찾기 등 접근 가능
//
class ActivityLogin : AppCompatActivity() {

    // 인텐트 변수
    private lateinit var login_btn: Button
    private lateinit var kakaoLogin_btn: Button

    private lateinit var signUp_tv: TextView
    private lateinit var findPassword_tv: TextView
    private lateinit var email_et : EditText
    private lateinit var password_et: EditText

    var detector: GestureDetector? = null //무슨 제스쳐를 했는지 감지

    //유틸 클래스 선언
    private lateinit var retrofitHelper: RetrofitHelper
    private lateinit var preferenceHelper: PreferenceHelper

    // 전역 변수로 바인딩 객체 선언
    private var mBinding: ActivityLoginBinding? =null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!
    //!!는 Kotlin에서 Nullable 타입을 강제로 Non-nullable 타입으로 변환하는 것을 의미

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i ("ActivityLogin", "onCreate")
        // 기존 setContentView 를 제거해주시고..
//        setContentView(R.layout.activity_login)

        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용해서
        // 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivityLoginBinding.inflate(layoutInflater)

        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의
        // 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시 합니다.
        setContentView(binding.root)

        // 프리퍼런스 헬퍼 선언
        preferenceHelper = PreferenceHelper(this)
        retrofitHelper = RetrofitHelper(this)


        requestPermission()
        //카카오SDK 초기화
        checkisLogin(preferenceHelper)
        KakaoSdk.init(this, "44316c42987d9a62edd1c49462605432")

        initUtils()
        initViews()
        initListener()

        //

    }

    private fun initUtils() {

    }

    private fun checkisLogin(preferenceHelper: PreferenceHelper) {
        if(preferenceHelper.get_jwt_Token() != "값이없음"){
          // 자동로그인
            var token = preferenceHelper.get_jwt_Token()
            retrofitHelper.checkToken(token.toString()){ isSuccess ->

                if(isSuccess){
                    val mIntent = Intent(this, MainActivity::class.java)
                    startActivity(mIntent)
                    finish()
                }else{
                    Log.i ("checkisLogin", "유효하지 않은 토큰 입니다.")
                }
            }
        }
    }

    private fun initViews(){
        // 뷰 초기화

        login_btn = binding.loginBTN
        login_btn.isEnabled = false

        kakaoLogin_btn = binding.kakaoLoginBTN
        signUp_tv = binding.signUpTV
        findPassword_tv = binding.findPasswordTV

        email_et = binding.emailET
        password_et = binding.passwordET

    }
    private fun initListener(){

        binding.layout.setOnTouchListener(OnTouchListener { v, event ->
            hideKeyboard()
            false
        })

//        // 리스너 초기화
//        detector = GestureDetector(this, object : GestureDetector.OnGestureListener {
//            //화면이 눌렸을 때
//            override fun onDown(e: MotionEvent): Boolean {
//
//                hideKeyboard()
//                return true
//            }
//
//            override fun onShowPress(e: MotionEvent) {
//
//            }
//
//            override fun onSingleTapUp(e: MotionEvent): Boolean {
//                return true
//            }
//
//            override fun onScroll(
//                e1: MotionEvent,
//                e2: MotionEvent,
//                distanceX: Float,
//                distanceY: Float
//            ): Boolean {
//                return true
//            }
//
//            override fun onLongPress(e: MotionEvent) {
//
//            }
//
//            override fun onFling(
//                e1: MotionEvent,
//                e2: MotionEvent,
//                velocityX: Float,
//                velocityY: Float
//            ): Boolean {
//                return true
//            }
//
//
//        })
//
//        binding.layout.setOnTouchListener { _, event ->
//            detector!!.onTouchEvent(event)
//        }

        // 로그인 버튼 클릭 이벤트
        login_btn.setOnClickListener {
            Log.i ("loginBTN", "로그인 버튼")

            login(this, email_et.text.toString(), password_et.text.toString())

        }

        kakaoLogin_btn.setOnClickListener{
            Log.i ("kakaoLoginBTN", "카카오 로그인 버튼")
            loginWithKakao()
            // 카카오 로그인 버튼 클릭 이벤트
        }

        signUp_tv.setOnClickListener{
            Log.i ("signUpTV", "회원가입 클릭")

            var user = User()
            user.type = 1// 카카오 타입 가입

            val mIntent = Intent(this, ActivityBirthInput::class.java)

            mIntent.putExtra("user", user)

            startActivity(mIntent)
            overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit)
        }

        findPassword_tv.setOnClickListener{
            Log.i ("findPasswordTV", "비밀번호 찾기")
            val mIntent = Intent(this, ActivityFindPassword_InputEmail::class.java)
            startActivity(mIntent)
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkEditTexts()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        email_et.addTextChangedListener(textWatcher)
        password_et.addTextChangedListener(textWatcher)


    }

    private fun login(context: Context, email : String, password: String) {

        Log.i ("login", "로그인 실행")

        val retrofit = ApiClient.getApiClient()
        val userService =retrofit.create(UserDataInterface::class.java)

        val request = UserDataInterface.LoginRequest(email, password)

        val call = userService.login(request)

        call?.enqueue(object : Callback<JsonElement?> {

            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {
                Log.i("onSuccess", response.body().toString())

                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.asJsonObject

                    if (jsonResponse != null) {
                        val loginCheck = jsonResponse.get("메세지").asString
                        if (loginCheck == "true") {
                            val data = jsonResponse.get("데이터").asJsonObject
                            val jwt_token = data.get("토큰").asString
                            preferenceHelper.setIsLogin(jwt_token)
                            val mIntent = Intent(context, MainActivity::class.java)
                            startActivity(mIntent)
                        } else {
                            showLoginFailureDialog(context)
                        }
                    }else{
                        Log.i("onFailure", "응답이 올바르지 않음")
                    }
                }else{
                    Log.i("onFailure", "로그인 실패")
                    showLoginFailureDialog(context)
                }

            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                Log.i("onFailure", t.toString())
            }

        })

    }

    /***
     * 로그인 실패시 실행 함수
     */
    fun showLoginFailureDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("로그인 실패")
            .setMessage("로그인에 실패했습니다. 이메일이나 비밀번호를 확인해 주세요!")
            .setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }

        val dialog = builder.create()
        dialog.show()
    }

    fun loginWithKakao(){

        // 로그인 조합 예제

// 카카오계정으로 로그인 공통 callback 구성
// 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")

                UserApiClient.instance.me{ user, error ->
                    if (error != null) {
                        Log.e(TAG, "사용자 정보 요청 실패", error)
                    }
                    else if (user != null) {
                        Log.i(TAG, "사용자 정보 요청 성공" +
                                "\n회원번호: ${user.id}" +
                                "\n이메일: ${user.kakaoAccount?.email}" +
                                "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")

                        //이메일 체크
                        startKakaoLogin(user)



//                        val mIntent = Intent(this, MainActivity::class.java)
//                        startActivity(mIntent)
//                        finish()

                    }

                }
            }
        }

// 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    UserApiClient.instance.me{ user, error ->
                        if (error != null) {
                            Log.e(TAG, "사용자 정보 요청 실패", error)
                        }
                        else if (user != null) {
                            Log.i(TAG, "사용자 정보 요청 성공" +
                                    "\n회원번호: ${user.id}" +
                                    "\n이메일: ${user.kakaoAccount?.email}" +
                                    "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                    "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")

                            startKakaoLogin(user)
                        }
                    }
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }

    }

    private fun startKakaoLogin(user: com.kakao.sdk.user.model.User) {
        val email = user.kakaoAccount?.email

        if (email != null) {

            retrofitHelper.checkEmailDuplication(email , object : RetrofitHelper.signupType {
                override fun onSuccess() {// 이메일 중복 없음

                    val mIntent = Intent(getApplicationContext(), ActivityBirthInput::class.java)

                    var user = User()
                    user.email = email
                    user.type = 2// 카카오 타입 가입

                    mIntent.putExtra("user", user)

                    startActivity(mIntent)
                }

                override fun onFailure(userSignupType: Int) {// 이메일 중복 있음

                    if(userSignupType == 2){
                        // 이미 가입된 회원가입 타입이 카카오 계정이라면
                        //자동 로그인?
                        login(applicationContext, email, "")
                    }else{

                        Toast.makeText(getApplicationContext(), "이미 가입된 유저입니다. 이메일로 로그인 부탁드립니다.",Toast.LENGTH_SHORT).show()
                        // 혹은 비밀번호 찾기 유도?

                    }

                }

            })

        }


    }

    private fun checkEditTexts() {
        val editText1 = email_et
        val editText2 = password_et

        val isBothFilled = areBothEditTextsFilled(editText1, editText2)

        login_btn.isEnabled = isBothFilled

        // 여기에서 isBothFilled 값을 사용하거나 처리하면 됩니다.
    }
    fun areBothEditTextsFilled(editText1: EditText, editText2: EditText): Boolean {
        val text1 = editText1.text.toString()
        val text2 = editText2.text.toString()

        return text1.isNotEmpty() && text2.isNotEmpty()
    }

    private fun requestPermission() {// 노티피케이션 퍼미션 요청
        Log.i ("requestPermission", "requestPermission")
        TedPermission.create()
            .setPermissionListener(object : PermissionListener{
                override fun onPermissionGranted() {

                    Log.i ("정보태그", "권한이 이미 허용되어 있음")

                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {

                    Log.i ("정보태그", "권한이 허용되어 있지 않음"+deniedPermissions)

                    Toast.makeText(this@ActivityLogin,
                        "권한을 허가해주세요.",
                        Toast.LENGTH_SHORT)
                        .show()

                }

            }).setDeniedMessage("권한을 허용해주세요. [설정] > [앱 및 알림] > [고급] > [앱 권한]")
            .setPermissions(Manifest.permission.POST_NOTIFICATIONS)
            .check()

    }

    fun hideKeyboard() {
        val currentFocusView = currentFocus
        if (currentFocusView != null) {
            val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                currentFocusView.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }


}
