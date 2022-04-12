package com.sedat.catsapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.sedat.catsapp.databinding.ActivitySplashScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    var logoCatAnim: Animation ?= null
    var appNameAnim: Animation ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        logoCatAnim = AnimationUtils.loadAnimation(this, R.anim.logo_cat_anim)
        appNameAnim = AnimationUtils.loadAnimation(this, R.anim.app_name_anim)

        binding.logoCat.animation = logoCatAnim
        binding.appName.animation = appNameAnim

        val runnable = Runnable {
            val intentToMainActivity = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(intentToMainActivity)
            finish()
        }

        Handler(Looper.getMainLooper()).postDelayed(runnable, 3000)
    }
}