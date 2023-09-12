package com.example.proteinpro.util.RecyclerView

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proteinpro.R
import com.example.proteinpro.util.Class.food.ReviewItem
import com.example.proteinpro.util.Retrofit.ServerData

class ReviewListAdapter(private val context: Context, private var itemList: ArrayList<ReviewItem>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var mListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int, item: ReviewItem)
        fun onMenuClick(v: View?, position: Int, item: ReviewItem)
        fun onLikeClick(btn: TextView?, position: Int, item: ReviewItem)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {

        mListener = listener

    }

    fun setItem_list(item_list: ArrayList<ReviewItem>) {
        itemList = item_list
        notifyDataSetChanged()
    }

    fun setLikeState(position: Int, item: ReviewItem){

        itemList.set(position,item)
        notifyItemChanged(position)

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val nickname_tv : TextView = view.findViewById(R.id.nickname_TV)
        val good_tv : TextView = view.findViewById(R.id.good_TV)
        val bad_tv : TextView = view.findViewById(R.id.bad_TV)
        val like_tv : TextView = view.findViewById(R.id.like_TV)
        val review_rb: RatingBar = view.findViewById(R.id.review_RB)
        val date_tv: TextView = view.findViewById(R.id.date_TV)
        val menu_btn: ImageButton = view.findViewById(R.id.menu_IB)
        val imagelist_rv: RecyclerView = view.findViewById(R.id.imageList_rv)

        fun setdata(context: Context, item: ReviewItem ) {

            nickname_tv.setText(item.닉네임)
            good_tv.setText(item.긍정후기)
            bad_tv.setText(item.부정후기)
            like_tv.setText("👍"+item.좋아요)
            if(item.좋아요여부 == 1){
                //이 유저가 좋아요 했음
                like_tv.setBackgroundResource(R.drawable.round_background_border_fill_blue)
                like_tv.setTextColor(Color.WHITE)

            }else{
                like_tv.setBackgroundResource(R.drawable.round_background_border_blue2)
                like_tv.setTextColor(Color.BLUE)

            }

            if(item.작성여부 == 1){
                menu_btn.visibility = View.VISIBLE
            }else{
                menu_btn.visibility = View.GONE
            }
            date_tv.setText(item.생성일)
            review_rb.rating = item.평점.toFloat()

            //        Glide.with(context).load(ServerData.img_URL_board+item.이미지주소).into(thumbnail_iv)
            // 본서버 적용시 위 코드를 사용할 것
            val imgList : ArrayList<Uri> = ArrayList()

            for(data in item.이미지주소){

                val uriStr = ServerData.img_URL_review+data

                val imageUri = Uri.parse(uriStr)

                imgList.add(imageUri)

            }


            val nestedLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val imageAdapter = PhotoListAdapter(context,imgList, "list") // 중첩된 리사이클러뷰의 아이템 목록을 전달
            imagelist_rv.layoutManager = nestedLayoutManager
            imagelist_rv.adapter = imageAdapter

            // 이미지 설정 등 추가적인 작업이 필요하면 여기에 작성


            itemView.setOnClickListener{
                val pos = adapterPosition

                if(pos != RecyclerView.NO_POSITION){
                    //리스너 객체의 메서드 호출
                    Log.i ("리사이클러뷰 정보", "pos : $pos")
                    mListener?.onItemClick(itemView, pos, item)

                }

            }

            menu_btn.setOnClickListener {
                val pos = adapterPosition

                if(pos != RecyclerView.NO_POSITION){
                    //리스너 객체의 메서드 호출
                    Log.i ("리사이클러뷰 정보", "pos : $pos")
                    mListener?.onMenuClick(menu_btn, pos, item)
                }
            }

            like_tv.setOnClickListener {

                val pos = adapterPosition

                if(pos != RecyclerView.NO_POSITION){
                    //리스너 객체의 메서드 호출
                    Log.i ("리사이클러뷰 정보", "pos : $pos")
                    mListener?.onLikeClick(like_tv, pos, item)
                }

            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var view: View
        var context : Context = parent.context
        var inflater : LayoutInflater =
            (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)

        view = inflater
            .inflate(R.layout.review_item, parent, false)

        return ViewHolder(view)

    }

    override fun getItemCount(): Int {

        return itemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var item = itemList[position]

        (holder as ViewHolder).setdata(context, item)

    }




}