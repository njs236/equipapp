package com.nathan.equipapp.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.*
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
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
    private var instantNotis = ArrayList<Notification>()
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
            deleteLateNotifications()


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
                        Thread.sleep(10000)
                        handler.sendEmptyMessage(0)


                    } catch (ex: Exception) {

                    }

                }
            })
        waitingTimer?.start()


    }

    private fun sendInstantNotification() {
        val user = FirebaseAuth.getInstance().currentUser
        // pre-conditions: instantNotis contains a notification.
        for (notification in instantNotis) {
            //Log.d(TAG, "notificationID: ${notification.id}")
            //Log.d(TAG, "userId: ${user!!.uid}")
            db.collection(user!!.uid).document(notification.id!!).get().addOnSuccessListener { documentSnapshot ->

                if (documentSnapshot.data != null) {

                }else {
                    val data = HashMap<String, Any>()
                    // add data from notification of when it was sent and the valid time to send to clients.
                    data["timeToDeath"] = notification.delay
                    data["date"] = notification.date
                    data["message"] = notification.description
                    // update user collection with the id of the notification.
                    db.collection(user.uid).document(notification.id!!).set(data).addOnSuccessListener {
                        Log.d(TAG, "document created: ")
                        // condition: if a user fetches a notification after the timeToDie, don't send. Only send notification within the time frame it was originally specified with.
                        val delayInMillis = notification.delay * 1000
                        var calendar= Calendar.getInstance()
                        var time = calendar.time
                        //add delay from time of notification.
                        // the date object which corresponds to when event fires.
                        var triggerTimeToDie = Date(notification.date.time + delayInMillis)
                        // if there is still time on the clock for it to be sent
                        if (time.before(triggerTimeToDie)) {

                            // if at the specified time.
                            // send Notification.
                            sendNotification(MyApplication.currentActivity, notification.description)
                        } else {
                            Log.d(TAG, "this notification was sent after it is allowed to be retrieved by logged in user")



                        }

                    }.addOnFailureListener { error->
                        Log.w(TAG, "Error writing document", error)

                    }

                }
            }.addOnFailureListener { err ->

                Log.w(TAG, "error retrieving document: $err")
            }

        }
    }


    private fun checkForOccuringNotification() {

        for (notification in notis) {

            // create timeObject of when the notification is being sent.
                // get Delay in Milliseconds
            val delayInMillis = notification.delay *1000
            // create current Date/Time stamp.
            var calendar= Calendar.getInstance()
            var time = calendar.time
            //subtract delay from time of notification.
            // the date object which corresponds to when event fires.
            var triggerNotiTime = Date(notification.date.time - delayInMillis)

            if (notification.repeat != null) {
                val endTime = Date(notification.end!!.time - delayInMillis)
                // determine that the time is the same but day is different and falls between the start and end date
                if (time.time >= triggerNotiTime.time && time.time <= endTime.time) {
                    calendar.time = triggerNotiTime

                    calendar.set(Calendar.HOUR_OF_DAY, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)

                    var timeOfDayForNotificationInLong = triggerNotiTime.time - calendar.time.time
                    calendar.time = time
                    calendar.set(Calendar.HOUR_OF_DAY, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    var timeOfDayInLong = time.time - calendar.time.time

                    if ((timeOfDayInLong - 5000) <= timeOfDayForNotificationInLong && timeOfDayForNotificationInLong < (timeOfDayInLong + 5000) ) {
                        sendNotification(MyApplication.currentActivity, notification.description)
                    }

                }

            } else {
                // if at the specified time
                if ((triggerNotiTime.time - 5000) <= time.time && time.time < (triggerNotiTime.time + 5000) ) {

                    // send Notification.
                    sendNotification(MyApplication.currentActivity, notification.description)
                }
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
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
            .setContentTitle(context.getString(R.string.notification_content_title))
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
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

    private fun deleteLateNotifications() {
        db.collection("notis").whereEqualTo("now", true).get().addOnSuccessListener { result->
            for (document in result) {
                val timestamp = document.getTimestamp("date")
                val date = timestamp?.toDate()
                val delay = document.get("delay") as Long
                val delayInMillis = delay *1000

                var calendar= Calendar.getInstance()
                var time = calendar.time
                //add delay from time of notification.
                // the date object which corresponds to when event fires.

                var triggerTimeToDie = Date(date!!.time + delayInMillis)
                // if there is still time on the clock for it to be sent
                if (!time.before(triggerTimeToDie)) {
                    db.collection("notis").document(document.id).delete().addOnSuccessListener { Log.d(TAG, "Document deleted successfully") }
                        .addOnFailureListener { err->Log.w(TAG, "error deleting document", err) }
                }

            }
        }.addOnFailureListener { err->
            Log.w(TAG, "document collection error", err)
        }
    }

    private fun checkDB() {
        notis.clear()
        instantNotis.clear()
        var collection = db.collection("notis")

        collection.get().addOnSuccessListener {result->
            for (document in result) {
                val timestamp = document.getTimestamp("date")
                val date = timestamp?.toDate()
                val delay = document.get("delay") as Long
                val description = document.get("description") as String
                var repeat: Boolean = false
                var endTimestamp: Timestamp? = null
                var end: Date? = null


                if (document.get("now") != null) {
                    // For debug. retrieve if the notification is to be sent immediately or wait for a certain time.
                    val now = document.get("now") as Boolean
                   // Log.d(TAG, "now: $now")

                    instantNotis.add(Notification(date!!, delay, description, document.id))

                } else {
                    //Log.d(TAG, "delayedNotification")
                    // if the notification is repeating
                   // Log.d(TAG, "is Repeated: ${document.get("repeat")}")
                    if (document.get("repeats") != null) {
                        repeat = document.get("repeats") as Boolean
                        //Log.d(TAG, "repeat: $repeat")

                        endTimestamp = document.getTimestamp("end")
                        end = endTimestamp?.toDate()
                        //Log.d(TAG, "end: $end")
                        notis.add(Notification(date!!, delay, description, repeat, end!!))
                    } else {
                        notis.add(Notification(date!!, delay, description))
                    }


                }

            }
            if (instantNotis.size > 0) {
                sendInstantNotification()
            }
            if (notis.size > 0) {
                checkForOccuringNotification()
            }
            //if (false) {

            //}
            //sendNotification(MyApplication.currentActivity, notis[0].description)

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
