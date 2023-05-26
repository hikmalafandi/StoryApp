package com.example.storyapp.view.signup

import android.util.Log
import androidx.lifecycle.*
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.RegisterResponse
import com.example.storyapp.model.DataUser
import com.example.storyapp.model.UserPreference
import retrofit2.Callback
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class RegisterViewModel(private val pref: UserPreference) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _registerSuccess = MutableLiveData(false)
    val registerSuccess: LiveData<Boolean> = _registerSuccess

    fun register(name: String, email: String, password: String) {
        _isLoading.value = (true)
        val apiService = ApiConfig().getApiService()
        val registerUser = apiService.register(name, email, password)
        registerUser.enqueue(object: Callback<RegisterResponse>{
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                _isLoading.value = (false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    saveUser(DataUser(name, email, password, false))
                    if (responseBody != null && !responseBody.error) {
                        _registerSuccess.postValue(true)
                        Log.e(TAG, "Register Berhasil: ${response.message()}")
                    }
                } else {
                    Log.e(TAG, "Register error: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e(TAG, "Register Gagal: ${t.message.toString()}")
            }

        })
    }


    fun saveUser(dataUser: DataUser) {
        viewModelScope.launch {
            pref.saveUser(dataUser)
        }
    }


    companion object {
        private const val TAG = "RegisterViewModel"
    }

}


