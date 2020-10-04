package com.lockscreen.hanmo.lockscreenkotlinexample.lockscreen.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import com.lockscreen.hanmo.lockscreenkotlinexample.R


/**
 * Oreo 이상일 경우 Notification Channel 생성
 * Created by hanmo on 2018. 10. 2..
 */
object MyNotificationManager {

    private const val CHANNEL_ID = "LockScreen Example ID"
    private const val CHANNEL_NAME = "LockScreen Example CHANEL"
//    private val CHANNEL_DESCRIPTION = "This is LockScreen Example CHANEL"

    fun getChannelId(): String {
        return CHANNEL_ID
    }

    fun getChannelName(): String {
        return CHANNEL_NAME
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createMainNotificationChannel(context: Context?): Notification {
        val id = CHANNEL_ID
        val name = CHANNEL_NAME
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(id, name, importance)
//        mChannel.enableVibration(false)
//        mChannel.enableLights(false)

        val manager = context?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        val notificationBuilder = NotificationCompat.Builder(context, id)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("title")
                .setContentText("content text")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)

        return notificationBuilder.build()
    }

}
