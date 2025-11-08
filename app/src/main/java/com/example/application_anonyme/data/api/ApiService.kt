// api/ApiService.kt
package com.votrenom.anonymoussocial.api

import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ========== AUTHENTIFICATION ==========
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @GET("auth/verify")
    suspend fun verifyToken(@Header("Authorization") token: String): Response<VerifyResponse>

    // ========== POSTS ==========
    @GET("posts")
    suspend fun getPosts(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<FeedResponse>

    @POST("posts")
    suspend fun createPost(
        @Header("Authorization") token: String,
        @Body request: CreatePostRequest
    ): Response<PostResponse>

    @GET("posts/user/me")
    suspend fun getUserPosts(@Header("Authorization") token: String): Response<UserPostsResponse>

    @DELETE("posts/{id}")
    suspend fun deletePost(
        @Header("Authorization") token: String,
        @Path("id") postId: Int
    ): Response<MessageResponse>

    // ========== ADMIN ==========
    @GET("admin/posts/pending")
    suspend fun getPendingPosts(@Header("Authorization") token: String): Response<PendingPostsResponse>

    @PUT("admin/posts/{id}/approve")
    suspend fun approvePost(
        @Header("Authorization") token: String,
        @Path("id") postId: Int
    ): Response<PostResponse>

    @PUT("admin/posts/{id}/reject")
    suspend fun rejectPost(
        @Header("Authorization") token: String,
        @Path("id") postId: Int,
        @Body reason: RejectReason
    ): Response<PostResponse>

    @GET("admin/stats")
    suspend fun getStats(@Header("Authorization") token: String): Response<StatsResponse>
}

// ========== MODÈLES DE REQUÊTES ==========
data class LoginRequest(val pseudo: String, val password: String)
data class RegisterRequest(val pseudo: String, val password: String)
data class CreatePostRequest(val content: String)
data class RejectReason(val reason: String)

// ========== MODÈLES DE RÉPONSES ==========
data class AuthResponse(val message: String, val token: String, val user: User)
data class VerifyResponse(val valid: Boolean, val user: User)
data class PostResponse(val message: String, val post: Post)
data class FeedResponse(val posts: List<Post>, val pagination: Pagination)
data class UserPostsResponse(val posts: List<Post>)
data class PendingPostsResponse(val posts: List<Post>)
data class MessageResponse(val message: String)
data class StatsResponse(val stats: Stats)

// ========== MODÈLES DE DONNÉES ==========
data class User(
    val id: Int,
    val pseudo: String,
    val isAdmin: Boolean = false,
    val createdAt: String
)

data class Post(
    val id: Int,
    val content: String,
    val authorPseudo: String? = null,
    val createdAt: String,
    val isApproved: Boolean? = null,
    val moderationReason: String? = null
)

data class Pagination(
    val currentPage: Int,
    val totalPages: Int,
    val totalPosts: Int,
    val hasNext: Boolean,
    val hasPrevious: Boolean
)

data class Stats(
    val totalUsers: Int,
    val totalPosts: Int,
    val approvedPosts: Int,
    val rejectedPosts: Int,
    val pendingPosts: Int
)