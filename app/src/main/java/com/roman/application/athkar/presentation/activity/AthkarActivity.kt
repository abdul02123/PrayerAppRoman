package com.roman.application.athkar.presentation.activity

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.roman.application.R
import com.roman.application.athkar.domain.model.AkhtarResponse
import com.roman.application.athkar.domain.model.Athkar
import com.roman.application.athkar.presentation.adapter.AkhtarAdapter
import com.roman.application.athkar.presentation.dialogue.ExportDialogue
import com.roman.application.athkar.presentation.viewmodel.AkhtarViewModel
import com.roman.application.base.BaseCompatVBActivity
import com.roman.application.databinding.ActivityAthkarBinding
import com.roman.application.util.SelectionType
import com.roman.application.util.network.ErrorResponse
import com.roman.application.util.network.NetworkResult
import com.roman.application.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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

        mBinding?.imgExport?.setOnClickListener {
            showExportDialogue()
        }


        mBinding?.recyclerView?.registerOnPageChangeCallback(object : OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                this@AthkarActivity.position = position
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
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

    private fun showExportDialogue() {
        ExportDialogue.getInstance(message = "", onClick = {
            when(it){
                SelectionType.IMAGE.indentifier ->{
                    startActivity( Intent(this@AthkarActivity, ExportActivity::class.java)
                       .putExtra("export", athkars?.get(position)?.text)
                       .putExtra("isVideo", false))

                  /*  CoroutineScope(Dispatchers.IO).launch {

                        *//*val bitmap: Bitmap = event.picture.createBitmapFromPicture()
                        try {
                            bitmap.saveToDisk(event.context)
                            _uiEvent.emit(ExportScreenUiEvent.ShowMessage(resourceProvider.getString(R.string.image_saved)))
                        } catch (e: Exception) {
                            _uiEvent.emit(ExportScreenUiEvent.ShowMessage(resourceProvider.getString(R.string.failed_to_save)))
                        }*//*
                    }*/

                }
                SelectionType.VIDEO.indentifier ->{
                    startActivity( Intent(this@AthkarActivity, ExportActivity::class.java)
                        .putExtra("export", athkars?.get(position)?.text)
                        .putExtra("isVideo", true))
                }
            }

        }).show(supportFragmentManager, null)
    }



    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}