package com.example.proteinpro.view.main.userInfo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proteinpro.R
import com.example.proteinpro.databinding.ActivityMyReviewBinding
import com.example.proteinpro.util.Class.food.ReviewItem
import com.example.proteinpro.util.PreferenceHelper
import com.example.proteinpro.util.RecyclerView.ReviewListAdapter
import com.example.proteinpro.util.Retrofit.FoodRetrofitHelper
import com.example.proteinpro.util.Retrofit.RetrofitHelper
import com.example.proteinpro.view.main.search.AcitvityReviewWrite
import com.google.gson.Gson
import com.google.gson.JsonArray
import kotlin.properties.Delegates

class ActivityMyReview : AppCompatActivity() {

    private lateinit var preferenceHelper: PreferenceHelper
    private lateinit var foodRetrofitHelper: FoodRetrofitHelper

    // 전역 변수로 바인딩 객체 선언
    private var mBinding:
            ActivityMyReviewBinding? =null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!
    //!!는 Kotlin에서 Nullable 타입을 강제로 Non-nullable 타입으로 변환하는 것을 의미

    private var skip: Int = 1
    private val itemList : ArrayList<ReviewItem> = ArrayList()
    private lateinit var adapter: ReviewListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_review)

        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용해서
        // 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivityMyReviewBinding.inflate(layoutInflater)
        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의
        // 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시 합니다.
        setContentView(binding.root)// < 기존의 setContentView 는 주석 처리해 주세요!

        initUtils()
        initData()
        initViews()
        initListener()

    }

    private fun initData(){
        val token = preferenceHelper.get_jwt_Token()
        if(token != null){

            foodRetrofitHelper.getMyReviewList(token,skip,object : FoodRetrofitHelper.reviewListCallback{
                override fun onSuccess(reviewList: JsonArray) {

                    val gson = Gson()

                    for(item in reviewList) {
                        val reviewItem = gson.fromJson(item, ReviewItem::class.java)
                        itemList.add(reviewItem)
                    }

                    adapter.setItem_list(itemList)

                }

                override fun onFailure() {



                }

            })


        }
    }
    private fun initViews(){
        // 뷰 초기화

        // 뷰 초기화
        adapter = ReviewListAdapter(this, itemList, "myReview")
        binding.reviewListRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.reviewListRV.adapter = adapter

    }
    private fun initListener(){
        // 리스너 초기화

        val rv_Listener = object : ReviewListAdapter.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int, item: ReviewItem) {

            }

            override fun onMenuClick(v: View?, position: Int, item: ReviewItem) {
                Log.i ("onMenuClick", ""+item.키)
                showPopupMenuDialog(this@ActivityMyReview, item)
            }

            override fun onLikeClick(btn: TextView?, position: Int, item: ReviewItem) {

                Log.i("onLikeClick", ""+item.키)

                val token = preferenceHelper.get_jwt_Token().toString()

                if(item.좋아요여부 == 1){
                    // 현재 이 유저가 좋아요를 한 상태 그러므로 취소 로직을 실행 해 줘야함
                    foodRetrofitHelper.deleteLike(item.키.toInt(),token){
                        if(it){
                            Log.i ("정보태그", "좋아요 취소 성공")

                            item.좋아요여부 = 0
                            item.좋아요= item.좋아요-1
                            adapter.setLikeState(position, item)

                        }else{
                            Log.i ("정보태그", "좋아요 취소 실패")
                        }

                    }
                }else{
                    // 현재 이 유저가 좋아요 하기 전인 상태 좋아요를 실행 해 줘야함

                    foodRetrofitHelper.addLike("1",item.키, token){
                        if(it){
                            Log.i ("정보태그", "좋아요 성공")
                            item.좋아요여부 = 1
                            item.좋아요= item.좋아요+1
                            adapter.setLikeState(position, item)
                        }else{
                            Log.i ("정보태그", "좋아요 실패")
                        }

                    }

                }

            }

        }

        adapter.setOnItemClickListener(rv_Listener)
    }
    private fun initUtils(){
        // 유틸 클래스 초기화

        preferenceHelper = PreferenceHelper(this)
        foodRetrofitHelper = FoodRetrofitHelper(this)

    }


    fun showPopupMenuDialog(context: Context, item: ReviewItem) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("") // 팝업 알림 제목 설정

        // 팝업 알림에 표시될 옵션들 (수정, 삭제, 취소)
        val options = arrayOf("수정하기", "삭제하기", "취소")

        alertDialogBuilder.setItems(options) { dialog, which ->
            when (which) {
                0 -> {
                    // "수정" 옵션 선택 시 수행할 동작 추가
                    val editIntent = Intent(context, AcitvityReviewWrite::class.java)
                    // 필요한 데이터를 Intent에 추가할 수 있음
                    editIntent.putExtra("ItemId", item.키) // 리뷰 항목의 ID를 전달
                    editIntent.putExtra("name", title)
                    editIntent.putExtra("editType", "update")
                    context.startActivity(editIntent)
                }
                1 -> {
                    // "삭제" 옵션 선택 시 수행할 동작 추가
                    // 삭제 로직을 구현하고 호출
                    // item.id를 사용하여 리뷰를 삭제하는 코드를 작성
                    // 예: deleteReview(item.id)
                    showDeleteConfirmationDialog(context, item ){
                        // 삭제로직
                        deleteReview(context, item)
                    }

                }
                2 -> {
                    // "취소" 옵션 선택 시 아무런 동작을 하지 않음
                }
            }
            dialog.dismiss() // 팝업 알림 닫기
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show() // 팝업 알림 표시
    }

    private fun deleteReview(context: Context, item: ReviewItem) {

        var token : String = ""

        if(preferenceHelper.get_jwt_Token() != null) {
            token = preferenceHelper.get_jwt_Token()!!
        }

        Log.i ("정보태그", "삭제 로직!")
        foodRetrofitHelper.deleteReview(item.키.toInt(), token)
        {
            if(it){
                Toast.makeText(getApplicationContext(), "정상적으로 삭제 되었습니다.", Toast.LENGTH_SHORT).show()
                itemList.remove(item)
                adapter.setItem_list(itemList)

            }else{
                Toast.makeText(getApplicationContext(), "삭제를 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
            }
        }


    }


    fun showDeleteConfirmationDialog(context: Context, item: ReviewItem, onDeleteConfirmed: () -> Unit) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("삭제 확인")
        alertDialogBuilder.setMessage("정말 삭제하시겠습니까?")

        alertDialogBuilder.setPositiveButton("확인") { dialog, _ ->
            onDeleteConfirmed.invoke() // 삭제 함수를 호출
            dialog.dismiss()
        }

        alertDialogBuilder.setNegativeButton("취소") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

}