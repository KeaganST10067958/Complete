package com.keagan.complete.ui

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.keagan.complete.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val etName = findViewById<TextInputEditText>(R.id.etName)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val etConfirm = findViewById<TextInputEditText>(R.id.etConfirmPassword)
        val btnRegister = findViewById<MaterialButton>(R.id.btnRegister)
        val tvGoLogin = findViewById<TextView>(R.id.tvGoLogin)

        btnRegister.setOnClickListener {
            val name = etName.text?.toString()?.trim().orEmpty()
            val email = etEmail.text?.toString()?.trim().orEmpty()
            val pwd = etPassword.text?.toString()?.trim().orEmpty()
            val confirm = etConfirm.text?.toString()?.trim().orEmpty()

            if (name.isEmpty() || email.isEmpty() || pwd.isEmpty()) {
                Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pwd != confirm) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, pwd)
                .addOnSuccessListener {
                    // Optional: set display name
                    val profileUpdates = com.google.firebase.auth.userProfileChangeRequest {
                        displayName = name
                    }
                    auth.currentUser?.updateProfile(profileUpdates)

                    Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, e.localizedMessage ?: "Registration failed", Toast.LENGTH_SHORT).show()
                }
        }

        tvGoLogin.setOnClickListener {
            finish() // go back to Login
        }
    }
}
