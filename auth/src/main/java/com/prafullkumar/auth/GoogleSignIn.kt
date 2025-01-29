package com.prafullkumar.auth

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun GoogleSignInButton(onSignInSuccess: (GoogleSignInAccount) -> Unit) {
    val context = LocalContext.current
    val googleSignInHelper = GoogleSignInHelper(
        activity = context as Activity,
        onSignInSuccess = { account ->
            onSignInSuccess(account)
        },
        onSignInFailure = { exception ->
            Log.e("GoogleSignIn", "Sign-in failed: ${exception?.message}")
        }
    )
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        googleSignInHelper.handleSignInResult(result.data)
    }
    Button(onClick = {
        val signInIntent = googleSignInHelper.getSignInIntent()
        googleSignInLauncher.launch(signInIntent)
    }) {
        Text("Sign In with Google")
    }
}

class GoogleSignInHelper(
    private val activity: Activity,
    private val onSignInSuccess: (GoogleSignInAccount) -> Unit,
    private val onSignInFailure: (Exception?) -> Unit
) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val googleSignInClient: GoogleSignInClient

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.web_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(activity, gso)
    }

    fun getSignInIntent(): Intent = googleSignInClient.signInIntent

    fun handleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.result
            checkIfAccountExists(account)
        } catch (e: Exception) {
            onSignInFailure(e)
        }
    }

    private fun checkIfAccountExists(account: GoogleSignInAccount?) {
        account?.let {
            val email = it.email
            if (email != null) {
                auth.fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val signInMethods = task.result?.signInMethods
                            if (signInMethods.isNullOrEmpty()) {
                                // Account does not exist, proceed with registration
                                firebaseAuthWithGoogle(account)
                            } else {
                                // Account exists, handle accordingly
                                onSignInSuccess(account)
                            }
                        } else {
                            onSignInFailure(task.exception)
                        }
                    }
            } else {
                onSignInFailure(null)
            }
        } ?: onSignInFailure(null)
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        account?.let {
            val credential = GoogleAuthProvider.getCredential(it.idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        onSignInSuccess(it)
                    } else {
                        onSignInFailure(task.exception)
                    }
                }
        } ?: onSignInFailure(null)
    }
}