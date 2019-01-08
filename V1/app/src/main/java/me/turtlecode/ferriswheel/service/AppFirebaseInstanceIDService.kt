package me.turtlecode.ferriswheel.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import me.turtlecode.ferriswheel.util.FirestoreUtil

/**
 * Created by louis on 02/06/2018 at 16:15
 **/
class AppFirebaseInstanceIDService : FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        val newRegistrationToken = FirebaseInstanceId.getInstance().token

        if (FirebaseAuth.getInstance().currentUser != null)
            addTokenToFirestore(newRegistrationToken)
    }

    companion object {
        fun addTokenToFirestore(newRegistrationToken: String?) {

            if (newRegistrationToken == null) throw NullPointerException("FCM Token is NULL")

            FirestoreUtil.getFCMRegistrationToken {
                if (it.contains(newRegistrationToken))
                    return@getFCMRegistrationToken

                it.add(newRegistrationToken)
                FirestoreUtil.setFCMRegistrationToken(it)
            }

        }
    }
}