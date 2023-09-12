package com.example.proteinpro.view.main.anotherContents

import YoutubeItem
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.proteinpro.R
import com.example.proteinpro.databinding.ActivityProteinInformationBinding
import com.example.proteinpro.util.PreferenceHelper
import com.example.proteinpro.util.Retrofit.ContentsRetrofitHelper
import com.example.proteinpro.util.Retrofit.RetrofitHelper
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener


class ActivityProteinInformation : AppCompatActivity() {

    // 전역 변수로 바인딩 객체 선언
    private var mBinding:
            ActivityProteinInformationBinding? =null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!
    //!!는 Kotlin에서 Nullable 타입을 강제로 Non-nullable 타입으로 변환하는 것을 의미

    // 유틸 클래스
    // 유틸 함수 선언
    private lateinit var preferenceHelper: PreferenceHelper
    private lateinit var contentsRetrofitHelper: ContentsRetrofitHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_protein_information)

        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용해서
        // 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivityProteinInformationBinding.inflate(layoutInflater)
        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의
        // 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시 합니다.
        setContentView(binding.root)// < 기존의 setContentView 는 주석 처리해 주세요!

        lifecycle.addObserver(binding.youtubePlayerView)

        val Item = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("youtubeItem",YoutubeItem::class.java)
        } else {
            intent.getSerializableExtra("youtubeItem") as YoutubeItem
        }


        initUtils()

        if (Item != null) {
            contentsRetrofitHelper.getBoard(Item.키.toInt(), object: ContentsRetrofitHelper.ItemCallback{
                override fun onSuccess(youtubeItme: YoutubeItem) {

                    initdata(youtubeItme)

                }

                override fun onFailure() {

                }


            })
        }


        initViews()
        initListener()

    }

    private fun initdata(item: YoutubeItem?){

        if (item != null) {

        binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
            override fun onReady(youTubePlayer: YouTubePlayer) {
                var videoId = ""
                   videoId = extractVideoIdFromUrl(item.링크).toString()

                youTubePlayer.loadVideo(videoId, 0f)
            }
        })

        binding.backBtn.setOnClickListener { onBackPressed() }

            binding.youtubeTitleTv.setText(item.제목)
            binding.descTV.setText(item.내용)
        }

    }

    private fun initViews(){
        // 뷰 초기화



    }
    private fun initListener(){
        // 리스너 초기화
    }
    private fun initUtils(){
        // 유틸 클래스 초기화
        preferenceHelper = PreferenceHelper(this)
        contentsRetrofitHelper =ContentsRetrofitHelper(this)


    }

    fun extractVideoIdFromUrl(url: String): String? {
        val pattern = "v=([A-Za-z0-9_-]+)".toRegex()
        val matchResult = pattern.find(url)
        return matchResult?.groupValues?.get(1)
    }


}