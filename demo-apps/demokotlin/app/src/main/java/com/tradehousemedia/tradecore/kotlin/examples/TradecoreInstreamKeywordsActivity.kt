package com.tradehousemedia.tradecore.kotlin.examples

import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.google.ads.interactivemedia.v3.api.*
import com.google.ads.interactivemedia.v3.api.AdErrorEvent.AdErrorListener
import com.google.ads.interactivemedia.v3.api.AdEvent.AdEventType
import com.google.ads.interactivemedia.v3.api.AdsLoader.AdsLoadedListener
import com.google.ads.interactivemedia.v3.api.player.AdMediaInfo
import com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer
import com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer.VideoAdPlayerCallback
import com.google.ads.interactivemedia.v3.api.player.VideoProgressUpdate
import com.tradehousemedia.tradecore.core.TradecoreAd
import com.tradehousemedia.tradecore.core.TradecoreError
import com.tradehousemedia.tradecore.core.listeners.TradecoreInstreamAdListener
import com.tradehousemedia.tradecore.core.utils.TradecoreImaUtils
import com.tradehousemedia.tradecore.ima.TradecoreInstreamVideo
import com.tradehousemedia.tradecore.kotlin.R
import java.util.*

/**
 * Default example with IMA SDK and Tradecore.
 * [Official guide](https://developers.google.com/interactive-media-ads/docs/sdks/android/client-side/get-started)
 */
class TradecoreInstreamKeywordsActivity : AppCompatActivity() {

    private var savedPosition = 0

    private lateinit var sdkFactory: ImaSdkFactory
    private lateinit var adsLoader: AdsLoader
    private lateinit var adsManager: AdsManager
    private lateinit var videoPlayer: VideoView
    private lateinit var mediaController: MediaController
    private lateinit var videoAdPlayerAdapter: GamAdapter
    private lateinit var imaSdkSettings: ImaSdkSettings

    private var instreamVideo: TradecoreInstreamVideo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instream)

        imaSdkSettings = ImaSdkFactory.getInstance().createImaSdkSettings()

        sdkFactory = ImaSdkFactory.getInstance()
        sdkFactory.initialize(this, imaSdkSettings)

        mediaController = MediaController(this)
        videoPlayer = findViewById(R.id.videoView)
        mediaController.setAnchorView(videoPlayer)
        videoPlayer.setMediaController(mediaController)

        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager?
        videoAdPlayerAdapter = GamAdapter(videoPlayer, audioManager)

        val adDisplayContainer = ImaSdkFactory.createAdDisplayContainer(
            findViewById(R.id.videoPlayerContainer),
            videoAdPlayerAdapter
        )
        adsLoader = sdkFactory.createAdsLoader(this, imaSdkSettings, adDisplayContainer)
        adsLoader.addAdErrorListener(createAdErrorListener())
        adsLoader.addAdsLoadedListener(createAdLoadedListener())

        val playButton = findViewById<View>(R.id.playButton)
        playButton.setOnClickListener { view: View ->
            videoPlayer.setVideoPath(SAMPLE_VIDEO_URL)
            requestAds()
            view.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        instreamVideo?.destroy()
    }

    private fun createAdErrorListener(): AdErrorListener {
        return AdErrorListener { adErrorEvent: AdErrorEvent? ->
            Log.i(
                LOGTAG,
                "Ad Error: " + (adErrorEvent?.error?.message ?: "Unknown error")
            )
            resumeContent()
        }
    }

    private fun createAdLoadedListener(): AdsLoadedListener {
        return AdsLoadedListener { adsManagerLoadedEvent: AdsManagerLoadedEvent ->
            adsManager = adsManagerLoadedEvent.adsManager
            adsManager.addAdErrorListener { adErrorEvent: AdErrorEvent ->
                Log.e(
                    LOGTAG,
                    "Ad Error: " + (adErrorEvent.error.message)
                )
                val universalAdIds =
                    adsManager.getCurrentAd().getUniversalAdIds()
                        .contentToString()
                Log.i(LOGTAG, "Discarding the current ad break with universal ad Ids: " + universalAdIds)
                adsManager.discardAdBreak()
            }
            adsManager.addAdEventListener { adEvent: AdEvent ->
                if (adEvent.getType() != AdEventType.AD_PROGRESS) {
                    Log.i(LOGTAG, "Event: " + adEvent.getType())
                }
                when (adEvent.getType()) {
                    AdEventType.LOADED -> adsManager.start()
                    AdEventType.CONTENT_PAUSE_REQUESTED -> pauseContentForAds()
                    AdEventType.CONTENT_RESUME_REQUESTED -> resumeContent()
                    AdEventType.ALL_ADS_COMPLETED -> adsManager.destroy()
                    AdEventType.CLICKED -> {}
                    else -> {}
                }
            }
            adsManager.init(ImaSdkFactory.getInstance().createAdsRenderingSettings())
        }
    }

    private fun pauseContentForAds() {
        Log.i(LOGTAG, "pauseContentForAds")
        savedPosition = videoPlayer.getCurrentPosition()
        videoPlayer.stopPlayback()
        videoPlayer.setMediaController(null)
    }

    private fun resumeContent() {
        Log.i(LOGTAG, "resumeContent")
        videoPlayer.setVideoPath(SAMPLE_VIDEO_URL)
        videoPlayer.setMediaController(mediaController)
        videoPlayer.setOnPreparedListener { mediaPlayer: MediaPlayer ->
            if (savedPosition > 0) {
                mediaPlayer.seekTo(savedPosition)
            }
            mediaPlayer.start()
        }
        videoPlayer.setOnCompletionListener { mediaPlayer: MediaPlayer? ->
            videoAdPlayerAdapter.notifyImaOnContentCompleted()
        }
    }

    private fun requestAds() {
        val adUnit = TradecoreInstreamVideo("tradecore-zone-3494", this)
        adUnit.setAdListener(createTradecoreListener())
        adUnit.loadAd()

        this.instreamVideo = adUnit
    }

    fun createTradecoreListener(): TradecoreInstreamAdListener {
        return object : TradecoreInstreamAdListener {
            override fun onAdLoaded(tradecoreAd: TradecoreAd) {
                val request = sdkFactory.createAdsRequest()

                val encodedKeywords = TradecoreImaUtils.encodeKeywords(tradecoreAd.targeting)
                val url = SAMPLE_VAST_TAG_URL + "&cust_params=" + encodedKeywords

                request.setAdTagUrl(url)

                request.setContentProgressProvider {
                    if (videoPlayer.getDuration() <= 0) {
                        return@setContentProgressProvider VideoProgressUpdate.VIDEO_TIME_NOT_READY
                    }
                    VideoProgressUpdate(
                        videoPlayer.getCurrentPosition().toLong(),
                        videoPlayer.getDuration().toLong()
                    )
                }
                adsLoader.requestAds(request)
            }

            override fun onAdFailed(tradecoreError: TradecoreError) {
                resumeContent()
            }
        }
    }


    class GamAdapter(private val videoPlayer: VideoView, private val audioManager: AudioManager?) : VideoAdPlayer {
        private val videoAdPlayerCallbacks: MutableList<VideoAdPlayerCallback> = ArrayList<VideoAdPlayerCallback>()
        private var timer: Timer? = null
        private var adDuration = 0

        private var savedAdPosition = 0
        private var loadedAdMediaInfo: AdMediaInfo? = null


        override fun addCallback(videoAdPlayerCallback: VideoAdPlayerCallback) {
            videoAdPlayerCallbacks.add(videoAdPlayerCallback)
        }

        override fun loadAd(adMediaInfo: AdMediaInfo, adPodInfo: AdPodInfo) {
            loadedAdMediaInfo = adMediaInfo
        }

        override fun pauseAd(adMediaInfo: AdMediaInfo) {
            Log.i(LOGTAG, "pauseAd")
            savedAdPosition = videoPlayer.getCurrentPosition()
            stopAdTracking()
        }

        override fun playAd(adMediaInfo: AdMediaInfo) {
            videoPlayer.setVideoURI(adMediaInfo.getUrl().toUri())

            videoPlayer.setOnPreparedListener { mediaPlayer: MediaPlayer ->
                adDuration = mediaPlayer.duration
                if (savedAdPosition > 0) {
                    mediaPlayer.seekTo(savedAdPosition)
                }
                mediaPlayer.start()
                startAdTracking()
            }

            videoPlayer.setOnErrorListener { mediaPlayer: MediaPlayer?, errorType: Int, extra: Int ->
                notifyImaSdkAboutAdError(
                    errorType
                )
            }

            videoPlayer.setOnCompletionListener(OnCompletionListener { mediaPlayer: MediaPlayer? ->
                savedAdPosition = 0
                notifyImaSdkAboutAdEnded()
            })
        }

        override fun release() {
        }

        override fun removeCallback(videoAdPlayerCallback: VideoAdPlayerCallback) {
            videoAdPlayerCallbacks.remove(videoAdPlayerCallback)
        }

        override fun stopAd(adMediaInfo: AdMediaInfo) {
            Log.i(LOGTAG, "stopAd")
            stopAdTracking()
        }

        override fun getVolume(): Int {
            val current = if (audioManager != null) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) else 3
            val max = if (audioManager != null) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) else 10
            return if (max == 0) 1 else (current / max) // чтобы не делить на 0
        }

        private fun startAdTracking() {
            Log.i(LOGTAG, "startAdTracking")
            if (timer != null) {
                return
            }
            timer = Timer()
            val updateTimerTask: TimerTask = object : TimerTask() {
                override fun run() {
                    val progressUpdate = getAdProgress()
                    notifyImaSdkAboutAdProgress(progressUpdate)
                }
            }
            timer?.schedule(updateTimerTask, POLLING_TIME_MS, INITIAL_DELAY_MS)
        }

        private fun notifyImaSdkAboutAdEnded() {
            Log.i(LOGTAG, "notifyImaSdkAboutAdEnded")
            savedAdPosition = 0
            if (loadedAdMediaInfo != null) {
                for (callback in videoAdPlayerCallbacks) {
                    callback.onEnded(loadedAdMediaInfo!!)
                }
            }
        }

        private fun notifyImaSdkAboutAdProgress(adProgress: VideoProgressUpdate) {
            if (loadedAdMediaInfo != null) {
                for (callback in videoAdPlayerCallbacks) {
                    callback.onAdProgress(loadedAdMediaInfo!!, adProgress)
                }
            }
        }

        /**
         * @return boolean
         */
        private fun notifyImaSdkAboutAdError(errorType: Int): Boolean {
            Log.i(LOGTAG, "notifyImaSdkAboutAdError")

            if (errorType == MediaPlayer.MEDIA_ERROR_UNSUPPORTED) {
                Log.e(LOGTAG, "notifyImaSdkAboutAdError: MEDIA_ERROR_UNSUPPORTED")
            } else if (errorType == MediaPlayer.MEDIA_ERROR_TIMED_OUT) {
                Log.e(LOGTAG, "notifyImaSdkAboutAdError: MEDIA_ERROR_TIMED_OUT")
            }
            if (loadedAdMediaInfo != null) {
                for (callback in videoAdPlayerCallbacks) {
                    callback.onError(loadedAdMediaInfo!!)
                }
            }
            return true
        }

        fun notifyImaOnContentCompleted() {
            Log.i(LOGTAG, "notifyImaOnContentCompleted")
            for (callback in videoAdPlayerCallbacks) {
                callback.onContentComplete()
            }
        }

        private fun stopAdTracking() {
            Log.i(LOGTAG, "stopAdTracking")
            if (timer != null) {
                timer?.cancel()
                timer = null
            }
        }

        override fun getAdProgress(): VideoProgressUpdate {
            val adPosition = videoPlayer.getCurrentPosition().toLong()
            return VideoProgressUpdate(adPosition, adDuration.toLong())
        }

        init {
            this.videoPlayer.setOnCompletionListener { mp: MediaPlayer? -> notifyImaOnContentCompleted() }
        }

        companion object {
            private const val LOGTAG = "TradecoreInstreamAdapter"
            private const val POLLING_TIME_MS: Long = 250
            private const val INITIAL_DELAY_MS: Long = 250
        }
    }


    companion object {
        private const val LOGTAG = "InstreamActivity"
        private const val SAMPLE_VIDEO_URL = "https://storage.googleapis.com/gvabox/media/samples/stock.mp4"
        private val SAMPLE_VAST_TAG_URL = ("https://pubads.g.doubleclick.net/gampad/ads?iu=/21775744923/external/"
                + "single_preroll_skippable&sz=640x480&ciu_szs=300x250%2C728x90&gdfp_req=1&output=vast"
                + "&unviewed_position_start=1&env=vp&impl=s&correlator=")
    }
}