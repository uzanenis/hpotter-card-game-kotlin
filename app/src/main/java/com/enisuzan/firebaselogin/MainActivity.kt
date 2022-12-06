package com.enisuzan.firebaselogin

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.enisuzan.firebaselogin.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val db = Firebase.firestore
        val docRef = db.collection("cards")
        docRef.whereEqualTo("house", "Slytherin")
            .get()
            .addOnSuccessListener { cards ->
                for (card in cards ){
                    println("${card.id} => ${card.data}")
                }
            }
            .addOnFailureListener{
                println("Error: $it")
            }
        fetchData()

    }

    private fun fetchData(){
        val db = Firebase.firestore
        val docRef = db.collection("cards")
        docRef.get()
            .addOnSuccessListener { cards ->
                for (card in cards ){
                    println("${card.id} => ${card.data}")
                }
            }
            .addOnFailureListener{
                println("Error: $it")
            }
    }

}