package com.example.proteinpro.user.util

// 인텐트(Intent)를 사용하여 User 객체를 다른 액티비티로 전달하려면,
// User 클래스가 Serializable 또는 Parcelable 인터페이스를 구현해야 합니다.
// 이 인터페이스를 구현함으로써 객체가 직렬화(Serialization) 가능하게 되며, 인텐트에 객체를 담아서 전달할 수 있습니다.
//
//User 클래스를 Serializable 인터페이스를 사용하여 직렬화시켜 줌으로 써 User 객체는 intent에 putExtra 를 통해 전달 할 수 있습니다.
import java.io.Serializable

data class User(
    var email: String = "",
    var nickname: String = "",
    var birthDate: String = "",
    var height: Double = 0.0,
    var weight: Double = 0.0,
    var activityLevel: ActivityLevel = ActivityLevel.SEDENTARY
): Serializable {
    // 활동량을 정의하기 위한 enum 클래스
    enum class ActivityLevel {
        SEDENTARY, // 앉아서 일하는 경우 (거의 활동하지 않음)
        LIGHTLY_ACTIVE, // 가벼운 활동 (일상적인 운동이나 운동이 없음)
        MODERATELY_ACTIVE, // 보통의 활동 (주 3-4회 운동)
        VERY_ACTIVE, // 매우 활동적 (매일 운동)
        EXTRA_ACTIVE // 매우 활동적 (매일 고강도 운동)
    }
}
