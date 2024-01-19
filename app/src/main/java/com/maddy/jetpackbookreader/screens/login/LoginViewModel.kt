package com.maddy.jetpackbookreader.screens.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "ReaderFirebase"

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        showHomeScreen: () -> Unit
    ) =
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {task ->
                        when {
                            task.isSuccessful -> {
                                Log.d("ReaderFirebase", "signIn Successful")
                                showHomeScreen()
                            }
                            task.isCanceled -> {
                                Log.d("ReaderFirebase", "signIn Canceled: ${task.result}")
                            }
                        }
                    }
                    .addOnFailureListener {
                        Log.d("ReaderFirebase", "signIn Failure: ${it.localizedMessage}")
                    }
            } catch (e: Exception) {
                Log.d(TAG, "signIn Exception: ${e.localizedMessage}")
            }
        }

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        showHomeScreen: () -> Unit
    ) {
        if (_loading.value == true) return

        try {
            _loading.value = true
            viewModelScope.launch {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        when {
                            task.isSuccessful -> {
                                Log.d("ReaderFirebase", "create user Successful: ${task.result}")
                                showHomeScreen()
                            }
                            task.isCanceled -> {
                                Log.d("ReaderFirebase", "create user Canceled: ${task.result}")
                            }
                        }
                    }
                    .addOnFailureListener {
                        Log.d("ReaderFirebase", "create user Failure: ${it.localizedMessage}")
                    }
            }
            _loading.value = false
        } catch (e: Exception) {
            Log.d(TAG, "create user Exception: ${e.localizedMessage}")
        }
    }
}