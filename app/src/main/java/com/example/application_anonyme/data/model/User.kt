package com.example.application_anonyme.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: String,

    @SerializedName("pseudo")
    val pseudo: String,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("avatarUrl")
    val avatarUrl: String? = null
)