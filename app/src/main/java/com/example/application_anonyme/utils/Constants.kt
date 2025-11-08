package com.votrenom.application_anonyme.utils

object Constants {
    // API
    const val BASE_URL = "https://votre-backend-url.com/api/" // À modifier plus tard
    const val CONNECT_TIMEOUT = 30L
    const val READ_TIMEOUT = 30L
    const val WRITE_TIMEOUT = 30L

    // SharedPreferences
    const val PREFS_NAME = "application_anonyme_prefs"
    const val KEY_USER_ID = "user_id"
    const val KEY_USER_PSEUDO = "user_pseudo"
    const val KEY_USER_TOKEN = "user_token"
    const val KEY_IS_LOGGED_IN = "is_logged_in"
    const val KEY_USER_CREATED_AT = "user_created_at"

    // Validation
    const val MIN_PSEUDO_LENGTH = 3
    const val MAX_PSEUDO_LENGTH = 20
    const val MIN_POST_LENGTH = 1
    const val MAX_POST_LENGTH = 500

    // Messages
    const val ERROR_NETWORK = "Erreur de connexion au réseau"
    const val ERROR_UNKNOWN = "Une erreur est survenue"
    const val SUCCESS_POST_CREATED = "Publication créée avec succès"
    const val SUCCESS_LOGIN = "Connexion réussie"
}