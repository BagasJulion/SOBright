package com.example.sobright.auth.data

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest

class AuthRepository(application: Application) {

    private val _authState by lazy { MutableLiveData<AuthState>(AuthState.Idle) }
    val authState: LiveData<AuthState> = _authState

    private var _loginState = MutableLiveData(false)
    val loginState = _loginState

    private val auth = FirebaseAuth.getInstance()

    fun handleSignUp(email: String, password: String, name: String) {
        _authState.value = AuthState.Loading

        val profileUpdates = userProfileChangeRequest {
            displayName = name
            photoUri = Uri.parse("https://incontact.com.sg/wp-content/uploads/2021/05/Student-Stress.jpg")
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i(TAG,"Email signup is successful")

                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.i(TAG,"Email Signing is successful")

                                auth.currentUser?.updateProfile(profileUpdates)
                                    ?.addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Log.i(TAG,"Profile signup is successful")
                                            _authState.value = AuthState.Success
                                            auth.signOut()
                                        } else {
                                            task.exception?.let {
                                                Log.i(TAG,"Profile signup failed with error ${it.localizedMessage}")
                                                _authState.value = AuthState.Error(it.localizedMessage)
                                                auth.signOut()
                                            }
                                        }
                                    }
                            } else {
                                task.exception?.let {
                                    Log.i(TAG,"Email Signing failed with error ${it.localizedMessage}")
                                    _authState.value = AuthState.Error(it.localizedMessage)
                                }
                            }
                        }
                } else {
                    task.exception?.let {
                        Log.i(TAG,"Email signup failed with error ${it.localizedMessage}")
                        _authState.value = AuthState.Error(it.localizedMessage)
                    }
                }
            }
    }

    fun handSignIn(email: String, password: String) {
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i(TAG,"Email Signing is successful")
                    _authState.value = AuthState.Success
                } else {
                    task.exception?.let {
                        Log.i(TAG,"Email Signing failed with error ${it.localizedMessage}")
                        _authState.value = AuthState.Error(it.localizedMessage)
                    }
                }
            }
    }

    fun getLoginState() {
        _loginState.value = auth.currentUser != null
    }

    fun getUser() = auth.currentUser

    fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.i(TAG,"Message successfully sent")
            } else {
                it.exception?.let {
                    Log.i(TAG, "Reset Password Error ${it.localizedMessage}")
                    _authState.value = AuthState.Error(it.localizedMessage)
                }
            }
        }
    }

//    fun updateProfile(name: String) {
//
//        val profileUpdates = userProfileChangeRequest {
//            displayName = name
//            photoUri = Uri.parse("https://upload.wikimedia.org/wikipedia/commons/6/68/Joe_Biden_presidential_portrait.jpg")
//        }
//
//        auth.currentUser?.updateProfile(profileUpdates)
//            ?.addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Log.i(TAG,"Profile update is successful")
//                    _authState.value = AuthState.Success
//                } else {
//                    task.exception?.let {
//                        Log.i(TAG,"Profile update failed with error ${it.localizedMessage}")
//                        _authState.value = AuthState.Error(it.localizedMessage)
//                    }
//                }
//            }
//    }

//    fun signOut() = auth.signOut()

    companion object{
        const val TAG = "Auth Repository"
    }
}