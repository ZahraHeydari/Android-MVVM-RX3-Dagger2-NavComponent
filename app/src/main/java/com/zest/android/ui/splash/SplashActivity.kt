package com.zest.android.ui.splash

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import android.view.animation.AnimationUtils
import com.zest.android.R
import com.zest.android.databinding.ActivitySplashBinding
import com.zest.android.ui.main.MainActivity

/**
 * To display the splash screen
 *
 * @author ZARA
 */
class SplashActivity : AppCompatActivity() {


    private val SPLASH_DISPLAY_LENGTH: Long = 1500
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        binding.splashImageView.animation = zoomIn
        binding.splashImageView.startAnimation(zoomIn)


        Handler().postDelayed(Runnable {
            MainActivity.start(this@SplashActivity)
            finish()
        }, SPLASH_DISPLAY_LENGTH)

    }

}
