package com.example.proteinpro.util.Class.food

data class AdditiveItem(

    val name: String,
    val ewgGrade: Int,
    val purpose: String

){
    companion object {
        // EWG 등급을 문자열로 변환하는 함수
        fun getEwgGradeText(ewgGrade: Int?): String {
            return when (ewgGrade) {
                null -> "미등록"
                in 0..2 -> "안전"
                in 3..6 -> "주의"
                in 7..10 -> "위험"
                else -> "미등록"
            }
        }
    }
}







