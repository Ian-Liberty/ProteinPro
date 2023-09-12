package com.example.proteinpro.util.Class.food

data class ReviewItem(

    var 긍정후기 : String = "",
    var 부정후기 : String ="",
    var 평점 : Int = 0,
    var 닉네임: String = "",
    var 키: String = "",
    var 사용자키:Int = 0,
    val 생성일: String = "",
    val 식품키 : String = "",
    var 이미지주소 : ArrayList<String> = ArrayList(),
    var 좋아요여부 : Int = 0,
    var 좋아요 : Int = 0,
    var 작성여부 : Int = 0

)