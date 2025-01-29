package com.prafullkumar.commons.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.prafullkumar.commons.profileCreation.ProfileCreationRepository
import com.prafullkumar.commons.profileCreation.ProfileCreationRepositoryImpl
import com.prafullkumar.commons.profileCreation.ProfileCreationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val commonModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single<ProfileCreationRepository> { ProfileCreationRepositoryImpl(get(), get()) }
    viewModel { ProfileCreationViewModel(get(), get(), get()) }
}