package com.example.application_anonyme.data.api

import com.android.identity.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    /**
     * URL de base de l'API
     * IMPORTANT: À modifier selon votre environnement
     *
     * Pour Android Emulator (AVD):
     * private const val BASE_URL = "http://10.0.2.2:3000/api/"
     *
     * Pour Appareil Physique (même réseau WiFi):
     * private const val BASE_URL = "http://192.168.x.x:3000/api/"
     * Remplacez 192.168.x.x par votre IP locale
     *
     * Pour Production:
     * private const val BASE_URL = "https://votre-domaine.com/api/"
     */
    private const val BASE_URL = "http://10.0.2.2:3000/api/"

    /**
     * Intercepteur pour logger les requêtes HTTP (utile pour le debug)
     */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /**
     * Configuration du client HTTP
     */
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            // Ajouter des headers communs à toutes les requêtes
            val request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build()
            chain.proceed(request)
        }
        .connectTimeout(Constants.CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(Constants.READ_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(Constants.WRITE_TIMEOUT, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    /**
     * Instance Retrofit singleton
     */
    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    /**
     * Fonction pour changer l'URL de base dynamiquement si nécessaire
     */
    fun createService(baseUrl: String = BASE_URL): ApiService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}