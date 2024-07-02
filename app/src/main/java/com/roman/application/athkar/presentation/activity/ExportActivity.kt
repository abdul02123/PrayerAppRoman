package com.roman.application.athkar.presentation.activity

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.roman.application.R
import com.roman.application.athkar.domain.model.Athkar
import com.roman.application.base.BaseCompatVBActivity
import com.roman.application.databinding.ActivityExportBinding

class ExportActivity : BaseCompatVBActivity<ActivityExportBinding>() {

    private var link: String? = null
    private val player: ExoPlayer by lazy {
        ExoPlayer.Builder(applicationContext).build()
    }

    override fun setUpViewBinding(layoutInflater: LayoutInflater): ActivityExportBinding {
        return ActivityExportBinding.inflate(layoutInflater)
    }

    override fun init() {
        if (intent.getSerializableExtra("export") != null) {
            val data = intent.getSerializableExtra("export") as String
            val isVideo = intent.getSerializableExtra("isVideo") as Boolean
            link = intent.getSerializableExtra("link") as String
            mBinding?.tvText?.text = data

            if (isVideo) {
                mBinding?.imgMedia?.visibility = View.VISIBLE
                mBinding?.lyMedia?.visibility = View.VISIBLE
            } else {
                mBinding?.lyMedia?.visibility = View.INVISIBLE
                mBinding?.tvPreview?.visibility = View.INVISIBLE
            }
        }

        mBinding?.appCompatImageView2?.setOnClickListener {
            finish()
        }

        val mediaItem = MediaItem.fromUri(Uri.parse(link))
        player?.setMediaItem(mediaItem)
        player?.prepare()

        mBinding?.lyMedia?.setOnClickListener {
            if (player?.isPlaying == false) {
                playMedia()
            } else {
                pauseMedia()
            }
        }

        player?.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                if (isPlaying) {
                    showPlayView()
                } else {
                    showPauseView()
                }
            }
        })
    }


    private fun playMedia() {
        player?.playWhenReady = true
        showPlayView()
    }

    private fun pauseMedia() {
        player?.pause()
        showPauseView()

    }

    private fun showPlayView() {
        mBinding?.imgMedia?.setImageResource(R.drawable.ic_pause)
        mBinding?.tvPreview?.visibility = View.INVISIBLE
    }

    private fun showPauseView() {
        mBinding?.imgMedia?.setImageResource(R.drawable.ic_play)
        mBinding?.tvPreview?.visibility = View.VISIBLE
    }
}