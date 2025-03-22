package com.ui.home.videoTrainingRentalAgreementTermsAndConditions

//import com.google.android.exoplayer2.Player
//import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
//import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
//import kotlinx.android.synthetic.main.custom_playback_control_minimal.view.*
//import kotlinx.android.synthetic.main.custom_playback_control_minimal.view.*

import android.app.Activity
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import com.data.remote.GsonInterface
import com.data.remote.response.order.GetDeviceTrainingVideoAndDataRs
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowDeviceTrainingVideoBinding
import com.ui.home.HomeActivity
import iCode.android.BaseFragment
import iCode.utility.PrefHelper
import iCode.utility.playerUtil.exoPlayer.PlayerHolder
import iCode.utility.playerUtil.exoPlayer.PlayerState


@Suppress("DEPRECATION")
class DeviceTrainingVideoFragment : BaseFragment() {

    private val binding: FragMowDeviceTrainingVideoBinding by bindingLazy()

    override val layoutResId = R.layout.frag_mow_device_training_video

    private lateinit var homeActivity: HomeActivity

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var exoPlayer: ExoPlayer

    var isPlaying: Boolean = true
    //private val mediaSession: MediaSessionCompat by lazy { createMediaSession() }
//    private lateinit var mediaSessionConnector: MediaSessionConnector
    private lateinit var mediaSessionConnector: MediaSession

    /*private val mediaSessionConnector: MediaSessionConnector by lazy {
        createMediaSessionConnector()
    }*/

    private lateinit var playerState: PlayerState

    //private val playerState by lazy { PlayerState() }
    private lateinit var playerHolder: PlayerHolder

    private lateinit var mediaCatalog: List<MediaDescriptionCompat>
    private lateinit var videoURL: String
    private var videoText = ""

    var agreeCallback: ((ActionButtonStatus) -> Unit)? = null

    private var currentPosition = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeActivity = activity as HomeActivity
        isPlaying = true;

    }


    @OptIn(UnstableApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("Nilam","onViewCreated DeviceTrainingVideoFragment-->")

        arguments?.getInt("currentIndex")?.let { currentIndex ->

            PrefHelper.getInstance().getString("termsAndConditionData", null)?.let {
                val responseObj = GsonInterface.getInstance().gson.fromJson(
                    it,
                    GetDeviceTrainingVideoAndDataRs::class.java
                )
                val deviceTrainingVideoData = responseObj.deviceTrainingVideoDataList[currentIndex]
                videoURL = deviceTrainingVideoData.trainingVideoURL
                videoText = deviceTrainingVideoData.traningVideoText

            }
        }


        binding.webViewContent.loadDataWithBaseURL(
            null,
            ("<style>img{display: inline;height: auto;max-width: 100%;}</style>" + videoText),
            "text/html",
            "utf-8",
            null
        )
        /*binding.webViewContent.loadData(
            Base64.encodeToString(
                ("<style>img{display: inline;height: auto;max-width: 100%;}</style>" + videoText).toByteArray(),
                Base64.NO_PADDING
            ), "text/html", "base64"
        )*/
        // binding.textViewInfo.showHtmlText(videoText)

        if (videoURL.isNotEmpty()) {
            Log.e("Nilam","url is -> "+videoURL.toString())

            binding.textViewNoVideo.visibility = View.GONE
            binding.exoplayerView.visibility = View.VISIBLE
            mediaCatalog = listOf(
                with(MediaDescriptionCompat.Builder()) {
                    setDescription("Device Training Video")
                    setMediaId("1")
                    setMediaUri(Uri.parse(videoURL))
                    setTitle("Device Training Video")
                    setSubtitle("Device Training Video")
                    build()
                }
            )

            mediaSession = createMediaSession()
//            mediaSessionConnector = createMediaSessionConnector()
            // Initialize ExoPlayer
            exoPlayer = ExoPlayer.Builder(homeActivity).build()
            mediaSessionConnector = MediaSession.Builder(homeActivity, exoPlayer)
                .setId("unique_media_session_id_" + System.currentTimeMillis()) // Use a unique ID
                .build()
            playerState = PlayerState(position = currentPosition)
            homeActivity.volumeControlStream = AudioManager.STREAM_MUSIC
            createMediaSession()
            createPlayer()
        }
        else {
            Log.e("Nilam","url is -> else part ")

            binding.textViewNoVideo.visibility = View.VISIBLE
            binding.exoplayerView.visibility = View.GONE
        }


        binding.texViewCancel.setOnClickListener {
            agreeCallback?.invoke(ActionButtonStatus.CANCEL)
        }
        binding.texViewAgree.setOnClickListener {
            Log.e("Nilam","agree page clicked--> device training video agree button clicked")


            agreeCallback?.invoke(ActionButtonStatus.AGREE)
            if (videoURL.isNotEmpty()) {
                //playerHolder.stop()
                binding.exoplayerView.findViewById<ImageView>(R.id.exo_pause).performClick()

                //playerHolder.
            }

        }

        //binding1.exoFullscreenIcon.setOnClickListener {
        binding.exoplayerView.findViewById<ImageView>(R.id.exo_pause).setOnClickListener {
        //binding.exoplayerView.setOnClickListener {
            if (videoURL.isNotEmpty()) {

                if (playerHolder.audioFocusPlayer.isPlaying) {
                    isPlaying = !isPlaying;
                  //  exoPlayer.pause()
                 //   playerHolder.stop()
                    playerHolder.audioFocusPlayer.pause() // Pause the player
                    binding.exoplayerView.findViewById<ImageView>(R.id.exo_pause).setImageResource(
                        androidx.media3.session.R.drawable.media3_icon_play)  // Change to play icon
                    Log.e("Nilam","Hello-----if portion pause button clicked ---")
                  //  binding.exoplayerView.onPause();
                  //  playPauseButton.setImageResource(R.drawable.exo_icon_play)  // Change to play icon
                } else if (playerHolder.audioFocusPlayer.isLoading) {
                  if(isPlaying){
                      binding.exoplayerView.findViewById<ImageView>(R.id.exo_pause).setImageResource(
                          androidx.media3.session.R.drawable.media3_icon_play)  // Change to play icon
                      playerHolder.audioFocusPlayer.pause() // Pause the player
                  }else{
                      binding.exoplayerView.findViewById<ImageView>(R.id.exo_pause).setImageResource(
                          androidx.media3.session.R.drawable.media3_icon_pause)  // Change to play icon
                      playerHolder.audioFocusPlayer.play() // Pause the player

                  }
                    isPlaying = !isPlaying;

                    //  playPauseButton.setImageResource(R.drawable.exo_icon_play)  // Change to play icon
                } else {
                    isPlaying = !isPlaying;

                    Log.e("Nilam","Hello-----else portion pause button clicked ---")
                    binding.exoplayerView.findViewById<ImageView>(R.id.exo_pause).setImageResource(
                        androidx.media3.session.R.drawable.media3_icon_pause)  // Change to play icon
                    playerHolder.audioFocusPlayer.play() // Pause the player

                  //  playPauseButton.setImageResource(R.drawable.exo_icon_pause)  // Change to pause icon
                }
               // binding1.exoPause.performClick()
               // binding.exoplayerView.findViewById<ImageButton>(R.id.exo_pause).performClick()


            }

        }

        binding.exoplayerView.findViewById<ImageView>(R.id.exo_fullscreen_icon).setOnClickListener {
            //binding.exoplayerView.setOnClickListener {
            if (videoURL.isNotEmpty()) {

                // binding1.exoPause.performClick()
              //  binding.exoplayerView.findViewById<ImageButton>(R.id.exo_pause).performClick()
                playerHolder.audioFocusPlayer.pause() // Pause the player
                binding.exoplayerView.findViewById<ImageView>(R.id.exo_pause).setImageResource(
                    androidx.media3.session.R.drawable.media3_icon_play)  // Change to play icon

                // binding.exoplayerView.onPause();

            }

            var currentPosition = 0L
            binding.exoplayerView.player?.currentPosition?.let {
                currentPosition = it
            }
            FullScreenVideoActivity.start(
                this@DeviceTrainingVideoFragment,
                videoURL,
                currentPosition
            )
        }

//        binding.exoplayerView.exo_fullscreen_icon.setOnClickListener {
//            if (videoURL.isNotEmpty()) {
//                binding.exoplayerView.exo_pause.performClick()
//            }
//
//            var currentPosition = 0L
//            binding.exoplayerView.player?.currentPosition?.let {
//                currentPosition = it
//            }
//            FullScreenVideoActivity.start(
//                this@DeviceTrainingVideoFragment,
//                videoURL,
//                currentPosition
//            )
//        }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            data?.let {
                currentPosition = it.getLongExtra("currentPosition", 0L)
            }
        }
    }

    @UnstableApi
    override fun onStart() {
        super.onStart()
        if (videoURL.isNotEmpty()) {
            startPlayer()
            activateMediaSession()
        }

    }

    override fun onStop() {
        super.onStop()
        if (videoURL.isNotEmpty()) {
            stopPlayer()
            deactivateMediaSession()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (videoURL.isNotEmpty()) {
            releasePlayer()
            releaseMediaSession()
        }

    }

    // MediaSession related functions.
    private fun createMediaSession(): MediaSessionCompat =
        MediaSessionCompat(homeActivity, homeActivity.packageName)


    /*
            private fun createMediaSessionConnector(): MediaSessionConnector =
            MediaSessionConnector(mediaSession).apply {
                // If QueueNavigator isn't set, then mediaSessionConnector will not handle following
                // MediaSession actions (and they won't show up in the minimized PIP activity):
                // [ACTION_SKIP_PREVIOUS], [ACTION_SKIP_NEXT], [ACTION_SKIP_TO_QUEUE_ITEM]
                setQueueNavigator(object : TimelineQueueNavigator(mediaSession) {
    //                override fun getMediaDescription(windowIndex: Int): MediaDescriptionCompat {
    //                    return mediaCatalog[windowIndex]
    //                }

                    override fun getMediaDescription(
                        player: Player,
                        windowIndex: Int,
                    ): MediaDescriptionCompat {
                        return mediaCatalog[windowIndex]
                    }
                })
            }*/

//    private fun createMediaSessionConnector(): MediaSessionConnector =
//        MediaSessionConnector(mediaSession).apply {
//            // If QueueNavigator isn't set, then mediaSessionConnector will not handle following
//            // MediaSession actions (and they won't show up in the minimized PIP activity):
//            // [ACTION_SKIP_PREVIOUS], [ACTION_SKIP_NEXT], [ACTION_SKIP_TO_QUEUE_ITEM]
//            setQueueNavigator(object : TimelineQueueNavigator(mediaSession) {
////                override fun getMediaDescription(windowIndex: Int): MediaDescriptionCompat {
////                    return mediaCatalog[windowIndex]
////                }
//
//                override fun getMediaDescription(
//                    player: Player,
//                    windowIndex: Int
//                ): MediaDescriptionCompat {
//                    return mediaCatalog[windowIndex]
//                }
//            })
//        }






    private fun activateMediaSession() {
        if(videoURL.isNotEmpty()) {
            mediaSessionConnector.setPlayer(playerHolder.audioFocusPlayer)
            mediaSession.isActive = true
        }

    }

    private fun deactivateMediaSession() {
        if(videoURL.isNotEmpty()) {
            //   mediaSessionConnector.setPlayer(null, null)
            // mediaSession.isActive = false

            mediaSessionConnector.setPlayer(playerHolder.audioFocusPlayer)
            mediaSession.isActive = false
        }

    }

    private fun releaseMediaSession() {
        if(videoURL.isNotEmpty()) {
            mediaSession.release()
        }
    }


    private fun createPlayer() {
        if (videoURL.isNotEmpty()) {
            playerHolder =
                PlayerHolder(homeActivity, playerState, binding.exoplayerView, mediaCatalog)
        }


    }


    @OptIn(UnstableApi::class)
    private fun startPlayer() {

        if(videoURL.isNotEmpty()) {
            playerHolder.start()
        }
    }

    private fun stopPlayer() {
        if(videoURL.isNotEmpty()) {
            playerHolder.stop()
        }
    }

    private fun releasePlayer() {
        if(videoURL.isNotEmpty()) {
            playerHolder.release()
        }
    }

    companion object {

        fun newInstance(currentIndex: Int): DeviceTrainingVideoFragment {

            val bundle = Bundle()
            bundle.putInt("currentIndex", currentIndex)
            val deviceTrainingVideoFragment = DeviceTrainingVideoFragment()
            deviceTrainingVideoFragment.arguments = bundle
            return deviceTrainingVideoFragment
        }

    }
}