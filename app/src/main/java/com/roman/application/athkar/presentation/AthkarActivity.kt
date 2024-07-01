package com.roman.application.athkar.presentation

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.roman.application.R
import com.roman.application.athkar.AkhtarAdapter
import com.roman.application.athkar.domain.model.AkhtarResponse
import com.roman.application.athkar.domain.model.Athkar
import com.roman.application.base.BaseCompatVBActivity
import com.roman.application.databinding.ActivityAthkarBinding
import com.roman.application.util.network.ErrorResponse
import com.roman.application.util.network.NetworkResult
import com.roman.application.util.showToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AthkarActivity : BaseCompatVBActivity<ActivityAthkarBinding>() {

    private val viewModel: AkhtarViewModel by viewModels()
    private var athkars: ArrayList<Athkar?>? = null
    private var position: Int = 0
    private val player: ExoPlayer by lazy {
        ExoPlayer.Builder(applicationContext).build()
    }

    override fun setUpViewBinding(layoutInflater: LayoutInflater): ActivityAthkarBinding {
        return ActivityAthkarBinding.inflate(layoutInflater)
    }

    override fun init() {
        addListener()
        bindObserver()
        viewModel.getAkhtarData()
    }


    private fun setAdapter(list: ArrayList<Athkar?>?) {
        val adapter = AkhtarAdapter(list)
        mBinding?.recyclerView?.adapter = adapter
        mBinding?.recyclerView?.let { mBinding?.indicatorView?.setupWithViewPager(it) }
    }


    private fun bindObserver() {
        viewModel.result.observe(this) {
//            mBinding?.progressBar?.makeGone()
            when (it) {
                is NetworkResult.Success -> {
                    val data = it.result as AkhtarResponse
                    athkars = data.athkars
                    setAdapter(data.athkars)
                }

                is NetworkResult.Error -> {
                    val error = it.errorResponse as ErrorResponse
                    showToast(error.message)
                }

                is NetworkResult.Loading -> {
//                mBinding?.progressBar?.makeVisible()
                }
            }
        }
    }

    private fun addListener() {

        mBinding?.appCompatImageView2?.setOnClickListener {
            finish()
        }
        mBinding?.tvBack?.setOnClickListener {
            mBinding?.recyclerView?.currentItem = mBinding?.recyclerView?.currentItem!! - 1
        }
        mBinding?.tvNext?.setOnClickListener {
            mBinding?.recyclerView?.currentItem = mBinding?.recyclerView?.currentItem!! + 1
        }


        mBinding?.recyclerView?.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                this@AthkarActivity.position = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                stopMedia()
            }
        })
        mBinding?.imgMedia?.setOnClickListener {
            if (player.isPlaying){
                mBinding?.imgMedia?.setImageResource(R.drawable.ic_play)
                player.pause()
            }else{
                mBinding?.imgMedia?.setImageResource(R.drawable.ic_pause)
                playMedia()
            }
        }
    }

    private fun playMedia(){
        if (!athkars.isNullOrEmpty()){
             val mediaItem = MediaItem.fromUri(Uri.parse(athkars?.get(position)?.link))
             player.setMediaItem(mediaItem)
             player.prepare()
             player.play()
        }

    }

    private fun stopMedia(){
        mBinding?.imgMedia?.setImageResource(R.drawable.ic_play)
        player.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}