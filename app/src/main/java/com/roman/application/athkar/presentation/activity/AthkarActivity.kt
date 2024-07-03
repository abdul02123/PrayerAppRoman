package com.roman.application.athkar.presentation.activity

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
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
import com.roman.application.util.enums.SelectionType
import com.roman.application.util.makeVisible
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
        showProgressDialogue()
        viewModel.getAkhtarData()
    }


    private fun setAdapter(list: ArrayList<Athkar?>?) {
        val adapter = AkhtarAdapter(list)
        mBinding?.recyclerView?.adapter = adapter
        mBinding?.recyclerView?.let { mBinding?.indicatorView?.setupWithViewPager(it) }
        prepareMedia()
    }


    private fun bindObserver() {
        viewModel.result.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    val data = it.result as AkhtarResponse
                    athkars = data.athkars
                    setAdapter(data.athkars)
                    mBinding?.lyPlayerButtons?.makeVisible()
                    mBinding?.imgExport?.makeVisible()
                    hideProgressDialogue()
                }

                is NetworkResult.Error -> {
                    val error = it.errorResponse as ErrorResponse
                    showToast(error.message)
                    hideProgressDialogue()
                }

                is NetworkResult.Loading -> {
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
                stopMedia()
                prepareMedia()
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

        player.addListener(object : Player.Listener{
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                if (isPlaying){
                    mBinding?.imgMedia?.setImageResource(R.drawable.ic_pause)
                }else{
                    mBinding?.imgMedia?.setImageResource(R.drawable.ic_play)
                }
            }
        })
    }

    private fun playMedia(){
        if (!athkars.isNullOrEmpty()){
             player.playWhenReady = true
        }

    }

    private fun prepareMedia(){
        val mediaItem = MediaItem.fromUri(Uri.parse(athkars?.get(position)?.link))
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = false
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
                       .putExtra("link", athkars?.get(position)?.link)
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
                        .putExtra("link", athkars?.get(position)?.link)
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