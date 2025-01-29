package com.prafullkumar.codeforge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.prafullkumar.HomeScreen
import com.prafullkumar.HomeViewModel
import com.prafullkumar.auth.AuthScreen
import com.prafullkumar.codeforge.ui.theme.CodeForgeTheme
import com.prafullkumar.commons.profileCreation.DevSocialMediaOnboarding
import com.prafullkumar.home.R.drawable.baseline_group_24
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CodeForgeTheme {
                val navController = rememberNavController()
                NavGraph(navController)
            }
        }
    }
}

sealed interface Features {
    @Serializable
    data object Auth : Features

    @Serializable
    data object Home : Features

    @Serializable
    data object ProfileSetup : Features

    @Serializable
    data object Profile : Features

    @Serializable
    data object Projects : Features

    @Serializable
    data object AddPost : Features

    @Serializable
    data object Network : Features
}

@Composable
fun NavGraph(navController: NavHostController) {
    val pref = getKoin().get<SharedPrefManager>()
    val startDestination =
        if (pref.isFirstTimeCompleted()) Features.Home else if (FirebaseAuth.getInstance().currentUser == null) Features.Auth else Features.ProfileSetup
    val viewModels = rememberSaveable {
        mutableMapOf<Any, ViewModel>()
    }
    NavHost(startDestination = startDestination, navController = navController) {
        composable<Features.Auth> {
            AuthScreen {
                navController.navigate(Features.ProfileSetup)
            }
        }
        composable<Features.Home> {
            MainScreen(
                navController = navController,
                startDestination = Features.Home,
                viewModels = viewModels
            )
        }
        composable<Features.Profile> {
            MainScreen(
                navController = navController,
                startDestination = Features.Profile,
                viewModels = viewModels
            )
        }
        composable<Features.AddPost> {
            MainScreen(
                navController = navController,
                startDestination = Features.AddPost,
                viewModels = viewModels
            )
        }
        composable<Features.Network> {
            MainScreen(
                navController = navController,
                startDestination = Features.Network,
                viewModels = viewModels
            )
        }
        composable<Features.ProfileSetup> {
            DevSocialMediaOnboarding(koinViewModel()) {
                pref.setFirstTimeCompleted()
                navController.navigate(Features.Home)
            }
        }
    }
}

@Composable
fun MainScreen(
    navController: NavHostController, startDestination: Any, viewModels: MutableMap<Any, ViewModel>
) {
    Scaffold(bottomBar = {
        NavigationBar {
            NavigationBarItem(icon = {
                Icon(
                    Icons.Filled.Home, contentDescription = "Profile"
                )
            },
                label = { Text("Home") },
                selected = startDestination == Features.Home,
                onClick = { navController.navigate(Features.Profile) })
            NavigationBarItem(icon = {
                Icon(
                    Icons.Filled.Add, contentDescription = "Profile"
                )
            },
                label = { Text("Add Post") },
                selected = startDestination == Features.AddPost,
                onClick = { navController.navigate(Features.AddPost) })
            NavigationBarItem(icon = {
                Icon(
                    ImageVector.vectorResource(baseline_group_24),
                    contentDescription = "My Network"
                )
            },
                label = { Text("My Network") },
                selected = startDestination == Features.Network,
                onClick = { navController.navigate(Features.Network) })
            NavigationBarItem(icon = {
                Icon(
                    Icons.Filled.Person, contentDescription = "Profile"
                )
            },
                label = { Text("Profile") },
                selected = startDestination == Features.Profile,
                onClick = { navController.navigate(Features.Profile) })
        }

    }) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues), contentAlignment = Alignment.Center
        ) {
            when (startDestination) {
                Features.Home -> {
                    val viewModel = viewModels.getOrPut(Features.Home) {
                        koinViewModel<HomeViewModel>()
                    } as HomeViewModel
                    HomeScreen(viewModel)
                }

                Features.AddPost -> {
                    Box {
                        Text("Add Post")
                    }
                }

                Features.Network -> {
                    Box {
                        Text("Network")
                    }
                }

                Features.Profile -> {
                    Box {
                        Text("Profile")
                    }
                }
            }
        }
    }
}