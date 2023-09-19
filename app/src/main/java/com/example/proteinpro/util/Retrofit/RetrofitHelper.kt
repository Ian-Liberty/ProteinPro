package com.example.proteinpro.util.Retrofit

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.proteinpro.util.PreferenceHelper
import com.example.proteinpro.util.Class.User
import com.example.proteinpro.util.Class.WalletDataItem
import com.example.proteinpro.view.main.MainActivity
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitHelper(context: Context?) {

    private val retrofit = ApiClient.getApiClient()
    private val ctx = context

    interface WalletData {
        fun onSuccess(walletData: WalletDataItem)
        fun onFailure()
    }

    interface signupType {
        fun onSuccess()
        fun onFailure(userSignupType : Int)
    }

    /***
     * 인증번호 요청
     * @param email : 이메일 String
     *
     * 파라미터 이메일로 인증번호를 요청합니다.
     * 실패 시 인증번호 실패 toast 를 띄워줍니다.
     *
     * 사용법
     * requestCertNum("example@example.com") { isSuccess ->
        if (isSuccess) {
        // 인증번호 요청 성공
        } else {
        // 인증번호 요청 실패
        }
        }
     */
    fun requestCertNum( email: String, onResult: (Boolean) -> Unit){
        //인증번호 요청
        Log.i ("requestCertNum", "인증번호 요청")

        val api =retrofit.create(UserDataInterface::class.java)// 사용할 인터페이스

        val request = UserDataInterface.이메일기본(email)
        // 로그인의 경우 UserDataInterface.LoginRequest(email, password)

        val call = api.인증번호전송(request)

        call?.enqueue(object : Callback<JsonElement?> {

            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {

                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.asJsonObject
                    Log.i("onSuccess", response.body().toString())

                    if (jsonResponse != null) {
                        //             응답에서 변수 호출    jsonResponse.get("키값").asString
                        val result = jsonResponse.get("메세지").asString
                        val data = jsonResponse.get("데이터").asJsonObject

                        if(result == "true"){
                            // 인증번호 전송함
                            Log.i ("requestCertNum", "인증번호 요청 성공")
                            val token = data.get("토큰").asString
                            val pref = PreferenceHelper(ctx)
                            pref.setCheckToken(token)
                            Log.i ("인증토큰정보", token+"")

                            onResult(true)
                        }else{
                            // 인증번호 전송 실패
                            Log.e ("requestCertNum", "인증번호 요청 성공")
                            Toast.makeText(ctx, "인증번호 전송에 실패했습니다. 관리자에게 문의 하세요",
                                Toast.LENGTH_SHORT).show()
                            onResult(false)
                        }

                    } else {
                        Log.e("onFailure", "응답이 올바르지 않음 : jsonResponse 값이 null 임")
                        onResult(false)
                    }
                } else {
                    Log.e("onFailure", "응답이 올바르지 않음 : response.isSuccessful 값이 false 임")
                    onResult(false)
                }

            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                Log.e("onFailure", t.toString())
                onResult(false)
            }
        })

    }

    fun login(context: Context, email : String, password: String, onResult: (Boolean) -> Unit) {

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

                            val pref = PreferenceHelper(ctx)
                            pref.setIsLogin(jwt_token)

                            onResult(true)
                        } else {
                            onResult(false)
                        }
                    }else{
                        Log.i("onFailure", "응답이 올바르지 않음")
                        onResult(false)
                    }
                }else{
                    Log.i("onFailure", "로그인 실패")
                    onResult(false)
                }

            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                Log.i("onFailure", t.toString())
                onResult(false)
            }

        })

    }

    /***
     * 중복값 있으면 true 중복 없으면 false
     */
   fun checkEmailDuplication(email: String, callback: signupType) {

        Log.i ("checkEmailDuplication", "이메일 중복 검사")

        val api = retrofit.create(UserDataInterface::class.java)// 사용할 인터페이스

        val request = UserDataInterface.이메일기본(email)
        // 로그인의 경우 UserDataInterface.LoginRequest(email, password)

        val call = api.checkEmailDuplication(request)

        call?.enqueue(object : Callback<JsonElement?> {

            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {

                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.asJsonObject
                    Log.i("onSuccess", response.body().toString())

                    if (jsonResponse != null) {
                        // 응답에서 변수 호출 jsonResponse.get("키값").asString
                        val result = jsonResponse.get("메세지").asString

                        if(result == "false"){
                            // 중복된 이메일 있음
//                            Toast.makeText(ctx, "이미 사용중인 이메일 입니다.",Toast.LENGTH_SHORT).show()
                            val data = jsonResponse.get("데이터").asJsonObject
                            val type = data.get("타입").asInt

                            callback.onFailure(type)
                        }else{
                            // 이메일 중복 없음
                            callback.onSuccess()
                        }

                    } else {
                        Log.i("onFailure", "응답이 올바르지 않음")
                        callback.onFailure(-1)// 예외사항은 -1 로 표기
                    }
                } else {
                    Log.i("onFailure", "응답이 올바르지 않음")
                    callback.onFailure(-1)
                }

                Log.i ("checkEmailDuplication", "이메일 중복 검사끝")

            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                Log.i("onFailure", t.toString())
                callback.onFailure(-1)
            }

        })

    }

    /***
     * 받은 번호를 체크함
     */
    fun checkAuthnum(inputNum: String,token: String, onResult: (Boolean) -> Unit) {

        val retrofit = ApiClient.getApiClient()
        val api =retrofit.create(UserDataInterface::class.java)// 사용할 인터페이스

        val request = UserDataInterface.인증번호정보(token, inputNum)
        // 로그인의 경우 UserDataInterface.LoginRequest(email, password)

        val call = api.인증번호체크(request)

        call?.enqueue(object : Callback<JsonElement?> {

            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {

                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.asJsonObject
                    Log.i("onSuccess", response.body().toString())

                    if (jsonResponse != null) {
        //             응답에서 변수 호출    jsonResponse.get("키값").asString
                        if(jsonResponse.get("메세지").asString== "true"){
                            Log.i ("checkAuthnum", ""+jsonResponse.get("메시지"))
                            onResult(true)
                        }else{
                            Log.i ("checkAuthnum", ""+jsonResponse.get("메시지"))
                            Toast.makeText(ctx, "인증번호가 올바르지 않습니다.",Toast.LENGTH_SHORT).show()
                            onResult(false)
                        }

                    } else {
                        Log.i("onFailure", "응답이 올바르지 않음")

                    }
                } else {
                    Log.i("onFailure", "응답이 올바르지 않음")
                    onResult(false)
                }

            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                Log.i("onFailure", t.toString())
                onResult(false)
            }
        })

    }

    fun checkNicknameDuplication(nickName: String, onResult: (Boolean) -> Unit){


        val api =retrofit.create(UserDataInterface::class.java)// 사용할 인터페이스

        val request = UserDataInterface.닉네임기본(nickName)
        // 로그인의 경우 UserDataInterface.LoginRequest(email, password)

        val call = api.닉네임중복체크(request)

        call?.enqueue(object : Callback<JsonElement?> {

            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {


                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.asJsonObject
                    Log.i("onSuccess", response.body().toString())

                    if (jsonResponse != null) {
        //             응답에서 변수 호출    jsonResponse.get("키값").asString

                        if(jsonResponse.get("메세지").asString== "true"){
                            Log.i ("checkNicknameDuplication", ""+jsonResponse.get("데이터"))
                            onResult(true)
                        }else{
                            Log.i ("checkNicknameDuplication", ""+jsonResponse.get("데이터"))
                            Toast.makeText(ctx, "이미 사용중인 닉네임 입니다.",Toast.LENGTH_SHORT).show()
                            onResult(false)
                        }


                    } else {
                        Log.i("onFailure", "응답이 올바르지 않음")
                        onResult(false)
                    }
                } else {
                    Log.i("onFailure", "응답이 올바르지 않음")
                    onResult(false)
                }

            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                Log.i("onFailure", t.toString())
                onResult(false)
            }
        })

    }

    fun signUp(user: User, onResult: (Boolean) -> Unit){

        val 닉네임 = user.nickname
        val 이메일 = user.email
        val 비밀번호 = user.password
        val 성별 = user.gender
        val 생년월일 =user.birthDate
        val 몸무게 = user.weight
        val 신장 = user.height
        val 활동량 = user.getActivityLevelIntensity()
        val 등급 = 0
        val 탈퇴 = 0// 탈퇴전
        val 타입 = user.type// 일반로그인


        val api =retrofit.create(UserDataInterface::class.java)// 사용할 인터페이스

        val request = UserDataInterface.회원가입기본(닉네임, 이메일, 비밀번호, 성별, 생년월일, 몸무게, 신장, 활동량, 등급, 탈퇴, 타입)

        val call = api.회원가입(request)

        call?.enqueue(object : Callback<JsonElement?> {

            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {

                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.asJsonObject
                    Log.i("onSuccess", response.body().toString())

                    if (jsonResponse != null) {
        //             응답에서 변수 호출    jsonResponse.get("키값").asString
                        if(jsonResponse.get("메세지").asString == "true"){
                            onResult(true)
                            Log.i ("signUp", ""+jsonResponse.get("데이터"))
                        }else{
                            onResult(false)
                            Log.i ("signUp", ""+jsonResponse.get("데이터"))
                        }

                    } else {
                        Log.i("onFailure", "응답이 올바르지 않음")
                        onResult(false)
                    }
                } else {
                    Log.i("onFailure", "응답이 올바르지 않음")
                    onResult(false)
                }

            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                Log.i("onFailure", t.toString())
                onResult(false)
            }
        })


    }

    fun userDataUpdate(user: User,token: String, onResult: (Boolean) -> Unit){
        val 닉네임 = user.nickname
        val 이메일 = user.email
        val 성별 = user.gender
        val 생년월일 =user.birthDate
        val 몸무게 = user.weight
        val 신장 = user.height
        val 활동량 = user.getActivityLevelIntensity()

        val retrofit = ApiClient.getApiClient()
        val api =retrofit.create(UserDataInterface::class.java)// 사용할 인터페이스

        val request = UserDataInterface.회원정보변경기본(닉네임, 이메일, 성별, 생년월일, 몸무게, 신장, 활동량)
        // 로그인의 경우 UserDataInterface.LoginRequest(email, password)

        val call = api.사용자정보변경하기(request, "Bearer "+token)

        call?.enqueue(object : Callback<JsonElement?> {

            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {

                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.asJsonObject
                    Log.i("onSuccess", response.body().toString())

                    if (jsonResponse != null) {
        //             응답에서 변수 호출    jsonResponse.get("키값").asString
                        if(jsonResponse.get("메세지").asString == "true"){
                            onResult(true)
                            Log.i ("userDataUpdate", ""+jsonResponse.get("데이터"))
                        }else{
                            onResult(false)
                            Log.i ("userDataUpdate", ""+jsonResponse.get("데이터"))
                        }

                    } else {

                        Log.e("onFailure", "응답이 올바르지 않음 : jsonResponse 값이 null 임")
                        onResult(false)
                    }
                } else {

                    Log.e("onFailure", "응답이 올바르지 않음 : response.isSuccessful 값이 false 임")
                    onResult(false)
                }

            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                Log.i("onFailure", t.toString())
                onResult(false)
            }
        })

    }

    fun resetPassword(email: String , password: String, token: String, onResult: (Boolean) -> Unit){

        val api =retrofit.create(UserDataInterface::class.java)// 사용할 인터페이스

        val request = UserDataInterface.비밀번호재설정기본(email, password)
        // 로그인의 경우 UserDataInterface.LoginRequest(email, password)

        val call = api.비밀번호재설정(request, "Bearer "+token)

        call?.enqueue(object : Callback<JsonElement?> {

            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {


                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.asJsonObject
                    Log.i("onSuccess", response.body().toString())

                    if (jsonResponse != null) {
        //             응답에서 변수 호출    jsonResponse.get("키값").asString
                        if(jsonResponse.get("메세지").asString == "true"){//비밀번호 변경 성공
                        Log.i ("정보태그", ""+jsonResponse.get("메세지"))

                            onResult(true)
                        }
                        else{
                        Log.i ("정보태그", ""+jsonResponse.get("메세지"))

                            onResult(false)
                        }

                    } else {
                        Log.e("onFailure", "응답이 올바르지 않음 : jsonResponse 값이 null 임")
                        onResult(false)
                    }
                } else {
                    Log.e("onFailure", "응답이 올바르지 않음 : response.isSuccessful 값이 false 임")
                    onResult(false)
                }

            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                Log.i("onFailure", t.toString())
                onResult(false)
            }
        })

    }

    fun changePassword(password: String , changePassword: String, token: String, onResult: (Boolean) -> Unit){

        val api =retrofit.create(UserDataInterface::class.java)// 사용할 인터페이스

        val request = UserDataInterface.비밀번호변경기본(password, changePassword)
        // 로그인의 경우 UserDataInterface.LoginRequest(email, password)

        val call = api.비밀번호변경하기(request, "Bearer "+token)

        call?.enqueue(object : Callback<JsonElement?> {

            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {


                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.asJsonObject
                    Log.i("onSuccess", response.body().toString())

                    if (jsonResponse != null) {
                        //             응답에서 변수 호출    jsonResponse.get("키값").asString
                        if(jsonResponse.get("메세지").asString == "true"){
                            Log.i ("정보태그", ""+jsonResponse.get("데이터"))
                            onResult(true)
                        }else{
                            Log.i ("정보태그", ""+jsonResponse.get("데이터"))
                            onResult(false)
                        }

                    } else {
                        Log.e("onFailure", "응답이 올바르지 않음 : jsonResponse 값이 null 임")
                        onResult(false)
                    }
                } else {
                    Log.e("onFailure", "응답이 올바르지 않음 : response.isSuccessful 값이 false 임")
                    onResult(false)
                }

            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                Log.i("onFailure", t.toString())
                onResult(false)
            }
        })

    }

    fun checkToken(token: String, onResult: (Boolean) -> Unit){

        val retrofit = ApiClient.getApiClient()
        val api =retrofit.create(UserDataInterface::class.java)// 사용할 인터페이스

        val request = UserDataInterface.토큰기본(token)
        // 로그인의 경우 UserDataInterface.LoginRequest(email, password)

        val call = api.사용자토큰체크("Bearer "+token)

        call?.enqueue(object : Callback<JsonElement?> {

            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {

                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.asJsonObject
                    Log.i("onSuccess", response.body().toString())

                    if (jsonResponse != null) {
        //             응답에서 변수 호출    jsonResponse.get("키값").asString
                        if(jsonResponse.get("메세지").asString == "true"){
                            Log.i ("정보태그", ""+jsonResponse.get("메세지"))
                            onResult(true)
                        }else{
                            Log.i ("정보태그", ""+jsonResponse.get("메세지"))
                            onResult(false)
                        }
                    } else {
                        Log.e("onFailure", "응답이 올바르지 않음 : jsonResponse 값이 null 임")
                        onResult(false)
                    }
                } else {
                    Log.e("onFailure", "응답이 올바르지 않음 : response.isSuccessful 값이 false 임")
                    onResult(false)
                }

            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                Log.i("onFailure", t.toString())
                onResult(false)
            }

        })

    }

    fun getUserInfo(token: String, onResult: (User?, Boolean) -> Unit){

        val retrofit = ApiClient.getApiClient()
        val api =retrofit.create(UserDataInterface::class.java)// 사용할 인터페이스


        val call = api.사용자정보불러오기("Bearer "+token)

        call?.enqueue(object : Callback<JsonElement?> {

            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {

                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.asJsonObject
                    Log.i("onSuccess", response.body().toString())

                    if (jsonResponse != null) {
        //             응답에서 변수 호출    jsonResponse.get("키값").asString
                        if(jsonResponse.get("메세지").asString == "true"){
                            val data  = jsonResponse.get("데이터").asJsonObject


                            val user = User(
                                email = data.get("이메일").asString,
                                nickname = data.get("닉네임").asString,
                                birthDate = data.get("생년월일").asString,
                                height = data.get("신장").asInt,
                                weight = data.get("몸무게").asInt,
                                gender = data.get("성별").asInt,
                                activityLevel = User.getActivitylevel(data.get("활동량").asInt),
                                type = data.get("타입").asInt
                                // 나머지 필요한 정보들도 마찬가지로 추출해야 함
                            )

                            Log.i ("정보태그", ""+jsonResponse.get("메세지"))
                            onResult(user, true)
                        }else{
                            Log.i ("정보태그", ""+jsonResponse.get("메세지"))
                            onResult(null, false)
                        }

                    } else {
                        Log.e("onFailure", "응답이 올바르지 않음 : jsonResponse 값이 null 임")
                        onResult(null, false)
                    }
                } else {
                    Log.e("onFailure", "응답이 올바르지 않음 : response.isSuccessful 값이 false 임")
                    onResult(null, false)
                }

            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                Log.i("onFailure", t.toString())
                onResult(null, false)
            }
        })

    }

    fun userWithdraw(password: String, token: String, onResult: (Boolean) -> Unit){

        val api =retrofit.create(UserDataInterface::class.java)// 사용할 인터페이스

        val request = UserDataInterface.회원탈퇴기본(password)
        // 로그인의 경우 UserDataInterface.LoginRequest(email, password)

        val call = api.회원탈퇴(request,"Bearer "+token)

        call?.enqueue(object : Callback<JsonElement?> {

            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {


                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.asJsonObject
                    Log.i("onSuccess", response.body().toString())

                    if (jsonResponse != null) {
                        //             응답에서 변수 호출    jsonResponse.get("키값").asString
                        if(jsonResponse.get("메세지").asString == "true"){
                            Log.i ("정보태그", ""+jsonResponse.get("데이터"))
                            onResult(true)
                        }else{
                            Log.i ("정보태그", ""+jsonResponse.get("데이터"))
                            onResult(false)
                        }

                    } else {
                        Log.e("onFailure", "응답이 올바르지 않음 : jsonResponse 값이 null 임")
                        onResult(false)
                    }
                } else {
                    Log.e("onFailure", "응답이 올바르지 않음 : response.isSuccessful 값이 false 임")
                    onResult(false)
                }

            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                Log.i("onFailure", t.toString())
                onResult(false)
            }
        })

    }

    fun getWallet(token: String, callback: WalletData){

        val retrofit = ApiClient.getApiClient()
        val api =retrofit.create(UserDataInterface::class.java)// 사용할 인터페이스



        val call = api.지갑정보가져오기("Bearer "+token)

        call?.enqueue(object : Callback<JsonElement?> {

            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {


                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.asJsonObject
                    Log.i("onSuccess", response.body().toString())

                    if (jsonResponse != null) {
        //             응답에서 변수 호출    jsonResponse.get("키값").asString
                        //             응답에서 변수 호출    jsonResponse.get("키값").asString
                        if(jsonResponse.get("메세지").asString == "true"){
                            Log.i ("정보태그", ""+jsonResponse.get("데이터"))
                            val data = jsonResponse.get("데이터")

                            val gson = Gson()

                            val item = gson.fromJson(data, WalletDataItem::class.java)

                            callback.onSuccess(item)
                        }else{
                            Log.i ("정보태그", ""+jsonResponse.get("데이터"))
                            callback.onFailure()
                        }


                    } else {
                        Log.e("onFailure", "응답이 올바르지 않음 : jsonResponse 값이 null 임")
                        callback.onFailure()
                    }
                } else {
                    Log.e("onFailure", "응답이 올바르지 않음 : response.isSuccessful 값이 false 임")
                    callback.onFailure()
                }

            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                Log.i("onFailure", t.toString())
                callback.onFailure()
            }
        })

    }



}