package com.example.application_anonyme.model

data class Post(
    val id: String,
    val content: String,
    val authorPseudo: String,
    val createdAt: String,
    val isModerated: Boolean = false
)