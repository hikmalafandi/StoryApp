package com.example.storyapp.view.signin

import android.util.Log
import androidx.lifecycle.*
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.LoginResponse
import com.example.storyapp.model.DataUser
import com.example.storyapp.model.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreference) : ViewModel() {

    fun getUser(): LiveData<DataUser> {
        return pref.getUser().asLiveData()
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            pref.saveToken(token)
        }
    }

    fun loginUser() {
        viewModelScope.launch {
            pref.login()
        }
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginSuccess = MutableLiveData(false)
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    fun login(email: String, password: String) {
        _isLoading.value = (true)
        val apiService = ApiConfig().getApiService()
        val loginUser = apiService.login(email, password)
        loginUser.enqueue(object: Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = (false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        saveToken(responseBody.loginResult.token)
                        _loginSuccess.postValue(true)
                        Log.e(TAG, "Login Berhasil: ${response.message()}")
                    }
                } else {
                    Log.e(TAG, "Login Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(TAG, "Login Gagal: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}
