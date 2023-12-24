package com.example.finalproject

import android.os.Bundle
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

        recyclerViewSearch = findViewById(R.id.recyclerViewSearch) // Mendapatkan referensi ke RecyclerView dari layout XML
        recyclerViewSearch.layoutManager = LinearLayoutManager(this) // Atur layout manager (misalnya LinearLayoutManager)

        val userList: MutableList<User> = mutableListOf()

        databaseReference = FirebaseDatabase.getInstance().reference.child("users")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val uid = snapshot.child("uid").getValue(String::class.java)
                    val email = snapshot.child("email").getValue(String::class.java)
                    val username = snapshot.child("username").getValue(String::class.java)

                    if (uid != null && email != null && username != null) {
                        val user = User(uid, email, username)
                        userList.add(user)
                    }
                }
                // Setelah mendapatkan daftar pengguna, inisialisasikan adapter dan RecyclerView
                searchUserAdapter = SearchUserAdapter(userList)
                recyclerViewSearch.adapter = searchUserAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error saat mengambil data dari Firebase
            }
        })
    }
}

