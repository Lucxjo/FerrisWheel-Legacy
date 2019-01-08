package me.turtlecode.ferriswheel.service

import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import me.turtlecode.ferriswheel.R

/**
 * Created by louis on 02/06/2018 at 16:47
 **/
class AppFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
            val from: String = remoteMessage.from!!
            val body = remoteMessage.notification!!.body

            val mBuilder = NotificationCompat.Builder(this, "Chats")
                    .setSmallIcon(R.drawable.ic_chat_bubble_outline_100dp)
                    .setContentTitle("${from} sent you a message.")
                    .setContentText(body)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)


            Log.d("FCM", remoteMessage.data.toString())
        }
    }

}