package com.zest.android.ui.category

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zest.android.CategoryDirections
import com.zest.android.R
import com.zest.android.data.model.Category
import com.zest.android.databinding.FragmentCategoryBinding
import com.zest.android.ui.main.MainActivity
import com.zest.android.util.viewModelProvider
import javax.inject.Inject
import javax.inject.Provider

/**
 * Display a grid of [Category]s. User can choose to view the subs of each category.
 *
 * Created by ZARA
 */
class CategoryFragment : Fragment(), OnCategoryFragmentInteractionListener {

    private var mAdapter = CategoryAdapter(this)
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var viewModel: CategoryViewModel

    @Inject
    lateinit var viewModelProvider: Provider<CategoryViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).mainComponent.inject(this)
        viewModel = this.viewModelProvider(viewModelProvider)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(inflater)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.categoryRecyclerView.adapter = mAdapter

        with(viewModel) {
            loadCategories()

            isLoading.observe(viewLifecycleOwner) {
                binding.categoryProgressBar.visibility = if (it == true) View.VISIBLE else View.GONE
            }

            categoryList.observe(viewLifecycleOwner) {
                mAdapter.categories = it
            }
        }
        return binding.root
    }

    override fun showSubCategories(category: Category) {
        if (findNavController().currentDestination?.id == R.id.categoryFragment) {
            findNavController().navigate(CategoryDirections.actionCategoryFragmentToSearchFragment(category.title))
        }
    }

    override fun onDetach() {
        super.onDetach()
        viewModel.dispose()
    }
}
