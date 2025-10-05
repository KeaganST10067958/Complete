package com.keagan.complete.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.keagan.complete.R

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private val googleLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Toast.makeText(this, "Google sign-in failed: ${e.statusCode}", Toast.LENGTH_SHORT).show()
            Log.e("Login", "Google sign-in failed", e)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        // If already logged in, go straight to Main
        auth.currentUser?.let {
            goMain()
            return
        }

        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val btnLogin = findViewById<MaterialButton>(R.id.btnLogin)
        val btnGoogle = findViewById<MaterialButton>(R.id.btnGoogle)
        val tvRegister = findViewById<TextView>(R.id.tvRegister)

        btnLogin.setOnClickListener {
            val email = etEmail.text?.toString()?.trim().orEmpty()
            val pwd = etPassword.text?.toString()?.trim().orEmpty()
            if (email.isEmpty() || pwd.isEmpty()) {
                Toast.makeText(this, "Email and password required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(email, pwd)
                .addOnSuccessListener { goMain() }
                .addOnFailureListener { e ->
                    Toast.makeText(this, e.localizedMessage ?: "Login failed", Toast.LENGTH_SHORT).show()
                }
        }

        btnGoogle.setOnClickListener { startGoogleFlow() }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun startGoogleFlow() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // from google-services.json
            .requestEmail()
            .build()

        val client = GoogleSignIn.getClient(this, gso)
        googleLauncher.launch(client.signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { goMain() }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.localizedMessage ?: "Google auth failed", Toast.LENGTH_SHORT).show()
                Log.e("Login", "Firebase auth with Google failed", e)
            }
    }

    private fun goMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
