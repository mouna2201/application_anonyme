package com.example.application_anonyme.ui.post

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.votrenom.anonymoussocial.api.CreatePostRequest
import com.votrenom.anonymoussocial.api.PostResponse
import com.votrenom.anonymoussocial.api.RetrofitClient
import com.votrenom.anonymoussocial.utils.NetworkResult
import com.votrenom.anonymoussocial.utils.PreferenceManager
import kotlinx.coroutines.launch

class CreatePostViewModel(application: Application) : AndroidViewModel(application) {

    private val api = RetrofitClient.apiService
    private val prefManager = PreferenceManager(application)

    private val _createResult = MutableLiveData<NetworkResult<PostResponse>>()
    val createResult: LiveData<NetworkResult<PostResponse>> = _createResult

    fun createPost(content: String) {
        viewModelScope.launch {
            _createResult.value = NetworkResult.Loading()

            val token = prefManager.getAuthHeader()
            if (token == null) {
                _createResult.value = NetworkResult.Error("Non authentifié")
                return@launch
            }

            try {
                val response = api.createPost(token, CreatePostRequest(content))

                if (response.isSuccessful && response.body() != null) {
                    _createResult.value = NetworkResult.Success(response.body()!!)
                } else {
                    _createResult.value = NetworkResult.Error("Erreur de publication")
                }
            } catch (e: Exception) {
                _createResult.value = NetworkResult.Error(e.message ?: "Erreur réseau")
            }
        }
    }
}