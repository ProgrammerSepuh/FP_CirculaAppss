package com.example.finalproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
// Ganti dengan path yang sesuai untuk model Upload di aplikasi Anda
import com.example.finalproject.model.Upload
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener



class ExploreActivity : AppCompatActivity() {

    private lateinit var recyclerViewExplore: RecyclerView
    private lateinit var exploreAdapter: ExploreAdapter
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore)

        recyclerViewExplore = findViewById(R.id.recyclerViewSearch)
        recyclerViewExplore.layoutManager = GridLayoutManager(this, 1)

        val uploadList: MutableList<Upload> = mutableListOf()

        databaseReference = FirebaseDatabase.getInstance().reference.child("uploads")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val imageUrl = snapshot.child("imageUrl").getValue(String::class.java)
                    val description = snapshot.child("description").getValue(String::class.java)
                    val s = snapshot.child("s").getValue((String::class.java))

                    if (imageUrl != null && description != null && s != null) {
                        val upload = Upload(imageUrl, description,s)
                        uploadList.add(upload)
                    }
                }
                exploreAdapter = ExploreAdapter(uploadList)
                recyclerViewExplore.adapter = exploreAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }
}









