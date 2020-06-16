package com.zest.android.ui.category

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zest.android.R
import com.zest.android.data.model.Category
import com.zest.android.databinding.FragmentCategoryBinding
import com.zest.android.ui.main.MainActivity
import com.zest.android.ui.main.OnMainCallback
import javax.inject.Inject
import javax.inject.Provider

/**
 * Display a grid of [Category]s. User can choose to view the subs of each category.
 *
 * Created by ZARA
 */
class CategoryFragment : Fragment(), OnCategoryFragmentInteractionListener {


    private var mAdapter = CategoryAdapter(this)
    private var mCallback: OnMainCallback? = null
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var viewModel: CategoryViewModel
    @Inject
    lateinit var viewModelProvider: Provider<CategoryViewModel>

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
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        (activity as MainActivity).mainComponent.inject(this)

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                    viewModelProvider.get() as T
        }).get(CategoryViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCategoryBinding.inflate(inflater)
        binding.categoryRecyclerView.adapter = mAdapter

        with(viewModel){
            loadCategories()

            isLoading.observe(viewLifecycleOwner, Observer {
                binding.categoryProgressBar.visibility = if (it == true) View.VISIBLE else View.GONE
            })

            categoryList.observe(viewLifecycleOwner, Observer {
                mAdapter.categories = it
            })
        }

        return binding.root
    }

    override fun showSubCategories(category: Category) {
        mCallback?.gotoSubCategories(category)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.category, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }


    override fun onDetach() {
        super.onDetach()
        mCallback = null
        viewModel.dispose()
    }


    companion object {

        private val TAG = CategoryFragment::class.java.name
    }
}
