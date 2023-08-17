package com.example.proteinpro.util.Class

// 인텐트(Intent)를 사용하여 User 객체를 다른 액티비티로 전달하려면,
// User 클래스가 Serializable 또는 Parcelable 인터페이스를 구현해야 합니다.
// 이 인터페이스를 구현함으로써 객체가 직렬화(Serialization) 가능하게 되며, 인텐트에 객체를 담아서 전달할 수 있습니다.
//
//User 클래스를 Serializable 인터페이스를 사용하여 직렬화시켜 줌으로 써 User 객체는 intent에 putExtra 를 통해 전달 할 수 있습니다.
import java.io.Serializable
import com.example.proteinpro.util.Class.User.ActivityLevel as ActivityLevel

data class User(
    var email: String = "",
    var nickname: String = "",
    var birthDate: String = "",
    var password: String = "",
    var height: Int = -1,
    var weight: Int = -1,
    var gender: Int = -1,// 여성0 남성1
    var activityLevel: ActivityLevel = ActivityLevel.SEDENTARY
): Serializable {
    // 활동량을 정의하기 위한 enum 클래스
    enum class ActivityLevel {
        SEDENTARY, // 앉아서 일하는 경우 ("운동을 전혀 하지 않음")
        LIGHTLY_ACTIVE, // 가벼운 활동 ("주 1-3회 가벼운 운동")
        MODERATELY_ACTIVE, // 보통의 활동 ("주 4-5회 가볍거나 적당한 운동")
        VERY_ACTIVE, // 매우 활동적 ("주6-7회 적당한 운동/ 주3-4회 격한 운동" )
        EXTRA_ACTIVE // 매우 활동적 ("주 7회 격한 운동");


    }
    companion object {
        fun getActivitylevel(levelInt : Int): ActivityLevel {

            return when(levelInt){
                1 -> ActivityLevel.SEDENTARY
                2 -> ActivityLevel.LIGHTLY_ACTIVE
                3 -> ActivityLevel.MODERATELY_ACTIVE
                4 -> ActivityLevel.VERY_ACTIVE
                5 -> ActivityLevel.EXTRA_ACTIVE
                else -> {
                    ActivityLevel.SEDENTARY
                }
            }

        }

        fun getActivitylevelString(activityLevel : ActivityLevel) : String {
            return when (activityLevel) {
                ActivityLevel.SEDENTARY -> "운동 전혀 안함"
                ActivityLevel.LIGHTLY_ACTIVE ->"주 1-3회 가벼운 운동"
                ActivityLevel.MODERATELY_ACTIVE -> "주 4-5회 가볍거나 적당한 운동"
                ActivityLevel.VERY_ACTIVE -> "주6-7회 적당한 운동/ 주3-4회 격한 운동"
                ActivityLevel.EXTRA_ACTIVE -> "주 7회 격한 운동"
            }
        }
    }



    fun getActivityLevelIntensity(): Int {
        return when (activityLevel) {
            ActivityLevel.SEDENTARY -> 1
            ActivityLevel.LIGHTLY_ACTIVE -> 2
            ActivityLevel.MODERATELY_ACTIVE -> 3
            ActivityLevel.VERY_ACTIVE -> 4
            ActivityLevel.EXTRA_ACTIVE -> 5
        }
    }
}
