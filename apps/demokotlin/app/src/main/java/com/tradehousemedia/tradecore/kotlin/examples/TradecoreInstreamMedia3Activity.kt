package com.tradehousemedia.tradecore.kotlin.examples

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import com.tradehousemedia.tradecore.core.TradecoreAd
import com.tradehousemedia.tradecore.core.TradecoreError
import com.tradehousemedia.tradecore.core.listeners.TradecoreInstreamAdListener
import com.tradehousemedia.tradecore.ima.TradecoreInstreamAdUnit
import com.tradehousemedia.tradecore.kotlin.R
import com.tradehousemedia.tradecore.kotlin.utils.BaseExampleActivity

class TradecoreInstreamMedia3Activity : BaseExampleActivity() {

    private var savedPosition = 0

    private lateinit var videoPlayer: VideoView
    private lateinit var mediaController: MediaController
    private var tradecoreInstreamAdUnit: TradecoreInstreamAdUnit? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instream)

        videoPlayer = findViewById(R.id.videoView)
        mediaController = MediaController(this)
        mediaController.setAnchorView(videoPlayer)
        videoPlayer.setMediaController(mediaController)

        findViewById<View>(R.id.playButton).setOnClickListener { playButton: View ->
            this.loadAd(playButton)
        }
    }

    private fun loadAd(playButton: View) {
        val container = findViewById<ViewGroup>(R.id.videoPlayerContainer)

        tradecoreInstreamAdUnit = TradecoreInstreamAdUnit("tradecore-zone-3027", this)
        tradecoreInstreamAdUnit?.setVideoPlayer(videoPlayer, container)
        tradecoreInstreamAdUnit?.setAdListener(createAdListener())
        tradecoreInstreamAdUnit?.loadAd()

        playButton.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        tradecoreInstreamAdUnit?.destroy()
    }

    private fun createAdListener(): TradecoreInstreamAdListener {
        return object : TradecoreInstreamAdListener {
            override fun onAdLoaded(ad: TradecoreAd) {
                Log.d("ExampleActivity", "Ad loaded for zone " + ad.zoneId + " with targeting: " + ad.targeting)
                tradecoreInstreamAdUnit?.show()
            }

            override fun shouldPauseContent() {
                savedPosition = videoPlayer.currentPosition
                videoPlayer.stopPlayback()
                videoPlayer.setMediaController(null)
            }

            override fun shouldResumeContent() {
                videoPlayer.setVideoPath(SAMPLE_VIDEO_URL)
                videoPlayer.setMediaController(mediaController)
                videoPlayer.setOnPreparedListener { player: MediaPlayer ->
                    if (savedPosition > 0) {
                        player.seekTo(savedPosition)
                    }
                    player.start()
                }
            }

            override fun onAdFailed(error: TradecoreError) {
            }
        }
    }

    companion object {
        private const val SAMPLE_VIDEO_URL = "https://storage.googleapis.com/gvabox/media/samples/stock.mp4"
    }
}