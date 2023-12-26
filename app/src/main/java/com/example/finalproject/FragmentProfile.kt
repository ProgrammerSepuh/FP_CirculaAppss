package com.example.finalproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FragmentProfile : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userReference: DatabaseReference
    private lateinit var textViewUsername: TextView
    private lateinit var textViewEmail: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        textViewUsername = view.findViewById(R.id.userIdTextView)
        textViewEmail = view.findViewById(R.id.userEmailTextView)
        // Lakukan inisialisasi view lainnya sesuai kebutuhan

        // Di dalam metode onCreateView
        val recyclerView: RecyclerView = view.findViewById(R.id.recyProfil)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid

        userId?.let { uid ->
            val userReference = database.reference.child("users").child(uid)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(userSnapshot: DataSnapshot) {
                    if (userSnapshot.exists()) {
                        val username = userSnapshot.child("username").getValue(String::class.java)
                        val email = userSnapshot.child("email").getValue(String::class.java)

                        textViewUsername.text = "@$username"
                        textViewEmail.text = "$email"

                        fetchUserImages(uid)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error saat membaca data dari Firebase
                }
            })
        }

        // ... bagian lain dari kode yang memuat daftar gambar pengguna ke RecyclerView ...

        return view
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

                val recyclerView: RecyclerView = requireView().findViewById(R.id.recyProfil)
                recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
                val imageAdapter = ImageUserAdapter(requireContext(), imageList, descriptionList)
                recyclerView.adapter = imageAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error saat mengambil data dari Firebase
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentProfile().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
