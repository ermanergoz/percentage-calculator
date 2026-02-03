package com.erman.percentagecalculator

import android.app.Application
import com.erman.percentagecalculator.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PercentageCalculatorApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PercentageCalculatorApp)
            modules(appModule)
        }
    }
}
