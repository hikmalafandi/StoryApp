package com.example.storyapp.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.storyapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val description = intent.getStringExtra(EXTRA_DESCRIPTION)
        val picture = intent.getStringExtra(EXTRA_URL)

        Glide.with(this@DetailActivity)
            .load(picture)
            .into(binding.pictureUser)
        binding.usernameDetail.text = username
        binding.descriptionDetail.text = description

    }

    companion object {
        const val EXTRA_URL = "extra_url"
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_DESCRIPTION = "extra_description"
    }


}