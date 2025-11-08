package com.example.application_anonyme.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.votrenom.anonymoussocial.api.Post
import com.votrenom.anonymoussocial.api.RetrofitClient
import com.votrenom.anonymoussocial.utils.NetworkResult
import com.votrenom.anonymoussocial.utils.PreferenceManager
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val api = RetrofitClient.apiService
    private val prefManager = PreferenceManager(application)

    private val _userPosts = MutableLiveData<NetworkResult<List<Post>>>()
    val userPosts: LiveData<NetworkResult<List<Post>>> = _userPosts

    fun loadUserPosts() {
        viewModelScope.launch {
            _userPosts.value = NetworkResult.Loading()

            val token = prefManager.getAuthHeader()
            if (token == null) {
                _userPosts.value = NetworkResult.Error("Non authentifié")
                return@launch
            }

            try {
                val response = api.getUserPosts(token)

                if (response.isSuccessful && response.body() != null) {
                    _userPosts.value = NetworkResult.Success(response.body()!!.posts)
                } else {
                    _userPosts.value = NetworkResult.Error("Erreur de chargement")
                }
            } catch (e: Exception) {
                _userPosts.value = NetworkResult.Error(e.message ?: "Erreur réseau")
            }
        }
    }

    fun getUserPseudo(): String? = prefManager.getUserPseudo()

    fun logout() {
        prefManager.clearAll()
    }
}