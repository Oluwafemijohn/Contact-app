package com.decagon.android.sq007.ui

import com.google.firebase.database.Exclude
import java.io.Serializable

data class RecyclerModel(
    @Exclude
    var id: String? = null,
    var firstName: String? = "",
    var lastName: String? = "",
    var phoneNumber: String? = ""
//    var color:Int

) : Serializable
