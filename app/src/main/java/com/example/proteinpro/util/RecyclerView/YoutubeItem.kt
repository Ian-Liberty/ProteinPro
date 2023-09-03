import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class YoutubeItem(
    val 키: String = "",
    val 타입: Int = 0,
    val 작성자: String = "",
    val 링크: String = "",
    val 내용: String = "",
    val 제목: String = "",
    val 설명: String = "",
    val 이미지주소: String = "",
    val 생성일: String = "",
    val 수정일: String = ""
) : Serializable