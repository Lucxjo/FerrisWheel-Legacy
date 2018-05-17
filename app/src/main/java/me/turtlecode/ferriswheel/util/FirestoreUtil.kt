package me.turtlecode.ferriswheel.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import me.turtlecode.ferriswheel.model.User

object FirestoreUtil {
    private val fsInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val currentUserDocRef: DocumentReference
        get() = fsInstance.document("users/${FirebaseAuth.getInstance().uid
                ?: throw NullPointerException("UID is NULL")}")

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
                    onComplete(it.toObject(User::class.java))
                }
    }

}