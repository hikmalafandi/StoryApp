package com.example.storyapp.view.upload

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.api.AddStoriesResponse
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.model.UserPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadViewModel(private val pref: UserPreference): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _uploadSuccess = MutableLiveData(false)
    val uploadSuccess: LiveData<Boolean> = _uploadSuccess

    fun addStory(file: MultipartBody.Part, description: RequestBody) {
        viewModelScope.launch {
            _isLoading.value = (true)
            val token = pref.getToken().first() ?: ""
            val apiService = ApiConfig().getApiService(token)
            val addStoryUser = apiService.addStories(file, description)
            addStoryUser.enqueue(object: Callback<AddStoriesResponse>{
                override fun onResponse(call: Call<AddStoriesResponse>, response: Response<AddStoriesResponse>) {
                    _isLoading.value = (false)
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error) {
                            _uploadSuccess.postValue(true)
                            Log.e(TAG, "Upload berhasil!: ${response.message()}")
                        }
                    } else {
                        Log.e(TAG, "Upload Error!: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<AddStoriesResponse>, t: Throwable) {
                    Log.e(TAG, "Upload Gagal!: ${t.message.toString()}")
                }

            })
        }

    }

    companion object {
        private const val TAG = "UploadViewModel"
    }
}