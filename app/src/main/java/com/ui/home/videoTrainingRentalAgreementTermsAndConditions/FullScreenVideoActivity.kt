package com.ui.home.videoTrainingRentalAgreementTermsAndConditions

import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSession.Builder
import androidx.media3.ui.PlayerView
import com.mow.cash.android.R
import iCode.android.BaseActivity
import iCode.utility.playerUtil.exoPlayer.PlayerHolder
import iCode.utility.playerUtil.exoPlayer.PlayerState


class FullScreenVideoActivity : BaseActivity() {

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSession

    private lateinit var playerState: PlayerState
    private lateinit var playerHolder: PlayerHolder

    private lateinit var mediaCatalog: List<MediaDescriptionCompat>
    private lateinit var videoURL: String

    private lateinit var playerView: PlayerView

    private var currentPosition = 0L
    private lateinit var exoPlayer: ExoPlayer
    var isPlaying: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isPlaying = true;


        /*window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // For Android 11 (API 30) and above
           /* window.insetsController?.let { controller ->
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }*/
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        } else {
            // For Android 10 (API 29) and below
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }



        setContentView(R.layout.act_full_screen_video)

        intent.getStringExtra("url")?.let {
            videoURL = it
        }
        currentPosition = intent.getLongExtra("currentPosition",0L)


        playerView = findViewById(R.id.exoplayerView)

        val imageViewFullScreen = playerView.findViewById<ImageView>(R.id.exo_fullscreen_icon)
        imageViewFullScreen.setImageResource(R.drawable.ic_fullscreen_exit)
        playerView.findViewById<ImageView>(R.id.exo_pause).setOnClickListener {


            if (videoURL.isNotEmpty()) {
                if (playerHolder.audioFocusPlayer.isPlaying) {
                    isPlaying = !isPlaying;
                    //  exoPlayer.pause()
                    //   playerHolder.stop()
                    playerHolder.audioFocusPlayer.pause() // Pause the player
                    playerView.findViewById<ImageView>(R.id.exo_pause).setImageResource(androidx.media3.session.R.drawable.media3_icon_play)  // Change to play icon
                    Log.e("Nilam","Hello-----if portion pause button clicked ---")
                    //  playPauseButton.setImageResource(R.drawable.exo_icon_play)  // Change to play icon
                }else if (playerHolder.audioFocusPlayer.isLoading) {
                    if(isPlaying){
                        playerView.findViewById<ImageView>(R.id.exo_pause).setImageResource(androidx.media3.session.R.drawable.media3_icon_play)  // Change to play icon
                        playerHolder.audioFocusPlayer.pause() // Pause the player
                    }else{
                        playerView.findViewById<ImageView>(R.id.exo_pause).setImageResource(androidx.media3.session.R.drawable.media3_icon_pause)  // Change to play icon
                        playerHolder.audioFocusPlayer.play() // Pause the player

                    }
                    isPlaying = !isPlaying;

                    //  playPauseButton.setImageResource(R.drawable.exo_icon_play)  // Change to play icon
                }  else {
                    isPlaying = !isPlaying;
                    Log.e("Nilam","Hello-----else portion pause button clicked ---")
                    playerView.findViewById<ImageView>(R.id.exo_pause).setImageResource(androidx.media3.session.R.drawable.media3_icon_pause)  // Change to play icon
                    playerHolder.audioFocusPlayer.play() // Pause the player

                    //  playPauseButton.setImageResource(R.drawable.exo_icon_pause)  // Change to pause icon
                }
                // binding1.exoPause.performClick()
                // binding.exoplayerView.findViewById<ImageButton>(R.id.exo_pause).performClick()


            }

        }


        imageViewFullScreen.setOnClickListener {

            val data = Intent()

            playerView.player?.currentPosition?.let {
                data.putExtra("currentPosition", it)
            }

            setResult(RESULT_OK, data)
            finish()
        }


        if(videoURL.isNotEmpty()) {
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
          //  exoPlayer.release()  // Ensure ExoPlayer is released before reinitializing
            exoPlayer = ExoPlayer.Builder(this).build()
            mediaSession.release()  // Release the previous session if it exists
            mediaSessionConnector = Builder(this, exoPlayer)
                .setId("unique_media_session_id_" + System.currentTimeMillis()) // Use a unique ID
                 .build()
            playerState = PlayerState(position = currentPosition)

            volumeControlStream = AudioManager.STREAM_MUSIC
            createMediaSession()
            createPlayer()




        }
    }


    @UnstableApi
    override fun onStart() {
        super.onStart()
        if(videoURL.isNotEmpty()) {
            startPlayer()
            activateMediaSession()
        }
    }

    override fun onStop() {
        super.onStop()
        if(videoURL.isNotEmpty()) {
            stopPlayer()
            deactivateMediaSession()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if(videoURL.isNotEmpty()) {
            releasePlayer()
            releaseMediaSession()
        }

    }

    private fun createMediaSession(): MediaSessionCompat = MediaSessionCompat(this, packageName)




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
        if(videoURL.isNotEmpty()) {
            playerHolder = PlayerHolder(this, playerState, playerView,mediaCatalog)
        }
    }

/*<<<<<<< HEAD
    @OptIn(UnstableApi::class)
=======*/
    @UnstableApi
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
        fun start(context: Fragment, url: String, currentPosition: Long) {
            val intent = Intent(context.context, FullScreenVideoActivity::class.java)
            intent.putExtra("url", url)
            intent.putExtra("currentPosition", currentPosition)
            context.startActivityForResult(intent,111)
        }
    }


}
