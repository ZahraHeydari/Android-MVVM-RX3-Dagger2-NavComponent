package com.zest.android.ui.favorite

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.zest.android.R
import com.zest.android.data.model.Recipe
import com.zest.android.databinding.FragmentFavoriteBinding
import com.zest.android.ui.main.MainActivity
import com.zest.android.ui.main.OnMainCallback
import com.zest.android.ui.recipes.RecipeViewModel
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.fragment_favorite.*
import javax.inject.Inject
import javax.inject.Provider

/**
 * Display a grid of Favorite [Recipe]s. User can choose to view each favorite recipe.
 *
 * Created by ZARA
 */
class FavoriteFragment : Fragment(), OnFavoriteFragmentInteractionListener {

    private var mCallback: OnMainCallback? = null
    private lateinit var binding: FragmentFavoriteBinding
    private var mAdapter = FavoriteAdapter(this)
    private lateinit var viewModel: RecipeViewModel

    @Inject
    lateinit var viewModelProvider: Provider<RecipeViewModel>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnMainCallback) {
            mCallback = context
        } else {
            throw ClassCastException("$context must implement OnMainCallback!")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        (activity as MainActivity).mainComponent.inject(this)

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModelProvider.get() as T
        })[RecipeViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFavoriteBinding.inflate(inflater)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.favoriteRecyclerView.adapter = mAdapter

        mAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                checkEmptyView()
            }
        })

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(viewModel) {
            loadFavoriteItems()

            recipesData.observe(viewLifecycleOwner) {
                if (it != null) {
                    mAdapter.recipes = it
                }
            }
        }
    }

    private fun checkEmptyView() {
        favorite_empty_view.visibility = if (mAdapter.itemCount == 0) View.VISIBLE else View.GONE
    }

    override fun showDeleteFavoriteDialog(recipe: Recipe) {
        if (context == null) return
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.delete))
                .setMessage(getString(R.string.are_you_sure_want_to_delete_this_item_from_favorite_list))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes)) { dialog, i ->
                    dialog.dismiss()//Cancel the dialog
                    viewModel.deleteFavoriteFromDB(recipe)
                    mAdapter.updateData(recipe)
                    Snackbar.make(binding.root, getString(R.string.deleted_this_recipe_from_your_favorite_list),
                            Snackbar.LENGTH_LONG).setAction("Action", null).show()
                }
                .setNegativeButton(getString(R.string.no)) { dialog, i ->
                    dialog.dismiss()// User cancelled the dialog
                }
        builder.create()
        builder.show()
    }

    override fun isFavorited(recipe: Recipe): Boolean = viewModel.isFavorited(recipe)

    override fun gotoDetailPage(recipe: Recipe) {
        findNavController().navigate(FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(recipe))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorites, menu)
    }

    override fun onDetach() {
        super.onDetach()
        mCallback = null
        viewModel.dispose()
    }
}
