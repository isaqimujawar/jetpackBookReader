package com.maddy.jetpackbookreader.screens.home

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.maddy.jetpackbookreader.repository.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: FireRepository) : ViewModel() {
    private val displayName: String by lazy {
        FirebaseAuth.getInstance().currentUser?.email?.split('@')?.get(0).toString()
    }

    fun getUserDisplayName(): String {
        return if (displayName == "null") "name" else displayName
    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }
}
