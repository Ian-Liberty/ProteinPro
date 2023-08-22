package com.example.proteinpro.util.RecyclerView

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
import com.example.proteinpro.util.Class.ViewType
import com.example.proteinpro.util.Retrofit.ServerData


class FoodListAdapter(private val context: Context, private var itemList: ArrayList<FoodItem>, private val adapterType: Int): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int, item: FoodItem)
        fun onMoreClick(v: View?, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var view: View
        var context : Context = parent.context
        var inflater : LayoutInflater =
            (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)

        if(viewType == ViewType.FOOD_CARDVIEW_TYPE){
            view = inflater
                .inflate(R.layout.food_item_cardview, parent, false)
            return ViewHolder(view)
        }else if(viewType == ViewType.FOOD_LISTVIEW_TYPE){
            view = inflater
                .inflate(R.layout.food_item_listview, parent, false)
            return ViewHolder(view)
        }
        else{
            view = inflater
                .inflate(R.layout.more_button, parent, false)
            return MorebtnViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var item = itemList[position]

        if (holder is ViewHolder) {
            (holder as ViewHolder).setdata(context , item)
        }else if(holder is ListViewHolder) {
            (holder as ListViewHolder).setdata(context , item)
        }
        else{
            (holder as MorebtnViewHolder).setdata(context, adapterType)
        }

    }

    fun setItem_list(item_list: ArrayList<FoodItem>) {
        itemList = item_list
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {

        return itemList.size

    }

    override fun getItemViewType(position: Int): Int {

        return itemList.get(position).viewType

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val food_img_IV: ImageView = itemView.findViewById(R.id.food_img_IV)
        val brand_TV: TextView = itemView.findViewById(R.id.brand_TV)
        val name_TV: TextView = itemView.findViewById(R.id.name_TV)
        val data_TV: TextView = itemView.findViewById(R.id.data_TV)
        val proteinPerGram_TV: TextView = itemView.findViewById(R.id.proteinPerGram_TV)

        init {

        }

        fun setdata(context: Context, item: FoodItem ) {


            //        Glide.with(context).load(ServerData.img_URL+item.image).into(holder.food_img_IV)
            // 본서버 적용시 위 코드를 사용할 것
            Glide.with(context).load(ServerData.img_URL+"셀렉스프로틴복숭아.JPG").into(food_img_IV)

            brand_TV.text = item.brand
            name_TV.text = item.name
            data_TV.text = "${item.capacity} ${item.capacityUnit} | ${item.quantity} 개 | ${item.price} 원"
            proteinPerGram_TV.text = "단백질 1g 당 ${item.costPerformance} 원"

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

    inner class MorebtnViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var adapteType : Int = 0

        val more_imageView: ImageView = itemView.findViewById(R.id.more_imageView)
        init {
            more_imageView.setOnClickListener{view ->
                // 리스너
                val pos = adapterPosition
                Log.i ("버튼 뷰홀더", "클릭 리스너")
                Log.i ("어댑터 종류", "adapterType: " +adapteType)

                if(pos != RecyclerView.NO_POSITION) {
                    // 리스너 객체의 메서드 호출.
                    mListener?.onMoreClick(view, pos)
                }

            }

        }

        fun setdata(context: Context, adapterType: Int){

            adapteType = adapterType

        }

    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val food_img_IV: ImageView = itemView.findViewById(R.id.food_img_IV)
        val brand_TV: TextView = itemView.findViewById(R.id.brand_TV)
        val name_TV: TextView = itemView.findViewById(R.id.name_TV)
        val data_TV: TextView = itemView.findViewById(R.id.data_TV)
        val proteinPerGram_TV: TextView = itemView.findViewById(R.id.proteinPerGram_TV)

        init {

        }

        fun setdata(context: Context, item: FoodItem ) {
            //        Glide.with(context).load(ServerData.img_URL+item.image).into(holder.food_img_IV)
            // 본서버 적용시 위 코드를 사용할 것
            Glide.with(context).load(ServerData.img_URL+"셀렉스프로틴복숭아.JPG").into(food_img_IV)

            brand_TV.text = item.brand
            name_TV.text = item.name
            data_TV.text = "${item.capacity} ${item.capacityUnit} | ${item.quantity} 개 | ${item.price} 원"
            proteinPerGram_TV.text = "단백질 1g 당 ${item.costPerformance} 원"

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