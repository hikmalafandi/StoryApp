package com.example.storyapp.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.Stories
import com.example.storyapp.api.StoriesResponse
import com.example.storyapp.model.DataUser
import com.example.storyapp.model.UserPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: UserPreference) : ViewModel() {

    private val _stories = MutableLiveData<List<Stories>>()
    val stories: LiveData<List<Stories>> = _stories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUser(): LiveData<DataUser> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    fun getStories(page: Int, size: Int) {
        viewModelScope.launch {
            _isLoading.value = (true)
            val token = pref.getToken().first() ?: ""
            val apiService = ApiConfig().getApiService(token).getStories(page, size)
            apiService.enqueue(object : Callback<StoriesResponse> {
                override fun onResponse(call: Call<StoriesResponse>, response: Response<StoriesResponse>) {
                    _isLoading.value = (false)
                    if (response.isSuccessful) {
                        _stories.value = response.body()?.listStory ?: emptyList()
                    } else {
                        Log.e(TAG, "Story gagal dimuat. Status code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                    Log.e(TAG, "Story gagal dimuat", t)
                }
            })
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}
