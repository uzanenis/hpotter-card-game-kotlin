package com.enisuzan.firebaselogin

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.enisuzan.firebaselogin.databinding.ActivitySingleGameScreenBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_single_game_screen.*
import kotlin.reflect.typeOf

class SingleGameScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySingleGameScreenBinding
    private lateinit var buttons: List<ImageButton>
    private lateinit var database: DatabaseReference
    private val card: MutableList<Card> = mutableListOf()

    /*private val dataList = arrayListOf<Map<String, Any>>()
    private val imageList = ArrayList<Bitmap>()*/


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySingleGameScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        buttons = listOf(imageButton1, imageButton2, imageButton3, imageButton4)
        database = Firebase.database.reference.child("cards")
        var hashMap: HashMap<String, String> = HashMap<String, String>()
        var bitmapArray: ArrayList<Bitmap> = ArrayList<Bitmap>()

        fun writeNewCard(key: String, name: String, house: String, score: String, image: String) {
            val card = Card(name, house, score, image, false, false)

            database.child(key).setValue(card)
        }
        //writeNewCard("6","de","ne","12","i")


        val cardListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var items: ArrayList<Card> = arrayListOf()
                var items2: ArrayList<Card> = arrayListOf()


                for (postSnapshot in snapshot.children) {
                    val card = postSnapshot.getValue<Card>()
                    val card2 = postSnapshot.getValue<Card>()
                    if (card != null) {
                        items.add(card.copy())
                        items2.add(card.copy())
                    }

                    val cardHashMap = postSnapshot.value as HashMap<*, *>
                    cardHashMap.map { (key, value) ->
                        //println("$key -> $value")
                    }
                    //println("-----")

                }
                var screenItems: ArrayList<Card> = arrayListOf()
                items.shuffle()
                for (item in 0..1) {
                    screenItems.add(items[item].copy())
                    screenItems.add(items[item].copy())
                }
                screenItems.shuffle()
                screenItems.forEach {
                    println(it)
                }

                buttons.forEachIndexed { index, button ->
                    button.setOnClickListener {
                        val card = screenItems[index]
                        card.isFaceUp = !card.isFaceUp!!
                        if(card.isFaceUp!!){
                            val imageBytes = Base64.decode(screenItems[index].image, Base64.DEFAULT)
                            val decodedImage =
                                BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                            button.setImageBitmap(decodedImage)
                        }else{
                            button.setImageResource(R.drawable.cardfront)
                        }

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", error.toException())
            }
        }
        database.addValueEventListener(cardListener)


        val difficulty = intent.getStringExtra("Difficulty")


    }
    /*private fun fetchImages () {
        val db = Firebase.firestore
        // Create a list to store the data
        val docRef = db.collection("cards")
        docRef
            .get()
            .addOnSuccessListener { cards ->
                for (card in cards ){
                    println("${card.id} -> ${card.data}")

                }
            }
            .addOnFailureListener{
                println("Error: $it")
            }
    } */

    private fun decodePicString(encodedString: String): Bitmap {
        if (encodedString.isNotEmpty()) {
            val imageBytes = Base64.decode(encodedString, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            return decodedImage

        } else {
            println("Error while decoding")
            val imageBytes = Base64.decode(encodedString, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        }

    }
}
