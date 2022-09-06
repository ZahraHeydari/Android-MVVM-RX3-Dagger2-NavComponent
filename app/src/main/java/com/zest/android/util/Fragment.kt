package com.zest.android.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Provider

inline fun <reified T : ViewModel> Fragment.viewModelProvider(viewModelProvider: Provider<T>) =
    ViewModelProvider(this, object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            viewModelProvider.get() as T
    })[T::class.java]