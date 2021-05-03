package com.decagon.android.sq007.ui

import java.io.Serializable

data class RecyclerModel(
    var name: String? = "",
    var lastName: String? = "",
    var phoneNumber: String? = ""
//    var color:Int

) : Serializable
