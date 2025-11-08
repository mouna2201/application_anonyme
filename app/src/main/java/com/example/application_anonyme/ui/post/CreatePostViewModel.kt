package com.example.application_anonyme.ui.post

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.application_anonyme.data.api.CreatePostRequest
import com.example.application_anonyme.data.api.PostResponse
import com.example.application_anonyme.data.api.RetrofitClient
import com.example.application_anonyme.utils.NetworkResult
import com.example.application_anonyme.utils.PreferenceManager
import kotlinx.coroutines.launch

class CreatePostViewModel(application: Application) : AndroidViewModel(application) {

    private val api = RetrofitClient.instance
    private val prefManager = PreferenceManager(application)

    private val _createResult = MutableLiveData<NetworkResult<PostResponse>>()
    val createResult: LiveData<NetworkResult<PostResponse>> = _createResult

    fun createPost(content: String) {
        viewModelScope.launch {
            _createResult.value = NetworkResult.Loading()

            val token = prefManager.getToken()
            if (token == null) {
                _createResult.value = NetworkResult.Error("Non authentifié")
                return@launch
            }

            try {
                val response = api.createPost("Bearer $token", CreatePostRequest(content))

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