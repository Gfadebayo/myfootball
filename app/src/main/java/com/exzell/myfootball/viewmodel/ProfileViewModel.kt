package com.exzell.myfootball.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.exzell.myfootball.io.Repository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import javax.inject.Inject

class ProfileViewModel : ViewModel() {

    @Inject lateinit var mRepo: Repository

    @Inject lateinit var mAuth: FirebaseAuth

    fun updateProfilePic(result: Uri, callback: (Boolean) -> Unit) {
        mAuth.currentUser?.updateProfile(UserProfileChangeRequest.Builder()
                .setPhotoUri(result).build())!!.apply {
            addOnCompleteListener {
                callback.invoke(isSuccessful)
            }
        }
    }

    fun getProfilePath(): Uri {
        return mAuth.currentUser?.photoUrl ?: Uri.EMPTY
    }

    fun getPhone(): String{
        return mAuth.currentUser?.phoneNumber ?: ""
    }

    fun getEmail(): String{
        return mAuth.currentUser?.email ?: ""
    }

    fun getDisplayName(): String{
        return mAuth.currentUser?.displayName ?: ""
    }

    fun updateDisplayName(newName: String, callback: (Boolean) -> Unit) {
        mAuth.currentUser?.updateProfile(UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                .build())!!.apply {
                    addOnCompleteListener {
                        callback.invoke(isSuccessful)
                    }
        }
    }

    fun updateEmail(newEmail: String, callback: (Boolean) -> Unit) {
        mAuth.currentUser?.updateEmail(newEmail)!!.apply {
            addOnCompleteListener {
                callback.invoke(isSuccessful)
            }
        }
    }

    fun updatePhone(newPhone: String, callback: (Boolean) -> Unit) {
//        mAuth.currentUser?.updatePhoneNumber()!!.apply {
//            addOnCompleteListener {
//                callback.invoke(isSuccessful)
//            }
//        }
    }
}