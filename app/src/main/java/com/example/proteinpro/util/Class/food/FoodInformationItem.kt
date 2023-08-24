package com.example.proteinpro.util.Class.food

data class FoodInformationItem(

 val key: String = "",// 키
 val name: String = "",// 제품명
 val taste: String = "",// 맛
 val price: Int = 0,// 가격
 val capacity: Double = 0.0,//용량
 val capacityUnit: String = "",// 용량 단위
 val image: String = "",// 이미지 url
 val costPerformance: Float = 0.0f,// 가성비
 val quantity: Int = 0, // 수량
 val brand: String = "",// 브랜드
 val company:String ="",// 회사
 val proteinTypeList : ArrayList<String> = ArrayList(),//프로틴 타입 리스트String
 val additiveList: ArrayList<AdditiveItem> = ArrayList(),//첨가물 리스트
 val allergyListStr: String="", //알러지 리스트 String
 val positiveReviewWords: String ="",// 긍정단어 리스트
 val negativeReviewWords: String ="",// 부정단어 리스트
 val tagList : ArrayList<String> = ArrayList(),// 태그 단어
 val nutritionFacts: NutritionFacts, // 영양성분
 val proteinList: ArrayList<ProteinDataItem> = ArrayList(),// 설명이 포함된 단백질 목록
 val rowMaterials: String = "",// 원재료
 val originMaterials: String = ""// 원재료 표기 원본

)