package com.keagan.complete.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // You can set a real layout; this is a placeholder screen:
        val tv = android.widget.TextView(this).apply {
            textSize = 20f
            text = "You're in! (MainActivity)\n\nTap to sign out."
            setPadding(48, 120, 48, 48)
            setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }
        setContentView(tv)
    }
}
