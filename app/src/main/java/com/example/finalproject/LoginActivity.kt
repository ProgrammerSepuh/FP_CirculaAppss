package com.example.finalproject

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.finalproject.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val reg : TextView = findViewById(R.id.regis_link)
        reg.setOnClickListener{
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }
        firebaseAuth = FirebaseAuth.getInstance()

        val emailEditText = findViewById<EditText>(R.id.emailEt)
        val passwordEditText = findViewById<EditText>(R.id.passET)
        val loginButton = findViewById<Button>(R.id.btnlogin)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            val intent = Intent(this@LoginActivity, ProfileActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {

                            Toast.makeText(this@LoginActivity, "Login gagal. Periksa kembali email dan password Anda.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {

                Toast.makeText(this@LoginActivity, "Isi email dan password.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

//    override fun onStart() {
//        super.onStart()
//        if (firebaseAuth.currentUser != null) {
//            val intent = Intent(this, com.example.finalproject.MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//    }

