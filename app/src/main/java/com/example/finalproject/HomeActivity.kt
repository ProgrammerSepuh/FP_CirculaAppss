package com.example.finalproject

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.model.Upload
import com.example.finalproject.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var homeImageAdapter: HomeImageAdapter
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerView = findViewById(R.id.recyclerViewHome)
        recyclerView.layoutManager = LinearLayoutManager(this)
        database = FirebaseDatabase.getInstance()
        fetchAllUserImages()
    }

    private fun fetchAllUserImages() {
        val uploadsRef = database.reference.child("uploads")

        uploadsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val imageList: MutableList<String> = mutableListOf()
                val usernameList: MutableList<String> = mutableListOf()

                for (snapshot in dataSnapshot.children) {
                    val uid = snapshot.child("s").getValue(String::class.java)
                    val imageUrl = snapshot.child("imageUrl").getValue(String::class.java)

                    uid?.let {
                        // Ambil username dari tabel users berdasarkan uid
                        val userRef = database.reference.child("users").child(uid)
                        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(userSnapshot: DataSnapshot) {
                                val username = userSnapshot.child("username").getValue(String::class.java)
                                username?.let {
                                    imageList.add(imageUrl!!)
                                    usernameList.add(username)
                                    // Setelah mendapatkan username dan URL gambar, pasang ke dalam RecyclerView
                                    setupRecyclerView(imageList, usernameList)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle error saat mengambil data dari tabel users
                                Toast.makeText(this@HomeActivity, "Failed to load usernames.", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error saat mengambil data dari tabel uploads
                Toast.makeText(this@HomeActivity, "Failed to load user images.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRecyclerView(imageList: List<String>, usernameList: List<String>) {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewHome)
        recyclerView.layoutManager = GridLayoutManager(this@HomeActivity,3)
        val homeImageAdapter = HomeImageAdapter(this@HomeActivity, imageList, usernameList)
        recyclerView.adapter = homeImageAdapter
    }

}






