package com.zest.android.ui.recipes

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.zest.android.R
import com.zest.android.data.model.Recipe
import com.zest.android.databinding.FragmentRecipesBinding
import com.zest.android.ui.main.MainActivity
import com.zest.android.util.NetworkStateReceiver
import com.zest.android.util.viewModelProvider
import kotlinx.android.synthetic.main.fragment_recipes.*
import javax.inject.Inject
import javax.inject.Provider

/**
 * Display a grid of [Recipe]s. User can choose to view each recipe.
 *
 * Created by ZARA
 */
class RecipesFragment : Fragment(), NetworkStateReceiver.OnNetworkStateReceiverListener,
    OnRecipesFragmentInteractionListener {

    private var mAdapter = RecipesAdapter(this)
    private var mNetworkReceiver = NetworkStateReceiver()
    private lateinit var binding: FragmentRecipesBinding
    private lateinit var viewModel: RecipeViewModel

    @Inject
    lateinit var viewModelProvider: Provider<RecipeViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as MainActivity).mainComponent.inject(this)
        viewModel = this.viewModelProvider(viewModelProvider)

        mNetworkReceiver.addListener(this)
        context?.registerReceiver(
            mNetworkReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipesBinding.inflate(inflater)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        binding.recipesRecyclerView.adapter = mAdapter

        with(viewModel) {
            getMainRecipes()

            recipesData.observe(viewLifecycleOwner) {
                if (it != null) {
                    mAdapter.recipes = it
                }
            }

            isLoading.observe(viewLifecycleOwner) {
                binding.recipesProgressBar.visibility = if (it == true) View.VISIBLE else View.GONE
            }
        }

        mAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                checkEmptyView()
            }
        })

        return binding.root
    }

    override fun showMessage(message: Int) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).setAction("Action", null).show()
    }

    override fun gotoDetailPage(recipe: Recipe) {
        findNavController().navigate(
            RecipesFragmentDirections.actionRecipesFragmentToDetailFragment(recipe)
        )
    }

    override fun loadFavorite(recipe: Recipe): Recipe? {
        return viewModel.loadFavorite(recipe)
    }

    override fun removeFavorite(recipe: Recipe) {
        viewModel.deleteFavoriteFromDB(recipe)
    }

    override fun addOrRemoveFavorites(recipe: Recipe) {
        viewModel.addOrRemoveAsFavorite(recipe)
        mAdapter.notifyDataSetChanged()
    }

    override fun isFavorited(recipe: Recipe): Boolean {
        return viewModel.isFavorited(recipe)
    }

    override fun networkAvailable() {
        viewModel.getMainRecipes()
    }

    override fun networkUnavailable() {
        Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_SHORT).show()
    }

    private fun checkEmptyView() {
        recipes_empty_view.visibility =
            if (mAdapter.itemCount == 0) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                if (findNavController().currentDestination?.id == R.id.recipesFragment) {
                    findNavController().navigate(R.id.action_recipesFragment_to_searchFragment)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        mNetworkReceiver.removeListener(this)
        unregisterNetworkChanges()
    }

    private fun unregisterNetworkChanges() {
        context?.unregisterReceiver(mNetworkReceiver)
    }
}
