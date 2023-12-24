package com.example.finalproject
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener
//
//class ProfileActivity : AppCompatActivity() {
//
//    private lateinit var firebaseAuth: FirebaseAuth
//    private lateinit var database: FirebaseDatabase
//    private lateinit var userReference: DatabaseReference
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_profile)
//
//        firebaseAuth = FirebaseAuth.getInstance()
//        database = FirebaseDatabase.getInstance()
//
//        val userId = firebaseAuth.currentUser?.uid // Mendapatkan UID pengguna saat ini
//
//        userId?.let { uid ->
//            val userRef = database.reference.child("users").child(uid)
//
//            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if (snapshot.exists()) {
//                        val username = snapshot.child("username").getValue(String::class.java)
//                        val email = snapshot.child("email").getValue(String::class.java)
//
//                        // Menampilkan informasi username dan email ke dalam TextView
//                        findViewById<TextView>(R.id.userIdTextView).text = "Username: $username"
//                        findViewById<TextView>(R.id.userEmailTextView).text = "Email: $email"
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    // Handle error saat membaca data dari Firebase
//                    Toast.makeText(this@ProfileActivity, "Failed to read user data.", Toast.LENGTH_SHORT).show()
//                }
//            })
//        }
//        val btnUp: Button = findViewById(R.id.btnUpdate)
//        btnUp.setOnClickListener{
//            val intup = Intent(this, EditProfileActivity::class.java)
//            startActivity(intup)
//        }
//    }
//}
//
//
//


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.model.Upload
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase


class ProfileActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userReference: DatabaseReference
    private lateinit var imageViewProfile: ImageView
    private lateinit var textViewUsername: TextView
    private lateinit var textViewEmail: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // Di dalam metode onCreate atau onResume
        val recyclerView: RecyclerView = findViewById(R.id.recyProfil)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        val imageList: MutableList<String> = mutableListOf()
        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid // Mendapatkan UID pengguna saat ini

        textViewUsername = findViewById(R.id.userIdTextView)
        textViewEmail = findViewById(R.id.userEmailTextView)
        userId?.let { uid ->
            // Mengakses referensi user yang saat ini masuk di tabel 'users'
            val userReference = database.reference.child("users").child(uid)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(userSnapshot: DataSnapshot) {
                    if (userSnapshot.exists()) {
                        val username = userSnapshot.child("username").getValue(String::class.java)
                        val email = userSnapshot.child("email").getValue(String::class.java)

                        // Menampilkan informasi username dan email ke dalam TextView
                        textViewUsername.text = "@$username"
                        textViewEmail.text = "$email"

                        // Memuat daftar gambar pengguna dari tabel 'uploads' berdasarkan UID
                        fetchUserImages(uid)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error saat membaca data pengguna dari Firebase
                    Toast.makeText(this@ProfileActivity, "Failed to read user data.", Toast.LENGTH_SHORT).show()
                }
            })
        }


        val btnUp: Button = findViewById(R.id.btnUpdate)
        btnUp.setOnClickListener{
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
        val btnPost : Button = findViewById(R.id.btnUpload)
        btnPost.setOnClickListener{
            val intupload = Intent(this, UploadActivity::class.java)
            startActivity(intupload)
        }

        val btnEx:Button = findViewById(R.id.explore)
        btnEx.setOnClickListener{
            val intex = Intent(this,TampilActivity::class.java)
            startActivity(intex)
        }

        val btnSe:Button = findViewById(R.id.btnSearch)
        btnSe.setOnClickListener{
            val intea = Intent(this,SearchActivity::class.java)
            startActivity(intea)
        }

    }
    private fun fetchUserImages(uid: String) {
        val uploadsRef = database.reference.child("uploads").orderByChild("s").equalTo(uid)

        uploadsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val imageList: MutableList<String> = mutableListOf()

                for (snapshot in dataSnapshot.children) {
                    val imageUrl = snapshot.child("imageUrl").getValue(String::class.java)
                    imageUrl?.let { imageList.add(it) }
                }

                // Setelah mendapatkan daftar URL gambar, pasang adapter RecyclerView
                val recyclerView: RecyclerView = findViewById(R.id.recyProfil)
                recyclerView.layoutManager = GridLayoutManager(this@ProfileActivity, 3) // Atur layout manager dengan 3 kolom
                val imageAdapter = ImageUserAdapter(this@ProfileActivity, imageList)
                recyclerView.adapter = imageAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error saat mengambil data dari Firebase
                Toast.makeText(this@ProfileActivity, "Failed to read user images.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()

        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid

        userId?.let { uid ->
            userReference = database.reference.child("users").child(uid)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val username = snapshot.child("username").value.toString()
                        val email = snapshot.child("email").value.toString()

                        textViewUsername.text = "@$username"
                        textViewEmail.text = "$email"

                        fetchUserImages(uid) // Memuat kembali daftar gambar pengguna
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ProfileActivity, "Failed to read user data.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}








