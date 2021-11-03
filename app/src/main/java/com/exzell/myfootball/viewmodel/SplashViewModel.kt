package com.exzell.myfootball.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.exzell.myfootball.io.Repository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class SplashViewModel(private val mApplication: Application) : AndroidViewModel(mApplication) {

    @Inject
    lateinit var mAuth: FirebaseAuth

    @Inject
    lateinit var mRepo: Repository


    fun signup(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
         if(it.isSuccessful) signin(email, password, onSuccess, onError)
            else onError.invoke(it.exception?.localizedMessage ?: "")
        }
    }

    fun signin(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                onSuccess.invoke()
                mRepo.saveLoginDetails(email, password)

            }else onError.invoke(it.exception?.localizedMessage ?: "")
        }
    }

    fun signin(){
        mRepo.signin()
    }

    fun canSignIn(): Boolean {
        return mRepo.canSignIn()
    }

    fun retrievePassword(email: String){
        mAuth.sendPasswordResetEmail(email).addOnSuccessListener {
            //make a toast here
        }
    }

    fun changeAutoLoginState(canRemember: Boolean){
        //TODO: Fix remeber me implementation
    }

    fun canRemember() = false
}