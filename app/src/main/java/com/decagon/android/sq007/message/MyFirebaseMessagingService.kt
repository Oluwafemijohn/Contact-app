package com.decagon.android.sq007.message

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(p0: String) {
//        super.onNewToken(p0)
        Log.d("Tag", "This is token reference $p0")
    }
}
