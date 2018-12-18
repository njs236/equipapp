package com.nathan.equipapp.services

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.drm.DrmStore
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.session.MediaSession
import android.net.ConnectivityManager
import android.os.*
import android.provider.MediaStore
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserServiceCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import com.nathan.equipapp.MyApplication
import java.lang.Exception

private const val ACTION_PLAY : String = "com.nathan.equipapp.action.PLAY"
private const val ACTION_STOP : String = "com.nathan.equipapp.action.STOP"
private const val MY_MEDIA_ROOT_ID = "media_root_id"
private const val MY_EMPTY_MEDIA_ROOT_ID = "empty_root_id"
private const val URL_EXTRA = "com.nathan.equipapp.extras.URL"

class MediaPlayerService : MediaBrowserServiceCompat(), MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    override fun onGetRoot(p0: String, p1: Int, p2: Bundle?): BrowserRoot? {
        return if (allowBrowsing(p0, p1)) {
            MediaBrowserServiceCompat.BrowserRoot(MY_MEDIA_ROOT_ID, null)
        } else {
            MediaBrowserServiceCompat.BrowserRoot(MY_EMPTY_MEDIA_ROOT_ID, null)
        }
    }
    private val mBinder = LocalBinder()

    inner class LocalBinder: Binder() {

        fun getService(): MediaPlayerService {
            return this@MediaPlayerService
        }
    }

    fun allowBrowsing(clientPackageName: String, clientUid: Int): Boolean {

        return true
    }

    override fun onLoadChildren(p0: String, p1: Result<List<MediaBrowserCompat.MediaItem>>) {
        if (MY_EMPTY_MEDIA_ROOT_ID == p0) {
            p1.sendResult(null)
        }

        val mediaItems = emptyList<MediaBrowserCompat.MediaItem>()

        // Check if this is the root menu:
        if (MY_MEDIA_ROOT_ID == p0) {
            // Build the MediaItem objects for the top level,
            // and put them in the mediaItems list...
        } else {
            // Examine the passed parentMediaId to see which submenu we're at,
            // and put the children of that menu in the mediaItems list...
        }
        p1.sendResult(mediaItems)
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        Log.d(TAG, "Error: what: $what: extra: $extra")
        return true
    }

    private var url : String? = null
    private lateinit var mMediaSession: MediaSessionCompat
    private lateinit var mStateBuilder: PlaybackStateCompat.Builder
    private val TAG = this.javaClass.simpleName
    private var mediaPlayerReceiver: MyPlayerReceiver? = null

    class MyPlayerReceiver: BroadcastReceiver {
        private var mMediaPlayer: MediaPlayer? = null
        private var mediaPlayerService: MediaPlayerService? = null
        private var TAG = this.javaClass.simpleName

        constructor(mp: MediaPlayer, service: MediaPlayerService) {
            mMediaPlayer = mp
            mediaPlayerService = service

        }

        override fun onReceive(context: Context?, intent: Intent?) {

            val action: String = intent!!.action
            Log.d(TAG, "onReceive: $action")


            when (action) {
                ACTION_PLAY -> {
                    val url: String = intent.getStringExtra(URL_EXTRA)

                    mMediaPlayer?.apply {
                        setOnErrorListener(mediaPlayerService)

                        setOnPreparedListener(mediaPlayerService)
                        setDataSource(url)
                        prepareAsync()
                    }
                }

                ACTION_STOP -> {
                        if (mMediaPlayer!!.isPlaying) {

                                mMediaPlayer?.reset()

                        }
                }
            }
        }
    }


    override fun onCreate() {
        super.onCreate()

        initMediaPlayer()

        mMediaSession = MediaSessionCompat(baseContext,TAG ).apply {
            setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)

            mStateBuilder = PlaybackStateCompat.Builder().setActions(PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PLAY_PAUSE)

            setPlaybackState(mStateBuilder.build())

            setCallback(callback)

            setSessionToken(sessionToken)

        }



        LocalBroadcastManager.getInstance(this).registerReceiver(mediaPlayerReceiver as BroadcastReceiver, IntentFilter("com.nathan.equipapp.action.PLAY"))
        LocalBroadcastManager.getInstance(this).registerReceiver(mediaPlayerReceiver as BroadcastReceiver, IntentFilter("com.nathan.equipapp.action.STOP"))





    }
    
    private val afChangeListener = AudioManager.OnAudioFocusChangeListener() {focusChange: Int ->  
        
        // do something if focus changes.
        
    }

    private var audioFocusRequest: AudioFocusRequest? = null

    private val callback = object: MediaSessionCompat.Callback() {
        override fun onCommand(command: String, args: Bundle?, cb: ResultReceiver?) {
            super.onCommand(command, args, cb)
        }

        override fun onPlay() {
            val am = MyApplication.currentActivity.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            // Request audio focus for playback, this registers the afChangeListener
            if (android.os.Build.VERSION.SDK_INT >= 26) {
                audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN).run {
                    setOnAudioFocusChangeListener(afChangeListener)
                    setAudioAttributes(AudioAttributes.Builder().run {
                        setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        build()
                    })
                    build()
                }

                val result = am.requestAudioFocus(audioFocusRequest)
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // Start the service
                    MyApplication.currentActivity.startService(
                        Intent(
                            MyApplication.currentActivity,
                            MediaPlayerService::class.java
                        )
                    )
                    // Set the session active  (and update metadata and state)
                    mMediaSession.isActive = true
                    // start the player (custom call)
                    mMediaPlayer?.start()
                    // Register BECOME_NOISY BroadcastReceiver
                    //MyApplication.currentActivity.registerReceiver(myNoisyAudioStreamReceiver, intentFilter)
                    // Put the service in the foreground, post notification
                }
            }

        }

        override fun onPause() {
            super.onPause()
        }

        override fun onStop() {
            super.onStop()
        }

        override fun onSeekTo(pos: Long) {
            super.onSeekTo(pos)
        }

    }

    override fun onPrepared(mp: MediaPlayer) {
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mp.start()
    }


    private lateinit var mMediaPlayer: MediaPlayer

    fun initMediaPlayer() {
        mMediaPlayer = MediaPlayer()
        mMediaPlayer?.setOnErrorListener(this)
        mMediaPlayer?.apply { setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK) }
        mediaPlayerReceiver = MyPlayerReceiver(mMediaPlayer, this@MediaPlayerService)
        Log.d(TAG, "initMediaPlayer: started")

    }


}
