package com.example.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserLainActivity : AppCompatActivity() {
    private lateinit var userId: String
    private lateinit var usernameTextView: TextView
    private lateinit var emailTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_lain)
        userId = intent.getStringExtra("userId") ?: ""

        usernameTextView = findViewById(R.id.textViewOtherUsername)
        emailTextView = findViewById(R.id.textViewOtherEmail)

        val databaseReference = FirebaseDatabase.getInstance().reference.child("users").child(userId)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val username = dataSnapshot.child("username").getValue(String::class.java)
                val email = dataSnapshot.child("email").getValue(String::class.java)

                usernameTextView.text = username
                emailTextView.text = email
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error saat mengambil data dari Firebase
                Toast.makeText(this@UserLainActivity, "Failed to load user data.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}