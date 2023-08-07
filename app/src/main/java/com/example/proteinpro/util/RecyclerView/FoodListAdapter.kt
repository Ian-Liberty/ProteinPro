package com.example.proteinpro.util.RecyclerView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proteinpro.R
import com.example.proteinpro.util.Retrofit.ServerData


class FoodListAdapter(private val context: Context, private var itemList: ArrayList<FoodItem>): RecyclerView.Adapter<FoodListAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.food_item_cardview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodListAdapter.ViewHolder, position: Int) {

        var item = itemList[position]

//        Glide.with(context).load(ServerData.img_URL+item.image).into(holder.food_img_IV)
        // 본서버 적용시 위 코드를 사용할 것
        Glide.with(context).load(ServerData.img_URL+"셀렉스프로틴복숭아.JPG").into(holder.food_img_IV)

        holder.brand_TV.text = item.brand
        holder.name_TV.text = item.name
        holder.data_TV.text = "${item.capacity} ${item.capacityUnit} | ${item.quantity} 개 | ${item.price} 원"
        holder.proteinPerGram_TV.text = "단백질 1g 당 ${item.costPerformance} 원"

        // 이미지 설정 등 추가적인 작업이 필요하면 여기에 작성

    }

    fun setItem_list(item_list: ArrayList<FoodItem>) {
        itemList = item_list
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {

        return itemList.size

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val food_img_IV: ImageView = itemView.findViewById(R.id.food_img_IV)
        val brand_TV: TextView = itemView.findViewById(R.id.brand_TV)
        val name_TV: TextView = itemView.findViewById(R.id.name_TV)
        val data_TV: TextView = itemView.findViewById(R.id.data_TV)
        val proteinPerGram_TV: TextView = itemView.findViewById(R.id.proteinPerGram_TV)

    }
}