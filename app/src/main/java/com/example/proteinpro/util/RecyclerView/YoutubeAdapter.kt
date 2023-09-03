package com.example.proteinpro.util.RecyclerView

import YoutubeItem
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proteinpro.R
import com.example.proteinpro.util.Retrofit.ServerData

class YoutubeAdapter(private val context: Context , private var itemList: ArrayList<YoutubeItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var mListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int, item: YoutubeItem)

    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {

        mListener = listener

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var view: View
        var context : Context = parent.context
        var inflater : LayoutInflater =
            (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)

        view = inflater
            .inflate(R.layout.youtube_item, parent, false)

        return ViewHolder(view)

    }



    override fun getItemCount(): Int {

        return itemList.size

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var item = itemList[position]

        (holder as ViewHolder).setdata(context, item)

    }

    fun setItem_list(item_list: ArrayList<YoutubeItem>) {
        itemList = item_list
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val thumbnail_iv :ImageView = view.findViewById(R.id.thumbnail_IV)
        val title_tv : TextView = view.findViewById(R.id.title_TV)
        val desc_tv : TextView = view.findViewById(R.id.desc_TV)
        val date_tv : TextView = view.findViewById(R.id.date_tv)

        fun setdata(context: Context, item: YoutubeItem ) {


            //        Glide.with(context).load(ServerData.img_URL_board+item.이미지주소).into(thumbnail_iv)
            // 본서버 적용시 위 코드를 사용할 것
            Glide.with(context).load("https://proteinpro.kr/api/img/board/"+item.이미지주소).into(thumbnail_iv)

            title_tv.text = item.제목
            desc_tv.text = item.설명
            date_tv.text = item.수정일


            // 이미지 설정 등 추가적인 작업이 필요하면 여기에 작성

            itemView.setOnClickListener{
                val pos = adapterPosition

                if(pos != RecyclerView.NO_POSITION){
                    //리스너 객체의 메서드 호출
                    Log.i ("리사이클러뷰 정보", "pos : $pos")
                    mListener?.onItemClick(itemView, pos, item)
                }
            }

        }

    }


}