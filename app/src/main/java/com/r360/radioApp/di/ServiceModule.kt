package com.r360.radioApp.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.r360.radioApp.MainActivity2
import com.r360.radioApp.R
import com.r360.radioApp.utils.Constant

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Singleton


@Module
@InstallIn(ServiceComponent::class)
class ServiceModule {

    @ServiceScoped
    @Provides
    fun providePendingIntent(@ApplicationContext app: Context) = PendingIntent.getActivity(
        app,
        0,
        Intent(app, MainActivity2::class.java).also {
            it.action = Constant.ACTION_ACTIVITY_TO_FRAGMENT
        }, PendingIntent.FLAG_IMMUTABLE
    )

    @ServiceScoped
    @Provides
    fun provideNotificationBuilder(
        @ApplicationContext app: Context,
        pendingIntent: PendingIntent
    ) = NotificationCompat.Builder(app, Constant.NOTIFICATION_CHANNEL_ID)
        .setContentText("Playing....")
        .setOngoing(true)
        .setSmallIcon(R.drawable.rlogo)
        .setAutoCancel(false)
        .setContentTitle("Radio 360")
        .setContentIntent(pendingIntent)



}