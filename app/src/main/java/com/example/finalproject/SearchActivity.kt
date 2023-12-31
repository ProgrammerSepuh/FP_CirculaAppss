package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.model.User
import com.google.firebase.database.*

class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerViewSearch: RecyclerView
    private lateinit var searchUserAdapter: SearchUserAdapter
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        recyclerViewSearch = findViewById(R.id.recyclerViewSearch)
        recyclerViewSearch.layoutManager = LinearLayoutManager(this)

        val userList: MutableList<User> = mutableListOf()

        databaseReference = FirebaseDatabase.getInstance().reference.child("users")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val uid = snapshot.child("uid").getValue(String::class.java)
                    val email = snapshot.child("email").getValue(String::class.java)
                    val username = snapshot.child("username").getValue(String::class.java)

                    if (uid != null && email != null && username != null) {
                        val profileImageUrl = snapshot.child("profileImageUrl").getValue(String::class.java)
                        val user = User(uid, email, username, profileImageUrl)
                        userList.add(user)
                    }
                }

                searchUserAdapter = SearchUserAdapter(userList) { user ->
                    val intent = Intent(this@SearchActivity, UserLainActivity::class.java)
                    intent.putExtra("userId", user.uid) // Mengirim ID pengguna lain ke halaman profil pengguna lain
                    startActivity(intent)
                }
                recyclerViewSearch.adapter = searchUserAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        val btnPost: ImageView = findViewById(R.id.btnUpload)
        btnPost.setOnClickListener {
            val intentUpload = Intent(this, UploadActivity::class.java)
            startActivity(intentUpload)
        }

        val btnEx: ImageView = findViewById(R.id.explore)
        btnEx.setOnClickListener {
            val intentExplore = Intent(this, TampilActivity::class.java)
            startActivity(intentExplore)
        }

        val btnSe: ImageView = findViewById(R.id.btnSearch)
        btnSe.setOnClickListener {
        }

        val btnHom: ImageView = findViewById(R.id.home)
        btnHom.setOnClickListener {
            val intentHome = Intent(this, HomeActivity::class.java)
            startActivity(intentHome)
        }

        val btnPro: ImageView = findViewById(R.id.btnProfile)
        btnPro.setOnClickListener {
            val intentUpload = Intent(this, ProfileActivity::class.java)
            startActivity(intentUpload)
        }
    }
}


