package com.zest.android.ui.main

import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.zest.android.MainApplication
import com.zest.android.R
import com.zest.android.databinding.ActivityMainBinding
import com.zest.android.di.component.MainComponent
import com.zest.android.util.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var mainComponent: MainComponent
    private lateinit var binding: ActivityMainBinding
    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        binding.splashImageView.animation = zoomIn
        binding.splashImageView.startAnimation(zoomIn)

        Handler().postDelayed({
            main_container.visibility = View.VISIBLE
            splash_container.visibility = View.GONE
        }, SPLASH_DISPLAY_LENGTH)

        setSupportActionBar(binding.mainToolbar)

        supportActionBar?.setDisplayShowTitleEnabled(true)

        // Creation of the main graph using the application graph
        mainComponent = (applicationContext as MainApplication).provideAppComponent().mainComponent().create()

        // Make Dagger instantiate @Inject fields in MainActivity
        mainComponent.inject(this)

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        val navGraphIds = listOf(
            R.navigation.recipes,
            R.navigation.category,
            R.navigation.favorites
        )

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = binding.mainBottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment_container,
            intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->
            setupActionBarWithNavController(navController)
        })

        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val SPLASH_DISPLAY_LENGTH: Long = 5000
    }
}
