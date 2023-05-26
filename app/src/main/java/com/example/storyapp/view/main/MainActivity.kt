package com.example.storyapp.view.main

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.model.UserPreference
import com.example.storyapp.view.upload.UploadActivity
import com.example.storyapp.view.welcome.WelcomeActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var listStoryAdapter: ListStoryAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, com.google.android.material.R.color.mtrl_btn_transparent_bg_color)))

        listStoryAdapter = ListStoryAdapter(emptyList())

        setupViewModel()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rvListStory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = listStoryAdapter
        }
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        ).get(MainViewModel::class.java)
        mainViewModel.isLoading.observe(this, {
            showLoading(it)
        })
        mainViewModel.getUser().observe(this, { user ->
            if (user.isLogin){
                mainViewModel.getStories(1, 20)

                mainViewModel.stories.observe(this, {stories ->
                    if (stories.isNotEmpty()) {
                        binding.rvListStory.visibility = View.VISIBLE
                        listStoryAdapter.setData(stories)
                    }
                })
            } else {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        })


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.logout -> {
                mainViewModel.logout()
                finish()
                return true
            }
            R.id.addStory -> {
                startActivity(Intent(this@MainActivity, UploadActivity::class.java))
                return true
            }
            R.id.languages -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                return true
            }
            else -> {
                return true
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}
