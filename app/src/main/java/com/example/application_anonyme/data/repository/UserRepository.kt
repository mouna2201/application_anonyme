package com.example.application_anonyme.data.repository

import com.example.application_anonyme.data.api.AuthResponse
import com.example.application_anonyme.data.api.LoginRequest
import com.example.application_anonyme.data.api.RetrofitClient
import com.example.application_anonyme.utils.Constants
import com.example.application_anonyme.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class UserRepository {

    private val apiService = RetrofitClient.instance

    /**
     * Connexion de l'utilisateur avec son pseudo et mot de passe
     */
    suspend fun login(pseudo: String, password: String): NetworkResult<AuthResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val request = LoginRequest(pseudo, password)
                val response = apiService.login(request)

                if (response.isSuccessful) {
                    response.body()?.let {
                        NetworkResult.Success(it)
                    } ?: NetworkResult.Error("Réponse vide du serveur")
                } else {
                    NetworkResult.Error(
                        when (response.code()) {
                            400 -> "Pseudo ou mot de passe invalide"
                            401 -> "Identifiants incorrects"
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
}