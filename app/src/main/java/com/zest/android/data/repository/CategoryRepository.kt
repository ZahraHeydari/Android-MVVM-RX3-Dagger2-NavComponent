package com.zest.android.data.repository

import com.zest.android.data.source.local.AppDatabase
import com.zest.android.data.model.CategoryResponse
import com.zest.android.data.source.remote.APIResponse
import com.zest.android.data.source.remote.APIService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * To handle data operations. It provides a clean API so that the rest of the app can retrieve this data easily.
 * It knows where to get the data from and what API calls to make when data is updated.
 * You can consider repositories to be mediators between different data sources, such as persistent models,
 * web services, and caches.
 *
 * @Author ZARA.
 */
class CategoryRepository @Inject constructor(private var apiService: APIService,
                                             var appDatabase: AppDatabase) {


    fun loadRootCategories(
            compositeDisposable: CompositeDisposable,
            onResponse: APIResponse<CategoryResponse>
    ): io.reactivex.rxjava3.disposables.Disposable {
        return apiService.categories
                .subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onResponse.onSuccess(it)
                }, {
                    onResponse.onError(it)
                }).also {
                    compositeDisposable.add(it)
                }

    }


    companion object {
        private val TAG = CategoryRepository::class.java.name
    }
}
