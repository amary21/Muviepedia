package com.ian.junemon.spe_learning_mvvm

import android.app.Application
import com.ian.junemon.spe_learning_mvvm.di.allVmModule
import com.ian.junemon.spe_learning_mvvm.di.networkMod
import com.ian.junemon.spe_learning_mvvm.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 *
Created by Ian Damping on 06/07/2019.
Github = https://github.com/iandamping
 */
class Apps : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Apps)
            modules(listOf(networkMod, repositoryModule,allVmModule))
        }
    }
}