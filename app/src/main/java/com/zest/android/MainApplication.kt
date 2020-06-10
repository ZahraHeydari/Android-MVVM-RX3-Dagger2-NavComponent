package com.zest.android

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.zest.android.di.component.AppComponent
import com.zest.android.di.component.AppComponentProvider
import com.zest.android.di.component.DaggerAppComponent

/**
 * @Author ZARA.
 */
class MainApplication : Application(), AppComponentProvider {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)

        appComponent = DaggerAppComponent.factory().create(this).also {
            it.inject(this) }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun provideAppComponent(): AppComponent = appComponent

}