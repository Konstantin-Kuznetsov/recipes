package com.example.core.app

import android.app.Application
import com.example.core.BuildConfig
import com.example.core.di.CoreComponent
import com.example.core.di.CoreModule
import com.example.core.di.DaggerCoreComponent
import timber.log.Timber

class RecipesApp : Application() {

    companion object {
        lateinit var coreComponent: CoreComponent

        fun coreComponent() = coreComponent
    }

    override fun onCreate() {
        super.onCreate()
        initAppComponent(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initAppComponent(app: RecipesApp) {
        coreComponent = DaggerCoreComponent.builder()
            .coreModule(CoreModule(app))
            .build()
    }
}