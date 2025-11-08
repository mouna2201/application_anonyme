// ui/main/MainActivity.kt
package com.votrenom.anonymoussocial.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.votrenom.anonymoussocial.R
import com.votrenom.anonymoussocial.databinding.ActivityMainBinding
import com.votrenom.anonymoussocial.ui.auth.LoginActivity
import com.votrenom.anonymoussocial.ui.feed.FeedFragment
import com.votrenom.anonymoussocial.ui.profile.ProfileFragment
import com.votrenom.anonymoussocial.utils.PreferenceManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var prefManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PreferenceManager(this)

        // Vérifier si l'utilisateur est connecté
        if (!prefManager.isLoggedIn()) {
            navigateToLogin()
            return
        }

        setupBottomNavigation()

        // Charger le fragment par défaut (Feed)
        if (savedInstanceState == null) {
            loadFragment(FeedFragment())
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_feed -> {
                    loadFragment(FeedFragment())
                    true
                }
                R.id.nav_profile -> {
                    loadFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}