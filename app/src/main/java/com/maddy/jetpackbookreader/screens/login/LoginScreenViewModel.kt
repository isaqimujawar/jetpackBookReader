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

@HiltViewModel
class LoginScreenViewModel @Inject constructor() : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun signInWithEmailAndPassword(email: String, password: String, showHomeScreen: () -> Unit) =
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("ReaderFirebase", "signIn Success: ${task.result}")
                            showHomeScreen()
                        }
                    }
                    .addOnFailureListener {
                        Log.d("ReaderFirebase", "signIn Failure: ${it.localizedMessage}")
                    }
            } catch (e: Exception) {
                Log.d("ReaderFirebase", "signIn Exception: ${e.localizedMessage}")
            }
        }

    fun createUserWithEmailAndPassword() {

    }
}