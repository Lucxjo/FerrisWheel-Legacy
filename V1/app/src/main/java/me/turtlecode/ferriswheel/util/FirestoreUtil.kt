package me.turtlecode.ferriswheel.util

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.getSystemService
import com.crashlytics.android.Crashlytics
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_account.view.*
import me.turtlecode.ferriswheel.AppConstants
import me.turtlecode.ferriswheel.ChatActivity
import me.turtlecode.ferriswheel.R
import me.turtlecode.ferriswheel.model.*
import me.turtlecode.ferriswheel.recyclerview.item.PersonItem
import me.turtlecode.ferriswheel.recyclerview.item.TextMessageItem
import java.util.*

object FirestoreUtil {
    private val fsInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val currentUserDocRef: DocumentReference
        get() = fsInstance.document("users/${FirebaseAuth.getInstance().currentUser?.uid
                ?: throw NullPointerException("UID is NULL")}")

    private val chatChannelCollectionRef = fsInstance.collection("chatChannels")

    private val lists = fsInstance.collection("lists")
    private var mGender: Int = 0

    fun initCurrentUserFirstTime(onComplete: () -> Unit) {
        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {

                val newUser = User(FirebaseAuth.getInstance().currentUser?.displayName ?: "",
                        "",0, 0, 0, 0, "", mutableListOf())

                currentUserDocRef.set(newUser).addOnSuccessListener {
                    onComplete()
                }
            }
            else
                onComplete()
        }
    }

    fun doUsernameThing(gender: Int): String {

        var mNames: Any? = "Blue"

        var gType: String = "names"

        if (gender == 0) {
            gType = "names"
        } else if (gender == 1) {
            gType = "fNames"
        } else {
            gType = "nBNames"
        }

        lists.document(gType).get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {

                val listSize = 21

                mNames = documentSnapshot.data?.get(listSize.toString())
            }
        }

        val name: String = mNames as String

        return name

    }

    fun updateCurrentUser(name: String = "",
                          chats: Int = 0,
                          gender: Int = 0,
                          orientation: Int = 0,
                          relationship: Int = 0,
                          phoneNumber: String = "") {

        val userFieldMap = mutableMapOf<String, Any>()
        if (name.isNotBlank()) userFieldMap["name"] = name
        if (chats != null) userFieldMap["chats"] = chats
        if (gender != null) {
            userFieldMap["gender"] = gender

            val dName = doUsernameThing(gender)

            userFieldMap["dName"] = dName
        }
        if (orientation != null) userFieldMap["orientation"] = orientation
        if (relationship != null) userFieldMap["relationship"] = relationship
        if (phoneNumber.isNotBlank()) userFieldMap["phoneNumber"] = phoneNumber

        currentUserDocRef.update(userFieldMap)

    }

    fun getCurrentUser(onComplete: (User) -> Unit) {
        currentUserDocRef.get()
                .addOnSuccessListener {
                    onComplete(it.toObject(User::class.java)!!)
                }
    }

    fun addUsersListener(context: Context, onListen: (List<Item>) -> Unit): ListenerRegistration {

        return fsInstance.collection("users")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        Crashlytics.log(Log.ERROR, "FW.FsUtil", firebaseFirestoreException.message)
                        return@addSnapshotListener
                    }

                    val items = mutableListOf<Item>()
                    querySnapshot?.documents?.forEach {
                        if (it.id != FirebaseAuth.getInstance().currentUser?.uid)
                            items.add(PersonItem(it.toObject(User::class.java)!!, it.id, context))
                    }

                    onListen(items)

                }
    }

    fun removeListener(registration: ListenerRegistration) = registration.remove()

    fun getOrCreateChatChannel(otherUserID: String,
                               context: Context,
                               onComplete: (channelID: String) -> Unit) {
        currentUserDocRef.collection("engagedChatChannels")
                .document(otherUserID).get().addOnSuccessListener {
                    if (it.exists()) {
                        onComplete(it["channelID"] as String)
                        return@addOnSuccessListener
                    }

                    val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid

                    val newChannel = chatChannelCollectionRef.document()
                    newChannel.set(ChatChannel(mutableListOf(currentUserID, otherUserID)))

                    currentUserDocRef
                            .collection("engagedChatChannels")
                            .document(otherUserID)
                            .set(mapOf("channelID" to newChannel.id))

                    fsInstance.collection("users").document(otherUserID)
                            .collection("engagedChatChannels")
                            .document(currentUserID)
                            .set(mapOf("channelID" to newChannel.id))

                    onComplete(newChannel.id)
                }
    }

    fun addChatMessagesListener(channelID: String, context: Context,
                                onListen: (List<Item>) -> Unit) : ListenerRegistration {
        return chatChannelCollectionRef.document(channelID).collection("messages")
                .orderBy("time")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        Crashlytics.log(Log.ERROR, "FW.FsUtil", firebaseFirestoreException.message)
                        return@addSnapshotListener
                    }

                    val items = mutableListOf<Item>()
                    querySnapshot?.documents?.forEach {
                        if (it["type"] == MessageType.TEXT)
                            items.add(TextMessageItem(it.toObject(TextMessage::class.java)!!, context))
                        else
                            TODO("Add image message.")
                    }

                    onListen(items)

                }
    }

    fun sendMessage(message: Message, channelID: String) {
        chatChannelCollectionRef.document(channelID)
                .collection("messages")
                .add(message)
    }

    //REIGION: FCM

    fun getFCMRegistrationToken(onComplete: (tokens: MutableList<String>) -> Unit) {
        currentUserDocRef.get().addOnSuccessListener {
            val user = it.toObject(User::class.java)!!

            onComplete(user.tokens)
        }
    }

    fun setFCMRegistrationToken(tokens: MutableList<String>) {
        currentUserDocRef.update(mapOf("tokens" to tokens))
    }
    //END REIGION: FCM
}