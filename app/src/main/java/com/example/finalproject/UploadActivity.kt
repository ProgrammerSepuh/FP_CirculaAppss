package com.example.finalproject

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.model.Upload
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UploadActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var editTextDescription: EditText
    private lateinit var selectedImageUri: Uri
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var storageReference: StorageReference

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
        firebaseAuth = FirebaseAuth.getInstance()

        storageReference = FirebaseStorage.getInstance().reference.child("images")

        imageView = findViewById(R.id.imageView)
        editTextDescription = findViewById(R.id.editTextDescription)

        val buttonChooseImage: Button = findViewById(R.id.buttonChooseImage)
        buttonChooseImage.setOnClickListener {
            openFileChooser()
        }

        val buttonUpload: Button = findViewById(R.id.buttonUpload)
        buttonUpload.setOnClickListener {
            uploadFile()
        }
    }

    private fun openFileChooser() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data!!
            imageView.setImageURI(selectedImageUri)
        }
    }

    private fun getFileExtension(uri: Uri): String {
        val contentResolver = contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ?: ""
    }

    private fun uploadFile() {
        if (::selectedImageUri.isInitialized) {
            val fileReference = storageReference.child(System.currentTimeMillis().toString() + "." + getFileExtension(selectedImageUri))

            fileReference.putFile(selectedImageUri)
                .addOnSuccessListener { taskSnapshot ->
                    fileReference.downloadUrl.addOnSuccessListener { uri ->
                        val currentUserId = firebaseAuth.currentUser?.uid ?: ""
                        val currentUser = firebaseAuth.currentUser

                        // Ambil referensi ke tabel 'users'
                        val usersRef = FirebaseDatabase.getInstance().reference.child("users")

                        // Dapatkan 'username' dari tabel 'users' berdasarkan 'userId' saat ini
                        usersRef.child(currentUserId).addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(userSnapshot: DataSnapshot) {
                                val username = userSnapshot.child("username").getValue(String::class.java)

                                val uploadData = Upload(
                                    uri.toString(),
                                    editTextDescription.text.toString(),
                                    currentUserId,
                                    username ?: "" // Menggunakan 'username' dari tabel 'users'
                                )

                                val databaseReference = FirebaseDatabase.getInstance().reference.child("uploads")
                                val uploadId = databaseReference.push().key
                                if (uploadId != null) {
                                    databaseReference.child(uploadId).setValue(uploadData)
                                }

                                Toast.makeText(this@UploadActivity, "Upload berhasil", Toast.LENGTH_SHORT).show()

                                val intent = Intent(this@UploadActivity, ProfileActivity::class.java)
                                startActivity(intent)
                                finish()
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle error
                            }
                        })
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Gagal mengunggah gambar: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("UploadError", "Gagal mengunggah gambar: ${e.message}", e)
                }
        } else {
            Toast.makeText(this, "Tidak ada file yang dipilih", Toast.LENGTH_SHORT).show()
        }
    }
}