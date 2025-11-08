package com.example.application_anonyme.data.model

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("id")
    val id: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("authorPseudo")
    val authorPseudo: String,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("isModerated")
    val isModerated: Boolean = false,

    @SerializedName("moderationStatus")
    val moderationStatus: String = "pending" // pending, approved, rejected
)