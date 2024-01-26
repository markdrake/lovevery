package com.lovevery.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {
    // Note: If this is a concern we can always make it private with write access
    // only on main activity
    var searchQuery: MutableLiveData<String> = MutableLiveData(EMPTY_STRING)

    companion object {
        private const val EMPTY_STRING = ""
    }
}