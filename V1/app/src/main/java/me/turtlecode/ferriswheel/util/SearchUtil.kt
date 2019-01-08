package me.turtlecode.ferriswheel.util

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

object SearchUtil {
    private val fsInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val searchingRef: DocumentReference
            get() = fsInstance.document("searching")
}