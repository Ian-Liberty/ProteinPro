package com.example.proteinpro.util.Class

import java.util.logging.Filter

class FilterSettings {

    object FilterType {

        const val ALL = 0// 전체(효과)
        const val KIND = 1 // 종류
        const val ALLERGY = 2// 알러지
        const val TASTE = 3 // 맛
        const val ETC = 4 // 맛

    }

    data class FilterItem(
        val type: Int = -1,
        val name: String = "",// http요청시 사용해야하는 이름
        val Key : String = "", // 앱 에서 조회하기 위한 키 및 표시되는 이름
        val child: ArrayList<FilterItem> = ArrayList()
    )

    val mpi = FilterItem(FilterType.KIND,"분리우유단백","MPI")
    val mpc = FilterItem(FilterType.KIND,"농축우유단백","MPC")
    val wpc = FilterItem(FilterType.KIND,"농축유청단백","WPC")
    val wpi =  FilterItem(FilterType.KIND,"분리유청단백","WPI")
    val wpih = FilterItem(FilterType.KIND,"가수분해분리유청단백","WPIH")
    val wph = FilterItem(FilterType.KIND,"가수분해유청단백","WPH")
    val hsp = FilterItem(FilterType.KIND,"가수분해대두단백","HSP")
    val isp = FilterItem(FilterType.KIND,"분리우유단백","ISP")
    val 대두단백 = FilterItem(FilterType.KIND,"대두단백","대두단백")
    val 완두단백 = FilterItem(FilterType.KIND,"완두단백","완두단백")
    val 산양유단백 = FilterItem(FilterType.KIND,"산양유단백","산양유단백")

    val 우유 = FilterItem(FilterType.ALLERGY,"우유", "우유 \uD83E\uDD5B")
    val 호두 = FilterItem(FilterType.ALLERGY,"호두", "호두 \uD83D\uDC3F️")
    val 땅콩 = FilterItem(FilterType.ALLERGY,"땅콩", "땅콩 \uD83E\uDD5C")
    val 잣 = FilterItem(FilterType.ALLERGY,"잣", "잣 \uD83C\uDF32")
    val 메밀 = FilterItem(FilterType.ALLERGY,"메밀", "메밀 \uD83C\uDF3E")
    val 밀 = FilterItem(FilterType.ALLERGY,"밀", "밀 \uD83C\uDF3E")
    val 아몬드 = FilterItem(FilterType.ALLERGY,"아몬드", "아몬드 \uD83E\uDD5C")
    val 토마토 = FilterItem(FilterType.ALLERGY,"토마토", "토마토 \uD83C\uDF45")

    val 초코 = FilterItem(FilterType.TASTE,"초코", "초코 \uD83C\uDF6B")
    val 곡물 = FilterItem(FilterType.TASTE,"곡물", "곡물 \uD83C\uDF3E")
    val 딸기 = FilterItem(FilterType.TASTE,"딸기", "딸기 \uD83C\uDF53")
    val 바나나 = FilterItem(FilterType.TASTE,"바나나", "바나나 \uD83C\uDF4C")
    val 커피 = FilterItem(FilterType.TASTE,"커피", "커피 ☕")
    val 쿠키앤크림 = FilterItem(FilterType.TASTE,"쿠키앤크림", "쿠키앤크림\uD83C\uDF6A")
    val 바닐라 = FilterItem(FilterType.TASTE,"바닐라", "바닐라 \uD83C\uDF66")
    val 기타 = FilterItem(FilterType.TASTE,"기타", "기타 \uD83D\uDCAC")

    val 첨가물제로 = FilterItem(FilterType.ETC,"첨가물","첨가물제로 \uD83D\uDE45\u200D♂️")

    val 유당없음list = arrayListOf<FilterItem>(
        wpc,wph,대두단백,isp,hsp,완두단백
    )
    val 소화편리list = arrayListOf<FilterItem>(
        wph,대두단백
    )
    val 동물성list = arrayListOf<FilterItem>(
        mpi,mpc,wpc,wph,wpih,wpi,산양유단백
    )
    val 식물성list = arrayListOf<FilterItem>(
        대두단백,isp,hsp,완두단백
    )

    val effectFilterItems: ArrayList<FilterItem> = arrayListOf(

        FilterItem(FilterType.ALL,"유당없음","유당없음 \uD83D\uDE45\u200D♂️", 유당없음list),
        FilterItem(FilterType.ALL,"소화편리","소화편리 \uD83D\uDE00", 소화편리list)

    )
    val kindFilterItems: ArrayList<FilterItem> = arrayListOf(

        FilterItem(FilterType.ALL,"식물성단백질","식물성 \uD83C\uDF3F", 식물성list),
        FilterItem(FilterType.ALL,"동물성단백질","동물성 \uD83D\uDC2E", 동물성list),
        mpc,
        mpi,
        wpc,
        wpi,
        wph,
        wpih,
        hsp,
        isp,
        대두단백,
        완두단백,
        산양유단백
    )
    val allergyFilterItems: ArrayList<FilterItem> = arrayListOf(

        우유,호두,땅콩,잣,메밀,밀,아몬드,토마토

    )
    val tasteFilterItems: ArrayList<FilterItem> = arrayListOf(
        //초코,곡물,딸기,바나나,커피,쿠키앤크림,바닐라,기타

        초코,곡물,딸기,바나나,커피,쿠키앤크림,바닐라,기타

    )

    val etcFilterItems: ArrayList<FilterItem> = arrayListOf(

        첨가물제로

    )

    val allFilters: ArrayList<FilterItem> = arrayListOf(
        // effectFilterItems 내의 아이템들
        FilterItem(FilterType.ALL,"유당없음","유당없음 \uD83D\uDE45\u200D♂️", 유당없음list),
        FilterItem(FilterType.ALL,"소화편리","소화편리 \uD83D\uDE00", 소화편리list),

        // kindFilterItems 내의 아이템들
        FilterItem(FilterType.ALL,"식물성단백질","식물성 \uD83C\uDF31", 유당없음list),
        FilterItem(FilterType.ALL,"동물성단백질","동물성 \uD83D\uDC2E", 유당없음list),
        mpc,
        mpi,
        wpc,
        wpi,
        wph,
        wpih,
        hsp,
        isp,
        대두단백,
        완두단백,
        산양유단백,

        // allergyFilterItems 내의 아이템들
        우유,호두,땅콩,잣,메밀,밀,아몬드,토마토,

        // tasteFilterItems 내의 아이템들
        초코,곡물,딸기,바나나,커피,쿠키앤크림,바닐라,기타,

        // etcFilterItems 내의 아이템들
        첨가물제로
    )

    fun getFilterKeys(filterItems: ArrayList<FilterItem>): ArrayList<String> {

        val keyList = arrayListOf<String>()
        for (item in filterItems) {
            keyList.add(item.Key)
        }
        return keyList

    }

    fun getEffectItems():ArrayList<String> {
        return getFilterKeys(effectFilterItems)
    }

    fun getKindItems(): ArrayList<String> {
        return getFilterKeys(kindFilterItems)
    }
    fun getAllergyItems(): ArrayList<String> {
        return getFilterKeys(allergyFilterItems)
    }
    fun getTasteItems(): ArrayList<String> {
        return getFilterKeys(tasteFilterItems)
    }
    fun getEtcItems(): ArrayList<String> {
        return getFilterKeys(etcFilterItems)
    }

    fun findFilterItemsByKeys(keys: ArrayList<String>): ArrayList<FilterItem> {
        val foundItems = arrayListOf<FilterItem>()

        for (key in keys) {
            val foundItem = findFilterItemByKey(key)
            if (foundItem != null) {
                foundItems.add(foundItem)
            }
        }

        return foundItems
    }

    fun findFilterItemByKey(key: String): FilterItem? {

        val filterItems = allFilters
        for (item in filterItems) {
            if (item.Key == key) {
                return item
            }
        }
        return null
    }


}