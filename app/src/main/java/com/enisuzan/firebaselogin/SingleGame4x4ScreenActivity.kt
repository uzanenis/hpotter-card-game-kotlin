package com.enisuzan.firebaselogin

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.ImageButton
import com.enisuzan.firebaselogin.databinding.ActivitySingleGame4x4ScreenBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_single_game2x2_screen.*
import kotlinx.android.synthetic.main.activity_single_game2x2_screen.imageButton1
import kotlinx.android.synthetic.main.activity_single_game2x2_screen.imageButton2
import kotlinx.android.synthetic.main.activity_single_game2x2_screen.imageButton3
import kotlinx.android.synthetic.main.activity_single_game2x2_screen.imageButton4
import kotlinx.android.synthetic.main.activity_single_game4x4_screen.*

class SingleGame4x4ScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySingleGame4x4ScreenBinding
    private lateinit var buttons: List<ImageButton>
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleGame4x4ScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        buttons = listOf(
            imageButton1,
            imageButton2,
            imageButton3,
            imageButton4,
            imageButton5,
            imageButton6,
            imageButton7,
            imageButton8,
            imageButton9,
            imageButton10,
            imageButton11,
            imageButton12,
            imageButton13,
            imageButton14,
            imageButton15,
            imageButton16
        )
        database = Firebase.database.reference.child("cards")
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

                    if (card != null) {
                        items.add(card.copy())
                        items2.add(card.copy())
                    }
                }
                var screenItems: ArrayList<Card> = arrayListOf()
                items.shuffle()
                for (item in 0..3) {
                    screenItems.add(items[item].copy())
                    screenItems.add(items[item].copy())
                }
                screenItems.shuffle()

                buttons.forEachIndexed { index, button ->
                    button.setOnClickListener {
                        val card = screenItems[index]
                        card.isFaceUp = !card.isFaceUp!!
                        if (card.isFaceUp!!) {
                            val imageBytes = Base64.decode(card.image, Base64.DEFAULT)
                            val decodedImage =
                                BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                            button.setImageBitmap(decodedImage)
                        } else {
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

    }

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