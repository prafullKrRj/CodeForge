package com.prafullkumar.codeforge

import android.app.Application
import com.prafullkumar.commons.di.commonModule
import com.prafullkumar.homeModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module


val mainModule = module {
    single { SharedPrefManager(androidContext()) }
}

class CodeForgeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CodeForgeApp)
            androidLogger()
            modules(commonModule, mainModule, homeModule)
        }
    }
}
