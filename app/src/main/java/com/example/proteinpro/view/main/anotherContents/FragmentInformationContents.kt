package com.example.proteinpro.view.main.anotherContents
import android.content.Intent

import YoutubeItem
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proteinpro.R
import com.example.proteinpro.databinding.FragmentInformationContentsBinding
import com.example.proteinpro.util.Class.ViewType
import com.example.proteinpro.util.PreferenceHelper
import com.example.proteinpro.util.RecyclerView.FoodItem
import com.example.proteinpro.util.RecyclerView.YoutubeAdapter
import com.example.proteinpro.util.Retrofit.ContentsRetrofitHelper
import com.example.proteinpro.util.Retrofit.FoodRetrofitHelper
import com.example.proteinpro.util.Retrofit.RetrofitHelper
import com.example.proteinpro.view.main.MainActivity
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject


class FragmentInformationContents : Fragment() {

    //데이터
    private var itemList : ArrayList<YoutubeItem> = ArrayList()

    //컨텍스트 변수
    private lateinit var mainActivity: MainActivity
    // 변수 입력
    private lateinit var binding: FragmentInformationContentsBinding
    // 뷰 객체


    // 유틸 클래스

    private lateinit var preferenceHelper: PreferenceHelper
    private lateinit var contentsRetrofitHelper: ContentsRetrofitHelper

    // 리사이클러뷰

    private lateinit var adapter: YoutubeAdapter


    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity// 컨텍스트 받아오기

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInformationContentsBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUtils()
        initData()
        initViews()
        initListener()

    }

    // 리스너 선언
    private fun initListener() {

        val rv_Listener = object : YoutubeAdapter.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int, item: YoutubeItem) {

                val mIntent = Intent(mainActivity, ActivityProteinInformation::class.java)

                mIntent.putExtra("youtubeItem", item)

                startActivity(mIntent)

            }

        }

        adapter.setOnItemClickListener(rv_Listener)

    }

    // 뷰 객체 할당
    private fun initViews() {

        binding.contentsRV.layoutManager = LinearLayoutManager(mainActivity, LinearLayoutManager.VERTICAL, false)
        binding.contentsRV.adapter = adapter
    }

    // 현재 프래그먼트에서 필요한 데이터 받아오기
    private fun initData() {

        contentsRetrofitHelper.getYoutubeList(object: ContentsRetrofitHelper.YoutubeListCallback {
            override fun onSuccess(youtubelist: JsonArray) {

                //Json Array -> ArrayList
                val gson = Gson()

                for(item in youtubelist){
                    Log.i ("데이터 파싱중", ""+item)
                    val item = item.asJsonObject

                    val youtubeItem = gson.fromJson(item, YoutubeItem::class.java)

                    itemList.add(youtubeItem)

                }

                adapter.setItem_list(itemList)

            }

            override fun onFailure() {

                Log.i ("getYoutubeList", "onFailure")

            }
        }
        )

    }
    // 유틸 클래스 할당
    private fun initUtils() {

        preferenceHelper = PreferenceHelper(mainActivity)
        contentsRetrofitHelper = ContentsRetrofitHelper(mainActivity)

        adapter = YoutubeAdapter(mainActivity, itemList)

    }

    private fun getStringFromJson(jsonObject: JsonObject, key: String): String {
        return if (jsonObject.has(key) && !jsonObject.get(key).isJsonNull) {
            jsonObject.get(key).asString
        } else {
            ""
        }
    }

    private fun getIntFromJson(jsonObject: JsonObject, key: String): Int {
        return if (jsonObject.has(key) && !jsonObject.get(key).isJsonNull) {
            jsonObject.get(key).asInt
        } else {
            0
        }
    }

    private fun getFloatFromJson(jsonObject: JsonObject, key: String): Float {
        return if (jsonObject.has(key) && !jsonObject.get(key).isJsonNull) {
            jsonObject.get(key).asFloat
        } else {
            0.0f
        }
    }
}