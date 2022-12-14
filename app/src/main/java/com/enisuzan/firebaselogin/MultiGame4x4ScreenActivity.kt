package com.enisuzan.firebaselogin

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Base64
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.enisuzan.firebaselogin.databinding.ActivityMultiGame4x4ScreenBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_single_game2x2_screen.imageButton1
import kotlinx.android.synthetic.main.activity_single_game2x2_screen.imageButton2
import kotlinx.android.synthetic.main.activity_single_game2x2_screen.imageButton3
import kotlinx.android.synthetic.main.activity_single_game2x2_screen.imageButton4
import kotlinx.android.synthetic.main.activity_single_game4x4_screen.*
import java.io.File

class MultiGame4x4ScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMultiGame4x4ScreenBinding
    private lateinit var buttons: List<ImageButton>
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiGame4x4ScreenBinding.inflate(layoutInflater)
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
        var mediaPlayer = MediaPlayer.create(this@MultiGame4x4ScreenActivity, R.raw.song1wholegame)
        mediaPlayer.start()
        fun writeNewCard(key: String, name: String, house: String, score: String, image: String) {
            val card = Card(name, house, score, image, false, false)

            database.child(key).setValue(card)
        }
        //writeNewCard("6","de","ne","12","i")

        val cardListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var items: ArrayList<Card> = arrayListOf()
                var currentCard: Int? = null
                var currentTime: Long? = null
                var playerScore1: Double? = 0.0
                var playerScore2: Double? = 0.0
                var matchedCount: Int = 0
                var turn = true
                //Player two text opacity
                binding.playerTwo.alpha = 0.3f
                binding.playerTwoPoints.alpha = 0.3f



                println("Firebase ile veriler y??klendi!!")

                for (postSnapshot in snapshot.children) {
                    val card = postSnapshot.getValue<Card>()

                    if (card != null) {
                        items.add(card.copy())
                    }

                }
                var screenItems: ArrayList<Card> = arrayListOf()
                items.shuffle()
                var count1: Int = 0
                var count2: Int = 0
                var count3: Int = 0
                var count4: Int = 0
                items.forEach {
                    if (count1 < 2) {
                        if (it.house!!.lowercase() == "gryffindor") {
                            screenItems.add(it.copy())
                            screenItems.add(it.copy())
                            count1 += 1
                        }
                    }
                    if (count2 < 2) {
                        if (it.house!!.lowercase() == "slytherin") {
                            screenItems.add(it.copy())
                            screenItems.add(it.copy())
                            count2 += 1
                        }
                    }
                    if (count3 < 2) {
                        if (it.house!!.lowercase() == "ravenclaw") {
                            screenItems.add(it.copy())
                            screenItems.add(it.copy())
                            count3 += 1
                        }
                    }
                    if (count4 < 2) {
                        if (it.house!!.lowercase() == "hufflepuff") {
                            screenItems.add(it.copy())
                            screenItems.add(it.copy())
                            count4 += 1
                        }
                    }

                }
                /*for (item in 0..7) {
                    screenItems.add(items[item].copy())
                    screenItems.add(items[item].copy())
                }*/
                screenItems.shuffle()
                File(applicationContext.filesDir,"data.txt").printWriter().use { out ->
                    screenItems.forEachIndexed { index, card ->
                        out.println("$index -> ${card.name} -> ${card.house} -> ${card.score}")
                    }
                }
                //Saya??
                val timer = object : CountDownTimer(60000, 1000) {
                    override fun onTick(p0: Long) {
                        val seconds = p0 / 1000
                        currentTime = seconds
                        if (seconds < 10) binding.timeCounter.text = "00:0$seconds"
                        else binding.timeCounter.text = "00:$seconds"
                    }

                    override fun onFinish() {
                        mediaPlayer.release()
                        mediaPlayer = null
                        mediaPlayer = MediaPlayer.create(
                            this@MultiGame4x4ScreenActivity,
                            R.raw.song3timeover
                        )
                        mediaPlayer.start()
                        Toast.makeText(
                            this@MultiGame4x4ScreenActivity,
                            "S??re Bitti!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
                timer.start()

                //Kartlar?? resetleme
                fun resetUi() {
                    for (card in screenItems) {
                        if (!card.isMatched!!) {
                            card.isFaceUp = false
                        }
                    }
                }

                //E??le??me kontrol??
                fun checkMatch(card1: Int, card2: Int) {
                    fun String.trimAndDouble() = trim().toDouble()
                    if (screenItems[card1].name == screenItems[card2].name) {
                        matchedCount += 1;
                        ////
                        if (matchedCount == 8) {
                            mediaPlayer?.release()
                            mediaPlayer = null
                            mediaPlayer =
                                MediaPlayer.create(this@MultiGame4x4ScreenActivity, R.raw.song4won)
                            mediaPlayer.start()
                        } else {
                            mediaPlayer?.release()
                            mediaPlayer = null
                            mediaPlayer = MediaPlayer.create(
                                this@MultiGame4x4ScreenActivity,
                                R.raw.song2matched
                            )
                            mediaPlayer.start()
                        }
                        ////
                        val houseScore: Double =
                            if (screenItems[card1].house.toString() == "Gryffindor"
                                || screenItems[card1].house.toString() == "Slytherin"
                            ) 2.0 else 1.0;
                        Toast.makeText(
                            this@MultiGame4x4ScreenActivity,
                            "Cards matched! Good job!",
                            Toast.LENGTH_SHORT
                        ).show()
                        screenItems[card1].isMatched = true
                        screenItems[card2].isMatched = true
                        if (turn) {
                            playerScore1 =
                                playerScore1?.plus((2.0 * (screenItems[card1].score?.trimAndDouble()!!) * houseScore))
                            binding.playerOnePoints.text = "${playerScore1.toString()} Score"
                        } else {
                            playerScore2 =
                                playerScore2?.plus((2.0 * (screenItems[card1].score?.trimAndDouble()!!) * houseScore))
                            binding.playerTwoPoints.text = "${playerScore2.toString()} Score"

                        }
                    } else {
                        if (screenItems[card1].house == screenItems[card2].house) {
                            val houseScore: Double =
                                if (screenItems[card1].house.toString() == "Gryffindor"
                                    || screenItems[card1].house.toString() == "Slytherin"
                                ) 2.0 else 1.0;
                            val scoreCalc = ((screenItems[card1].score?.trimAndDouble()!! +
                                    screenItems[card2].score?.trimAndDouble()!!) / houseScore)
                            if (turn) {
                                playerScore1 = playerScore1?.minus(scoreCalc)
                                binding.playerOnePoints.text = "${playerScore1.toString()} Score"
                            } else {
                                playerScore2 = playerScore2?.minus(scoreCalc)
                                binding.playerTwoPoints.text = "${playerScore2.toString()} Score"
                            }


                        } else {
                            val houseScore1: Double? =
                                if (screenItems[card1].house.toString() == "Gryffindor"
                                    || screenItems[card1].house.toString() == "Slytherin"
                                ) 2.0 else 1.0;
                            val houseScore2: Double? =
                                if (screenItems[card2].house.toString() == "Gryffindor"
                                    || screenItems[card1].house.toString() == "Slytherin"
                                ) 2.0 else 1.0;
                            if (turn) {
                                playerScore1 = playerScore1?.minus(
                                    ((screenItems[card1].score?.trimAndDouble()!! +
                                            screenItems[card2].score?.trimAndDouble()!!) / 2) * (houseScore1!! * houseScore2!!)
                                )
                                binding.playerOnePoints.text = "${playerScore1.toString()} Score"
                            } else {
                                playerScore2 = playerScore2?.minus(
                                    ((screenItems[card1].score?.trimAndDouble()!! +
                                            screenItems[card2].score?.trimAndDouble()!!) / 2) * (houseScore1!! * houseScore2!!)
                                )
                                binding.playerTwoPoints.text = "${playerScore2.toString()} Score"
                            }
                            //
                            turn = !turn
                            if (!turn) {
                                binding.playerOne.alpha = 0.3f
                                binding.playerOnePoints.alpha = 0.3f
                                binding.playerTwo.alpha = 1f
                                binding.playerTwoPoints.alpha = 1f

                            } else {
                                binding.playerOne.alpha = 1f
                                binding.playerOnePoints.alpha = 1f
                                binding.playerTwo.alpha = 0.3f
                                binding.playerTwoPoints.alpha = 0.3f
                            }

                        }
                    }
                }

                //Cardlar??n durumlar??n?? kontrol etme
                fun updateCards(position: Int) {
                    val card = screenItems[position]
                    if (card.isFaceUp == true) {
                        Toast.makeText(
                            this@MultiGame4x4ScreenActivity,
                            "Hatal?? oynama!",
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

                //Cardlar?? g??ncelleme
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