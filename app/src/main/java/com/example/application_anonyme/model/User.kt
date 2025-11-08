package com.example.application_anonyme.model

data class User(
    val id: String,
    val pseudo: String,
    val createdAt: String,
    val avatarUrl: String? = null
)