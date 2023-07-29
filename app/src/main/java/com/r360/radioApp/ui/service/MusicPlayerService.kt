package com.r360.radioApp.ui.service

import android.annotation.SuppressLint
import android.app.*
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.google.android.exoplayer2.ExoPlayer
import com.r360.radioApp.utils.Constant.ACTION_PAUSE_SERVICE
import com.r360.radioApp.utils.Constant.ACTION_START_OR_RESUME_SERVICE
import com.r360.radioApp.utils.Constant.ACTION_STOP_SERVICE
import com.r360.radioApp.utils.Constant.NOTIFICATION_CHANNEL_ID
import com.r360.radioApp.utils.Constant.NOTIFICATION_CHANNEL_NAME
import com.r360.radioApp.utils.Constant.NOTIFICATION_ID
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MusicPlayerService : LifecycleService() {
    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder




    companion object {
        lateinit var simpleExoPlayer: ExoPlayer

        var isServiceStarted = false

    }



    @SuppressLint("VisibleForTests", "SimpleDateFormat")
    override fun onCreate() {
        super.onCreate()


    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if (!isServiceStarted) {
                        startForegroundService()
//                        setupRecoding()
                        isServiceStarted = true;
                    }
                }
                ACTION_PAUSE_SERVICE -> {
                    isServiceStarted = false
                }
                ACTION_STOP_SERVICE -> {
                    killedService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }


    fun killedService() {
        stopSelf()

        simpleExoPlayer.playWhenReady = false
        simpleExoPlayer.release()
        simpleExoPlayer.stop()
        simpleExoPlayer.playWhenReady = false
        isServiceStarted = false
        stopForeground(true)


    }


    private fun startForegroundService() {
        val noficationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannnel(noficationManager)
        }

        startForeground(NOTIFICATION_ID, baseNotificationBuilder.build())


    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannnel(notificationManager: NotificationManager) {
        val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(notificationChannel)
    }


//    private fun setupRecoding() {
//        val mediaDataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(this)
//
//        val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory)
//            .createMediaSource(MediaItem.fromUri(MainActivity2.RADIO_URL))
//        val mediaSourceFactory = DefaultMediaSourceFactory(mediaDataSourceFactory)
//        simpleExoPlayer = ExoPlayer.Builder(this)
//            .setMediaSourceFactory(mediaSourceFactory)
//            .build()
//
//        simpleExoPlayer.addMediaSource(mediaSource)
//        simpleExoPlayer.prepare()
//
//    }



    override fun onDestroy() {
        super.onDestroy()
        simpleExoPlayer.release()
        simpleExoPlayer.stop()
        simpleExoPlayer.playWhenReady = false

    }

}