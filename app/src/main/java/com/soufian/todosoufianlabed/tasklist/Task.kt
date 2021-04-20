package com.soufian.todosoufianlabed.tasklist

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Task(@SerialName("description") val description : String ? = "PAS DE DESC", @SerialName("id") val id : String, @SerialName("title") val title : String  ) : Serializable