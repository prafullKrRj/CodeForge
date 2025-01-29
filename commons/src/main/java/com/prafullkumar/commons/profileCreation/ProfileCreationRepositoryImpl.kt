package com.prafullkumar.commons.profileCreation

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.prafullkumar.commons.BaseClass
import com.prafullkumar.commons.model.user.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProfileCreationRepositoryImpl(
    private val firebaseAuth: FirebaseAuth, private val fireStore: FirebaseFirestore
) : ProfileCreationRepository {
    private val user: FirebaseUser? = firebaseAuth.currentUser
    override fun insertData(userProfile: UserProfile): Flow<BaseClass<Boolean>> {
        if (user == null) {
            return flow {
                emit(BaseClass.Error(Exception("User not logged in")))
            }
        }
        return flow {
            try {
                Log.d("ProfileCreationRepositoryImpl", "insertData: $userProfile")
                fireStore.collection("users").document(user.uid).set(userProfile)
                emit(BaseClass.Success(true))
            } catch (e: Exception) {
                emit(BaseClass.Error(e))
            }
        }
    }
}