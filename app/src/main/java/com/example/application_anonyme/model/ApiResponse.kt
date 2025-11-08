package com.votrenom.application_anonyme.data.model

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: T?,

    @SerializedName("message")
    val message: String?
)

data class LoginRequest(
    @SerializedName("pseudo")
    val pseudo: String
)

data class LoginResponse(
    @SerializedName("user")
    val user: User,

    @SerializedName("token")
    val token: String
)

data class CreatePostRequest(
    @SerializedName("content")
    val content: String
)