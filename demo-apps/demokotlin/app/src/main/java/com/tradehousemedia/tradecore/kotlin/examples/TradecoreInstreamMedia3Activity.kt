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
import com.tradehousemedia.tradecore.ima.TradecoreInstreamVideo
import com.tradehousemedia.tradecore.kotlin.R
import com.tradehousemedia.tradecore.kotlin.utils.BaseExampleActivity

class TradecoreInstreamMedia3Activity : BaseExampleActivity() {

    private var savedPosition = 0

    private lateinit var videoPlayer: VideoView
    private lateinit var mediaController: MediaController
    private var instreamVideo: TradecoreInstreamVideo? = null

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

        instreamVideo = TradecoreInstreamVideo("tradecore-zone-3494", this)
        instreamVideo?.setVideoPlayer(videoPlayer, container)
        instreamVideo?.setAdListener(createAdListener())
        instreamVideo?.loadAd()

        playButton.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        instreamVideo?.destroy()
    }

    private fun createAdListener(): TradecoreInstreamAdListener {
        return object : TradecoreInstreamAdListener {
            override fun onAdLoaded(ad: TradecoreAd) {
                // Ad loaded
                Log.d("ExampleActivity", "Ad loaded for zone " + ad.zoneId + " with targeting: " + ad.targeting)
                instreamVideo?.show()
            }

            override fun onAdFailed(error: TradecoreError) {
                // Ad failed to load
            }

            override fun shouldPauseContent() {
                // Ad is going to start, the content player should be paused
                savedPosition = videoPlayer.currentPosition
                videoPlayer.stopPlayback()
                videoPlayer.setMediaController(null)
            }

            override fun shouldResumeContent() {
                // Ad is completed, the content player should be resumed
                videoPlayer.setVideoPath(SAMPLE_VIDEO_URL)
                videoPlayer.setMediaController(mediaController)
                videoPlayer.setOnPreparedListener { player: MediaPlayer ->
                    if (savedPosition > 0) {
                        player.seekTo(savedPosition)
                    }
                    player.start()
                }
            }

            override fun onAdFinished(ad: TradecoreAd) {
                // Ad finished playing
            }

            override fun onAdStarted(ad: TradecoreAd) {
                // Ad started playing
            }

            override fun onAdClicked(ad: TradecoreAd) {
                // Ad clicked
            }
        }
    }

    companion object {
        private const val SAMPLE_VIDEO_URL = "https://storage.googleapis.com/gvabox/media/samples/stock.mp4"
    }
}