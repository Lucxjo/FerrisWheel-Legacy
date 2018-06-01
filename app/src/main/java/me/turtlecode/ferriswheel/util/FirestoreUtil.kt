package me.turtlecode.ferriswheel.util

import android.content.Context
import android.util.Log
import android.view.View
import com.crashlytics.android.Crashlytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_account.view.*
import me.turtlecode.ferriswheel.R
import me.turtlecode.ferriswheel.model.*
import me.turtlecode.ferriswheel.recyclerview.item.PersonItem
import me.turtlecode.ferriswheel.recyclerview.item.TextMessageItem

object FirestoreUtil {
    private val fsInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val currentUserDocRef: DocumentReference
        get() = fsInstance.document("users/${FirebaseAuth.getInstance().currentUser?.uid
                ?: throw NullPointerException("UID is NULL")}")

    private val chatChannelCollectionRef = fsInstance.collection("chatChannels")

    fun initCurrentUserFirstTime(onComplete: () -> Unit) {
        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                val newUser = User(FirebaseAuth.getInstance().currentUser?.displayName ?: "",
                        0, 0, 0, 0, "")

                currentUserDocRef.set(newUser).addOnSuccessListener {
                    onComplete()
                }
            }
            else
                onComplete()
        }
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
        if (gender != null) userFieldMap["gender"] = gender
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
                    querySnapshot.documents.forEach {
                        if (it.id != FirebaseAuth.getInstance().currentUser?.uid)
                            items.add(PersonItem(it.toObject(User::class.java), it.id, context))
                    }

                    onListen(items)

                }
    }

    fun removeListener(registration: ListenerRegistration) = registration.remove()

    fun getOrCreateChatChannel(otherUserID: String,
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
                    querySnapshot.documents.forEach {
                        if (it["type"] == MessageType.TEXT)
                            items.add(TextMessageItem(it.toObject(TextMessage::class.java), context))
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

}