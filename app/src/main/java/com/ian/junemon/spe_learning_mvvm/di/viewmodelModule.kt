package com.ian.junemon.spe_learning_mvvm.di


import com.ian.junemon.spe_learning_mvvm.data.viewmodel.movie.MovieViewmodel
import com.ian.junemon.spe_learning_mvvm.data.viewmodel.tv.TvViewmodel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 *
Created by Ian Damping on 24/06/2019.
Github = https://github.com/iandamping
 */

val allVmModule = module {
    viewModel { MovieViewmodel(get()) }
    viewModel { TvViewmodel(get()) }
}