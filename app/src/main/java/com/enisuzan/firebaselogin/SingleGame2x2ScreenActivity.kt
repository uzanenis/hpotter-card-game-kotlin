package com.enisuzan.firebaselogin

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Base64
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.enisuzan.firebaselogin.databinding.ActivitySingleGame2x2ScreenBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_single_game2x2_screen.*

class SingleGame2x2ScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySingleGame2x2ScreenBinding
    private lateinit var buttons: List<ImageButton>
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySingleGame2x2ScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        buttons = listOf(imageButton1, imageButton2, imageButton3, imageButton4)
        database = Firebase.database.reference.child("cards")

        fun writeNewCard(name: String, house: String, score: String, image: String) {
            val card = Card(name, house, score, image, false, false)
            val cardId = database.push().key
            database.child(cardId.toString()).setValue(card)
        }
        /*writeNewCard(
            "Sybill Trelawney",
            "Ravenclaw",
            "14",
            "") */

        val cardListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var items: ArrayList<Card> = arrayListOf()
                var items2: ArrayList<Card> = arrayListOf()
                var currentCard: Int? = null
                var currentTime: Long? = null
                var playerScore: Double? = 0.0
                var houseScores: HashMap<String, Double> = hashMapOf(
                    "Slytherin" to 2.0,
                    "Gryffindor" to 2.0,
                    "Ravenclaw" to 1.0,
                    "Hufflepuff" to 1.0
                )

                println("Firebase ile veriler yüklendi!!")

                for (postSnapshot in snapshot.children) {
                    val card = postSnapshot.getValue<Card>()

                    if (card != null) {
                        items.add(card.copy())
                        items2.add(card.copy())
                    }

                }
                var screenItems: ArrayList<Card> = arrayListOf()
                items.shuffle()
                for (item in 0..1) {
                    screenItems.add(items[item].copy())
                    screenItems.add(items[item].copy())
                }
                screenItems.shuffle()
                //Sayaç
                val timer = object : CountDownTimer(45000, 1000) {
                    override fun onTick(p0: Long) {
                        val seconds = p0 / 1000
                        currentTime = seconds
                        binding.timeCounter.text = "00:$seconds"
                    }

                    override fun onFinish() {
                        Toast.makeText(
                            this@SingleGame2x2ScreenActivity,
                            "Süre Bitti!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
                timer.start()

                //Kartları resetleme
                fun resetUi() {
                    for (card in screenItems) {
                        if (!card.isMatched!!) {
                            card.isFaceUp = false
                        }
                    }
                }

                //Eşleşme kontrolü
                fun checkMatch(card1: Int, card2: Int) {
                    fun String.trimAndDouble() = trim().toDouble()
                    if (screenItems[card1].name == screenItems[card2].name) {
                        val houseScore: Double =
                            if (screenItems[card1].house.toString() == "Gryffindor"
                                || screenItems[card1].house.toString() == "Slytherin"
                            ) 2.0 else 1.0;
                        Toast.makeText(
                            this@SingleGame2x2ScreenActivity,
                            "Cards matched! Good job!",
                            Toast.LENGTH_SHORT
                        ).show()
                        screenItems[card1].isMatched = true
                        screenItems[card2].isMatched = true
                        playerScore =
                            playerScore?.plus((2.0 * (screenItems[card1].score?.trimAndDouble()!!) * houseScore) * (currentTime?.toDouble()!! / 10))
                        binding.playerOnePoints.text = playerScore.toString()
                    } else {
                        if (screenItems[card1].house == screenItems[card2].house) {
                            val houseScore: Double =
                                if (screenItems[card1].house.toString() == "Gryffindor"
                                    || screenItems[card1].house.toString() == "Slytherin"
                                ) 2.0 else 1.0;
                            playerScore = playerScore?.minus(
                                ((screenItems[card1].score?.trimAndDouble()!! +
                                        screenItems[card2].score?.trimAndDouble()!!) / houseScore) *
                                        ((45.0 - currentTime?.toDouble()!!) / 10)
                            )
                            binding.playerOnePoints.text = playerScore.toString()

                        } else {
                            val houseScore1: Double? =
                                if (screenItems[card1].house.toString() == "Gryffindor"
                                    || screenItems[card1].house.toString() == "Slytherin"
                                ) 2.0 else 1.0;
                            val houseScore2: Double? =
                                if (screenItems[card2].house.toString() == "Gryffindor"
                                    || screenItems[card1].house.toString() == "Slytherin"
                                ) 2.0 else 1.0;
                            playerScore = playerScore?.minus(
                                ((screenItems[card1].score?.trimAndDouble()!! +
                                        screenItems[card2].score?.trimAndDouble()!!) / 2) * (houseScore1!! * houseScore2!!) *
                                        ((45.0 - currentTime?.toDouble()!!) / 10)
                            )
                            binding.playerOnePoints.text = playerScore.toString()

                        }
                    }
                }

                //Cardların durumlarını kontrol etme
                fun updateCards(position: Int) {
                    val card = screenItems[position]
                    println("${card.name} -> ${card.house}")
                    if (card.isFaceUp == true) {
                        Toast.makeText(
                            this@SingleGame2x2ScreenActivity,
                            "Hatalı oynama!",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }

                    if (currentCard == null) {
                        currentCard = position
                        resetUi()
                    } else {
                        checkMatch(currentCard!!, position)
                        currentCard = null
                    }

                    card.isFaceUp = !card.isFaceUp!!

                }

                //Cardları güncelleme
                fun updateUi() {
                    screenItems.forEachIndexed { index, card ->
                        val button = buttons[index]
                        if (card.isMatched == true) {
                            button.alpha = 0.3f
                        }
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

                buttons.forEachIndexed { index, button ->
                    button.setOnClickListener {
                        updateCards(index)
                        updateUi()
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", error.toException())
            }
        }
        database.addValueEventListener(cardListener)
        //val difficulty = intent.getStringExtra("Difficulty")
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
