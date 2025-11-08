package com.example.application_anonyme.data.repository

import com.example.application_anonyme.data.api.RetrofitClient
import com.example.application_anonyme.data.model.LoginRequest
import com.example.application_anonyme.data.model.LoginResponse
import com.example.application_anonyme.data.model.User
import com.example.application_anonyme.utils.Constants
import com.example.application_anonyme.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class UserRepository {

    private val apiService = RetrofitClient.instance

    /**
     * Connexion de l'utilisateur avec son pseudo
     */
    suspend fun login(request: LoginRequest): NetworkResult<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.login(request)

                if (response.isSuccessful) {
                    response.body()?.let {
                        NetworkResult.Success(it)
                    } ?: NetworkResult.Error("Réponse vide du serveur")
                } else {
                    NetworkResult.Error(
                        when (response.code()) {
                            400 -> "Pseudo invalide"
                            409 -> "Ce pseudo est déjà utilisé"
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
     * Récupérer le profil de l'utilisateur
     */
    suspend fun getProfile(token: String): NetworkResult<User> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getProfile("Bearer $token")

                if (response.isSuccessful) {
                    response.body()?.let {
                        NetworkResult.Success(it)
                    } ?: NetworkResult.Error("Profil non trouvé")
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
}