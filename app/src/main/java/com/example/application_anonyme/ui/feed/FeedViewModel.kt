package com.votrenom.anonymoussocial.ui.feed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.votrenom.anonymoussocial.api.FeedResponse
import com.votrenom.anonymoussocial.api.RetrofitClient
import com.votrenom.anonymoussocial.utils.NetworkResult
import kotlinx.coroutines.launch

class FeedViewModel(application: Application) : AndroidViewModel(application) {

    private val api = RetrofitClient.apiService

    private val _feedPosts = MutableLiveData<NetworkResult<FeedResponse>>()
    val feedPosts: LiveData<NetworkResult<FeedResponse>> = _feedPosts

    private var currentPage = 1

    fun loadFeed(page: Int = 1) {
        viewModelScope.launch {
            _feedPosts.value = NetworkResult.Loading()

            try {
                val response = api.getPosts(page)

                if (response.isSuccessful && response.body() != null) {
                    currentPage = page
                    _feedPosts.value = NetworkResult.Success(response.body()!!)
                } else {
                    _feedPosts.value = NetworkResult.Error("Erreur de chargement")
                }
            } catch (e: Exception) {
                _feedPosts.value = NetworkResult.Error(e.message ?: "Erreur réseau")
            }
        }
    }

    fun refresh() {
        loadFeed(1)
    }
}