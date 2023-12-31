package com.example.finalproject


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
import com.squareup.picasso.Picasso


class ProfileActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userReference: DatabaseReference
//    private lateinit var imageViewProfile: ImageView
    private lateinit var textViewUsername: TextView
    private lateinit var textViewEmail: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

//        val cardView: CardView = findViewById(R.id.cardView)
        val profilImageView: ImageView = findViewById(R.id.profil)

        val recyclerView: RecyclerView = findViewById(R.id.recyProfil)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        val imageList: MutableList<String> = mutableListOf()
        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid // Mendapatkan UID pengguna saat ini

        textViewUsername = findViewById(R.id.userIdTextView)
        textViewEmail = findViewById(R.id.userEmailTextView)
        userId?.let { uid ->
            val userReference = database.reference.child("users").child(uid)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(userSnapshot: DataSnapshot) {
                    if (userSnapshot.exists()) {

                        val profileImageUrl = userSnapshot.child("profileImageUrl").getValue(String::class.java)


                        profileImageUrl?.let {
                            Picasso.get().load(it).into(profilImageView)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                    Toast.makeText(this@ProfileActivity, "Failed to read user data.", Toast.LENGTH_SHORT).show()
                }
            })
        }


        val btnUp: Button = findViewById(R.id.btnUpdate)
        btnUp.setOnClickListener{
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
        val btnPost : ImageView = findViewById(R.id.btnUpload)
        btnPost.setOnClickListener{
            val intupload = Intent(this, UploadActivity::class.java)
            startActivity(intupload)
        }

        val btnEx:ImageView = findViewById(R.id.explore)
        btnEx.setOnClickListener{
            val intex = Intent(this,TampilActivity::class.java)
            startActivity(intex)
        }

        val btnSe:ImageView = findViewById(R.id.btnSearch)
        btnSe.setOnClickListener{
            val intea = Intent(this,SearchActivity::class.java)
            startActivity(intea)
        }

        val btnHom:ImageView = findViewById(R.id.home)
        btnHom.setOnClickListener{
            val intttt = Intent(this,HomeActivity::class.java)
            startActivity(intttt)
        }

    }
    private fun fetchUserImages(uid: String) {
        val uploadsRef = database.reference.child("uploads").orderByChild("s").equalTo(uid)

        uploadsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val imageList: MutableList<String> = mutableListOf()
                val descriptionList: MutableList<String> = mutableListOf()

                for (snapshot in dataSnapshot.children) {
                    val imageUrl = snapshot.child("imageUrl").getValue(String::class.java)
                    val description = snapshot.child("imageDescription").getValue(String::class.java)
                    imageUrl?.let {
                        imageList.add(it)
                        descriptionList.add(description ?: "")
                    }
                }


                val recyclerView: RecyclerView = findViewById(R.id.recyProfil)
                recyclerView.layoutManager = GridLayoutManager(this@ProfileActivity, 3)
                val imageAdapter = ImageUserAdapter(this@ProfileActivity, imageList, descriptionList)
                recyclerView.adapter = imageAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {

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

                        fetchUserImages(uid)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ProfileActivity, "Failed to read user data.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}