package com.votrenom.application_anonyme.data.repository

import com.votrenom.application_anonyme.data.api.RetrofitClient
import com.votrenom.application_anonyme.data.model.CreatePostRequest
import com.votrenom.application_anonyme.data.model.Post
import com.votrenom.application_anonyme.utils.Constants
import com.votrenom.application_anonyme.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class PostRepository {

    private val apiService = RetrofitClient.instance

    /**
     * Récupérer tous les posts du fil d'actualité
     */
    suspend fun getPosts(): NetworkResult<List<Post>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getPosts()

                if (response.isSuccessful) {
                    response.body()?.let { posts ->
                        // Filtrer uniquement les posts approuvés
                        val approvedPosts = posts.filter {
                            it.moderationStatus == "approved" || it.isModerated
                        }
                        NetworkResult.Success(approvedPosts)
                    } ?: NetworkResult.Success(emptyList())
                } else {
                    NetworkResult.Error(
                        when (response.code()) {
                            500 -> "Erreur serveur"
                            else -> "Erreur: ${response.code()}"
                        }
                    )
                }
            } catch (e: HttpException) {
                NetworkResult.Error("Erreur réseau: ${e.message()}")
            } catch (e: IOException) {
                NetworkResult.Error(Constants.ERROR_NETWORK)
            } catch (e: Exception) {
                NetworkResult.Error("Erreur inconnue: ${e.localizedMessage}")
            }
        }
    }

    /**
     * Créer un nouveau post
     */
    suspend fun createPost(
        content: String,
        token: String
    ): NetworkResult<Post> {
        return withContext(Dispatchers.IO) {
            try {
                val request = CreatePostRequest(content)
                val response = apiService.createPost("Bearer $token", request)

                if (response.isSuccessful) {
                    response.body()?.let {
                        NetworkResult.Success(it)
                    } ?: NetworkResult.Error("Erreur lors de la création")
                } else {
                    NetworkResult.Error(
                        when (response.code()) {
                            400 -> "Contenu invalide"
                            401 -> "Session expirée"
                            403 -> "Publication refusée par la modération"
                            413 -> "Contenu trop long"
                            429 -> "Trop de publications. Réessayez plus tard"
                            500 -> "Erreur serveur"
                            else -> "Erreur: ${response.code()}"
                        }
                    )
                }
            } catch (e: HttpException) {
                NetworkResult.Error("Erreur réseau: ${e.message()}")
            } catch (e: IOException) {
                NetworkResult.Error(Constants.ERROR_NETWORK)
            } catch (e: Exception) {
                NetworkResult.Error("Erreur inconnue: ${e.localizedMessage}")
            }
        }
    }

    /**
     * Récupérer les posts d'un utilisateur spécifique
     */
    suspend fun getUserPosts(userId: String, token: String): NetworkResult<List<Post>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getUserPosts("Bearer $token", userId)

                if (response.isSuccessful) {
                    response.body()?.let {
                        NetworkResult.Success(it)
                    } ?: NetworkResult.Success(emptyList())
                } else {
                    NetworkResult.Error(
                        when (response.code()) {
                            401 -> "Session expirée"
                            404 -> "Utilisateur non trouvé"
                            else -> "Erreur: ${response.code()}"
                        }
                    )
                }
            } catch (e: HttpException) {
                NetworkResult.Error("Erreur réseau: ${e.message()}")
            } catch (e: IOException) {
                NetworkResult.Error(Constants.ERROR_NETWORK)
            } catch (e: Exception) {
                NetworkResult.Error("Erreur inconnue: ${e.localizedMessage}")
            }
        }
    }

    /**
     * Supprimer un post
     */
    suspend fun deletePost(postId: String, token: String): NetworkResult<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.deletePost("Bearer $token", postId)

                if (response.isSuccessful) {
                    NetworkResult.Success(true)
                } else {
                    NetworkResult.Error(
                        when (response.code()) {
                            401 -> "Session expirée"
                            403 -> "Vous n'êtes pas autorisé à supprimer ce post"
                            404 -> "Post non trouvé"
                            else -> "Erreur: ${response.code()}"
                        }
                    )
                }
            } catch (e: HttpException) {
                NetworkResult.Error("Erreur réseau: ${e.message()}")
            } catch (e: IOException) {
                NetworkResult.Error(Constants.ERROR_NETWORK)
            } catch (e: Exception) {
                NetworkResult.Error("Erreur inconnue: ${e.localizedMessage}")
            }
        }
    }
}