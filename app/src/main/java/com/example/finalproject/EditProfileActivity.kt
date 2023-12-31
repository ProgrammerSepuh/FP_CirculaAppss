package com.example.finalproject

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var profileImageUri: Uri

    private lateinit var getContent: ActivityResultLauncher<Intent>
    private val PICK_IMAGE_REQUEST = 1

    private fun updateProfileImage(profileImageView: CircleImageView, imageUrl: String?) {
        if (imageUrl != null && imageUrl.isNotEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.blankuser)
                .into(profileImageView)
        } else {
            profileImageView.setImageResource(R.drawable.blankuser)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid


        userId?.let { uid ->
            userReference = database.reference.child("users").child(uid)
//            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if (snapshot.exists()) {
//                        val username = snapshot.child("username").value.toString()
//                        val profileImageUrl = snapshot.child("profileImageUrl").value.toString()
//
//                        findViewById<EditText>(R.id.editUsername).setText(username)
//                        displayProfileImage(profileImageUrl)
//                    }
//                }

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val username = snapshot.child("username").value.toString()
                        val profileImageUrl = snapshot.child("profileImageUrl").value.toString()


                        val editUsername: EditText = findViewById(R.id.editUsername)
                        editUsername.setText(username)


                        val profileImageView: CircleImageView = findViewById(R.id.profile)
                        updateProfileImage(profileImageView, profileImageUrl)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@EditProfileActivity,
                        "Failed to read user data.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

        val btnSave: Button = findViewById(R.id.buttonUpdate)
        btnSave.setOnClickListener {
            val updatedUsername = findViewById<EditText>(R.id.editUsername).text.toString()


            userReference.child("username").setValue(updatedUsername)
                .addOnSuccessListener {
                    Toast.makeText(this@EditProfileActivity, "Username updated successfully.", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this@EditProfileActivity, "Failed to update username.", Toast.LENGTH_SHORT).show()
                }
        }


        getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedImageUri: Uri? = result.data?.data
                selectedImageUri?.let {
                    uploadImage(it)
                }
            }
        }


        val editFoto: ImageView = findViewById(R.id.editFoto)
        editFoto.setOnClickListener {
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            if (selectedImageUri != null) {

                val profileImageView: CircleImageView = findViewById(R.id.profile)
                profileImageView.setImageURI(selectedImageUri)


                uploadImage(selectedImageUri)
            }
        }
    }

    private fun uploadImage(imageUri: Uri) {
        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            val storageReference = FirebaseStorage.getInstance().reference.child("profile_images/$userId")

            storageReference.putFile(imageUri)
                .addOnSuccessListener {
                    storageReference.downloadUrl.addOnSuccessListener { uri ->
                        // Update the user's profileImageUrl in the Firebase Realtime Database
                        userReference.child("profileImageUrl").setValue(uri.toString())
                    }
                }
                .addOnFailureListener {

                    Toast.makeText(this, "Failed to upload image.", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun displayProfileImage(imageUrl: String) {
        val profileImageView: CircleImageView = findViewById(R.id.profile)

        profileImageView.setImageURI(Uri.parse(imageUrl))
    }
}
