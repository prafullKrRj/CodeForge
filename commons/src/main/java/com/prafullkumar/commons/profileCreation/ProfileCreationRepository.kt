package com.prafullkumar.commons.profileCreation

import com.prafullkumar.commons.BaseClass
import com.prafullkumar.commons.model.user.UserProfile
import kotlinx.coroutines.flow.Flow

interface ProfileCreationRepository {
    fun insertData(userProfile: UserProfile): Flow<BaseClass<Boolean>>
}
