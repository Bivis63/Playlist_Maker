package com.example.playlistmaker.main


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navigationController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        navigationController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navigationController)
    }
    fun hideBottomNavigation() {
        val navView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        navView.visibility = View.GONE
    }
    fun openBottomNavigation() {
        val navView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        navView.visibility = View.VISIBLE
    }
}