package me.turtlecode.ferriswheel.util

import com.google.firebase.storage.FirebaseStorage

object StorageUtil {

    private val storageInstance: FirebaseStorage by lazy { FirebaseStorage.getInstance() }

}