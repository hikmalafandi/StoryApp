package com.example.storyapp.api

import com.google.gson.annotations.SerializedName

data class StoriesResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("listStory")
    val listStory: List<Stories>,
    @SerializedName("message")
    val message: String
)

