package com.zest.android.di.component

import com.zest.android.di.ActivityScope
import com.zest.android.ui.detail.DetailActivity
import dagger.Subcomponent


@ActivityScope
@Subcomponent
interface DetailComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): DetailComponent
    }

    fun inject(detailActivity: DetailActivity)
}