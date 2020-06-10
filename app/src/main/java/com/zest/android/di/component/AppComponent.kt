package com.zest.android.di.component

import android.app.Application
import com.zest.android.MainApplication
import com.zest.android.di.AppScope
import com.zest.android.di.module.BinderModule
import com.zest.android.di.module.DatabaseModule
import com.zest.android.di.module.NetworkModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@AppScope
@Singleton
@Component(modules = [
    DatabaseModule::class,
    NetworkModule::class,
    BinderModule::class,
    SubComponentsModule::class
])
interface AppComponent {

    fun inject(app: MainApplication)

    fun mainComponent(): MainComponent.Factory
    fun detailComponent(): DetailComponent.Factory


    @Component.Factory
    interface Factory {

        fun create(@BindsInstance app: Application): AppComponent
    }
}