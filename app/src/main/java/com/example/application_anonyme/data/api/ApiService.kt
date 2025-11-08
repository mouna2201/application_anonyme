package com.example.application_anonyme.data.api

import com.example.application_anonyme.data.model.CreatePostRequest
import com.example.application_anonyme.data.model.LoginRequest
import com.example.application_anonyme.data.model.LoginResponse
import com.example.application_anonyme.data.model.ModerationDecision
import com.example.application_anonyme.data.model.Post
import com.example.application_anonyme.data.model.User
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    /**
     * AUTHENTIFICATION
     */
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    /**
     * PROFIL UTILISATEUR
     */
    @GET("users/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): Response<User>

    /**
     * POSTS - Fil d'actualité
     */
    @GET("posts")
    suspend fun getPosts(): Response<List<Post>>

    @POST("posts")
    suspend fun createPost(
        @Header("Authorization") token: String,
        @Body request: CreatePostRequest
    ): Response<Post>

    @GET("posts/user/{userId}")
    suspend fun getUserPosts(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Response<List<Post>>

    @DELETE("posts/{postId}")
    suspend fun deletePost(
        @Header("Authorization") token: String,
        @Path("postId") postId: String
    ): Response<Unit>

    /**
     * MODÉRATION (pour les évolutions futures)
     */
    @GET("posts/pending")
    suspend fun getPendingPosts(
        @Header("Authorization") token: String
    ): Response<List<Post>>

    @PUT("posts/{postId}/moderate")
    suspend fun moderatePost(
        @Header("Authorization") token: String,
        @Path("postId") postId: String,
        @Body decision: ModerationDecision
    ): Response<Post>
}