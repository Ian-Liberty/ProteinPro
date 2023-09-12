package com.example.proteinpro.util.RecyclerView

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proteinpro.databinding.PhotoItemBinding

class PhotoListAdapter (private val context: Context, private var itemList: ArrayList<Uri>,private val adapterType: String): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var mListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onCancleClick(v: View?, position: Int, uri: Uri)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    inner class ViewHolder(val binding: PhotoItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setdata(context: Context, item: Uri ) {

        }

        fun bind(uri: Uri, type: String) {

            Glide.with(context)
                .load(uri)
                .into(binding.imageView)

            if(type == "list"){
                binding.cancelButton.visibility = View.GONE
            }

           binding.cancelButton.setOnClickListener {

               val pos = adapterPosition

               if(pos != RecyclerView.NO_POSITION){
                   //리스너 객체의 메서드 호출
                   Log.i ("리사이클러뷰 정보", "pos : $pos")
                   mListener?.onCancleClick(itemView, pos, uri)

               }

           }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(PhotoItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun getItemCount(): Int {
       return itemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(itemList[position], adapterType)
    }

    fun setItem_list(item_list: ArrayList<Uri>) {
        itemList = item_list
        notifyDataSetChanged()
    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Uri>() {
            override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean {
                return oldItem == newItem
            }
        }
    }


}