package com.babe.fata

import android.app.Application
import com.babe.fata.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(AppModule)
        }
    }
}
