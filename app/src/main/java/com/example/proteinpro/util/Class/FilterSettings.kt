package com.example.proteinpro.util.Class

class FilterSettings {

    fun getEffectItems(): ArrayList<String> {
        val itemlist = arrayListOf(

            "유당없음",
            "소화편리"

        )
        return itemlist
    }
    fun getTypeItems(): ArrayList<String> {
        val itemlist = arrayListOf(

            "식물성 \uD83C\uDF31",
            "동물성 \uD83D\uDC2E",
            "MPI",
            "MPC",
            "WPC",
            "WPI",
            "WPH",
            "WPIH",
            "HSP",
            "ISP",
            "대두단백",
            "완두단백",
            "산양유단백"

        )
        return itemlist
    }
    fun getAllergyItems(): ArrayList<String> {
        val itemlist = arrayListOf(

            "우유",
            "호두",
            "땅콩",
            "잣",
            "메밀",
            "밀",
            "아몬드",
            "토마토"

        )
        return itemlist
    }
    fun getTasteItems(): ArrayList<String> {
        val itemlist = arrayListOf(

            "초코",
            "곡물",
            "딸기",
            "바나나",
            "커피",
            "쿠키앤크림",
            "바닐라",
            "기타"

        )
        return itemlist
    }
    fun getEtcItems(): ArrayList<String> {
        val itemlist = arrayListOf(

            "첨가물제로"

        )
        return itemlist
    }

}