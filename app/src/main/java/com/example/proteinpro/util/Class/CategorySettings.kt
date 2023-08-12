package com.example.proteinpro.util.Class

class CategorySettings {

    data class CategoryItem(
        val name: String = "",
        val Key : String = ""
    )

    object FoodListCategory {

        const val ALL = "0"// 전체
        const val SUPPLEMENTS = "1" // 보충제
        const val DRINKS = "2"// 음료
        const val SNACK_BARS = "3" // 과자 바
        const val CHICKEN_BREAST_RELATED = "4"// 달가슴살 관련
        const val TURKEY_BREAST = "5" // 닭가슴살
        const val SAUSAGES = "6" // 소세지
        const val STEAKS = "7" // 스테이크
        const val OTHER = "8" //

        val categoryItems: ArrayList<CategoryItem> = arrayListOf(

            CategoryItem("전체", ALL),
            CategoryItem("보충제", SUPPLEMENTS),
            CategoryItem("음료", DRINKS),
            CategoryItem("과자 바", SNACK_BARS),
            CategoryItem("달가슴살 관련", CHICKEN_BREAST_RELATED),
            CategoryItem("닭가슴살", TURKEY_BREAST),
            CategoryItem("소세지", SAUSAGES),
            CategoryItem("스테이크", STEAKS),
            CategoryItem("기타", OTHER)

        )

    }

    fun getCategoryItems() : ArrayList<CategoryItem>{
        return  FoodListCategory.categoryItems
    }

    /***
     * 카테고리 변경이 필요하면 이 함수에 접근해서 목록을 변경해 주면 됨
     */
    fun getCategoryItemsForSearch(): ArrayList<CategoryItem> {
        val list = FoodListCategory.categoryItems
        val proteinList = arrayListOf(

            list.get(1),
            list.get(2),
            list.get(3),
            list.get(5),
            list.get(6),
            list.get(7),
            list.get(0),
            list.get(8)

        )
        return proteinList
    }

}