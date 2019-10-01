package com.ian.junemon.spe_learning_mvvm.data.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

/**
 *
Created by Ian Damping on 24/06/2019.
Github = https://github.com/iandamping
 */
abstract class BaseViewModel : ViewModel() {
    /*Shared viewModelScope*/
    protected val vmScopes = viewModelScope

}