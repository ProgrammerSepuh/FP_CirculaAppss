package com.example.finalproject

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
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

        // Menambahkan onClickListener ke tombol-tombol
        val btnPost: ImageView = findViewById(R.id.btnUpload)
        val btnEx: ImageView = findViewById(R.id.explore)
        val btnSe: ImageView = findViewById(R.id.btnSearch)
        val btnHom: ImageView = findViewById(R.id.home)

        btnPost.setOnClickListener {
            val intupload = Intent(this, UploadActivity::class.java)
            startActivity(intupload)
        }

        btnEx.setOnClickListener {
            val intex = Intent(this, TampilActivity::class.java)
            startActivity(intex)
        }

        btnSe.setOnClickListener {
            val intea = Intent(this, SearchActivity::class.java)
            startActivity(intea)
        }

        val btnPro: ImageView = findViewById(R.id.btnProfile)
        btnPro.setOnClickListener {
            val intentUpload = Intent(this, ProfileActivity::class.java)
            startActivity(intentUpload)
        }

        val btnHm: ImageView = findViewById(R.id.home)
        btnHm.setOnClickListener{

        }
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

                        val userRef = database.reference.child("users").child(uid)
                        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(userSnapshot: DataSnapshot) {
                                val username = userSnapshot.child("username").getValue(String::class.java)
                                username?.let {
                                    imageList.add(imageUrl!!)
                                    usernameList.add(username)

                                    setupRecyclerView(imageList, usernameList)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {

                                Toast.makeText(this@HomeActivity, "Failed to load usernames.", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

                Toast.makeText(this@HomeActivity, "Failed to load user images.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRecyclerView(imageList: List<String>, usernameList: List<String>) {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewHome)
        recyclerView.layoutManager = GridLayoutManager(this@HomeActivity, 1)
        val homeImageAdapter = HomeImageAdapter(this@HomeActivity, imageList, usernameList)
        recyclerView.adapter = homeImageAdapter
    }
}
