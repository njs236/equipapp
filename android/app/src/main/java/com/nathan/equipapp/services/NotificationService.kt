package com.nathan.equipapp.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.*
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.nathan.equipapp.MyApplication
import com.nathan.equipapp.R
import com.nathan.equipapp.Utils.WebInterface
import com.nathan.equipapp.assets.Notification
import com.nathan.equipapp.ui.MainActivity
import java.io.FileDescriptor
import java.lang.Exception
import java.util.*

private const val CHANNEL_ID = "com.nathan.equipapp.channel.Notification"

class NotificationService : Service() {

    private var db = FirebaseFirestore.getInstance()
    private var notis = ArrayList<Notification>()
    private var TAG = this.javaClass.simpleName
    var waitingTimer: Thread? = null
    var mode: Int? = -1
    private val mBinder = LocalBinder()

    inner class LocalBinder: Binder() {
        fun getService(): NotificationService = this@NotificationService
    }

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    override fun onCreate() {

        super.onCreate()


        createNotificationChannel()
        //start ongoing loop.
        callTimer()




    }

    var handler = Handler {_ ->
        if (WebInterface.isOnline(MyApplication.currentActivity)) {

            checkDB()


        } else {
            Toast.makeText(
                MyApplication.currentActivity,
                getString(R.string.check_internet),
                Toast.LENGTH_SHORT
            ).show()

        }

        true
    }


    private fun callTimer() {

            mode = 0
            waitingTimer = Thread(Runnable {

                while (true) {
                    try {
                        Thread.sleep(30000)
                        handler.sendEmptyMessage(0)


                    } catch (ex: Exception) {

                    }

                }
            })
        waitingTimer?.start()


    }

    private fun checkForOccuringNotification() {
        for (notification in notis) {
            val delayInMillis = notification.delay *1000

            var calendar= Calendar.getInstance()
            var time = calendar.time
            //subtract delay from time of notification.
            // the date object which corresponds to when event fires.
            var triggerNotiTime = Date(notification.date.time - delayInMillis)
            if (!time.after(triggerNotiTime) && !time.before(triggerNotiTime)) {

                // if at the specified time.
                // send Notification.
                sendNotification(MyApplication.currentActivity, notification.description)
            }
        }
    }

    fun sendNotification(context: Context, message: String ) {

        //implement a call to the map activity when you receive a notification
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        // implement notification with
        // icon from app
        // sound
        // message

        Log.d(TAG, "sendNotification: message: $message")


        var mBuilder = NotificationCompat.Builder(context, CHANNEL_ID )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
            .setContentTitle(context.getString(R.string.notification_content_title))
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            val notificationId = 123
            notify(notificationId, mBuilder.build())
        }


    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun checkDB() {
        notis.clear()
        var collection = db.collection("notis")

        collection.get().addOnSuccessListener {result->
            for (document in result) {
                val timestamp = document.getTimestamp("date")
                val date = timestamp?.toDate()
                val delay = document.get("delay") as Long
                val description = document.get("description") as String
                notis.add(Notification(date!!, delay, description))
            }
            if (false) {
                checkForOccuringNotification()
            }
            sendNotification(MyApplication.currentActivity, notis[0].description)

        }.addOnFailureListener {exception: Exception ->
            // report failure
            Log.d(TAG, "failed getting data. ex: $exception")

        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d(TAG, "onStartCommand: true")

        return super.onStartCommand(intent, flags, startId)
    }
}
