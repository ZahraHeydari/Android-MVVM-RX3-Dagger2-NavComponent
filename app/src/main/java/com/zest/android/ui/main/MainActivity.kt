package com.zest.android.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.zest.android.CategoryDirections
import com.zest.android.LifecycleLoggingActivity
import com.zest.android.MainApplication
import com.zest.android.R
import com.zest.android.data.model.Category
import com.zest.android.databinding.ActivityMainBinding
import com.zest.android.di.component.MainComponent
import com.zest.android.util.currentNavigationFragment
import com.zest.android.util.setupWithNavController


/**
 * @Author ZARA.
 */
class MainActivity : LifecycleLoggingActivity(), OnMainCallback {


    lateinit var mainComponent: MainComponent
    private lateinit var binding: ActivityMainBinding
    private var currentNavController: LiveData<NavController>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
                R.navigation.favorites)

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun gotoSubCategories(category: Category) {
        if (findNavController(R.id.nav_host_fragment_container).currentDestination?.id == R.id.categoryFragment) {
            findNavController(R.id.nav_host_fragment_container)
                    .navigate(CategoryDirections.actionCategoryFragmentToSearchFragment(category.title))
        }
    }

    companion object {

        private val TAG = MainActivity::class.java.name

        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}
