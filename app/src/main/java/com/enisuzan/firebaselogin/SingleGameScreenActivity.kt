package com.enisuzan.firebaselogin

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.ImageButton
import com.enisuzan.firebaselogin.databinding.ActivitySingleGameScreenBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_single_game_screen.*

class SingleGameScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySingleGameScreenBinding
    private lateinit var buttons: List<ImageButton>
    private val dataList = arrayListOf<Map<String, Any>>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySingleGameScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        buttons = listOf(imageButton1,imageButton2,imageButton3,imageButton4)
        fetchImages()

        for (button in buttons){
            button.setOnClickListener{
                dataList.forEach{
                    println(it)
                }

            }
        }
        val difficulty = intent.getStringExtra("Difficulty")



    }
    private fun fetchImages () {
        val db = Firebase.firestore
        // Create a list to store the data
        val docRef = db.collection("cards")
        docRef
            .get()
            .addOnSuccessListener { cards ->
                for (card in cards ){
                    //println("${card.data}")
                    dataList.add(card.data)
                }
            }
            .addOnFailureListener{
                println("Error: $it")
            }
    }
    private fun decodePicString (encodedString: String): Bitmap {
        if(encodedString.isNotEmpty()){
            val imageBytes = Base64.decode(encodedString, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            return decodedImage
        }else{
            println("Error while decoding")
            val imageBytes = Base64.decode(encodedString, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        }

    }
}