package com.enisuzan.firebaselogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.enisuzan.firebaselogin.databinding.ActivityNewGameBinding
import kotlinx.android.synthetic.main.activity_new_game.*

class NewGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityNewGameBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var difficulty : String = ""
        var gameMode : String = ""

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId == binding.radioButton22.id){
                Toast.makeText(this, binding.radioButton22.text.toString(), Toast.LENGTH_SHORT).show()
                difficulty = "2x2"
            }
            if(checkedId == binding.radioButton44.id){
                Toast.makeText(this, binding.radioButton44.text.toString(), Toast.LENGTH_SHORT).show()
                difficulty = "4x4"
            }
            if(checkedId == binding.radioButton66.id){
                Toast.makeText(this, binding.radioButton66.text.toString(), Toast.LENGTH_SHORT).show()
                difficulty = "6x6"
            }
        }

        binding.radioGroup2.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId == binding.radioButtonSingle.id){
                Toast.makeText(this, binding.radioButtonSingle.text.toString(), Toast.LENGTH_SHORT).show()
                gameMode = "Single"
            }
            if(checkedId == binding.radioButtonMulti.id){
                Toast.makeText(this, binding.radioButtonMulti.text.toString(), Toast.LENGTH_SHORT).show()
                gameMode = "Multi"
            }
        }
        binding.singleGameButton.setOnClickListener {
            println("$difficulty ve $gameMode")
            if(difficulty == "2x2" && gameMode == "Single"){
                val intent = Intent(this, SingleGame2x2ScreenActivity::class.java)
                val bundle = Bundle()
                bundle.putString("Difficulty", difficulty)
                intent.putExtras(bundle)
                startActivity(intent)
                finish()
            }
            if(difficulty == "4x4" && gameMode == "Single"){
                val intent = Intent(this, SingleGame4x4ScreenActivity::class.java)
                val bundle = Bundle()
                bundle.putString("Difficulty", difficulty)
                intent.putExtras(bundle)
                startActivity(intent)
                finish()
            }
            if(difficulty == "6x6" && gameMode == "Single"){
                val intent = Intent(this, SingleGame6x6ScreenActivity::class.java)
                val bundle = Bundle()
                bundle.putString("Difficulty", difficulty)
                intent.putExtras(bundle)
                startActivity(intent)
                finish()
            }
            if(difficulty == "2x2" && gameMode == "Multi"){
                val intent = Intent(this, MultiGame2x2ScreenActivity::class.java)
                val bundle = Bundle()
                bundle.putString("Difficulty", difficulty)
                intent.putExtras(bundle)
                startActivity(intent)
                finish()
            }
            if(difficulty == "4x4" && gameMode == "Multi"){
                val intent = Intent(this, MultiGame4x4ScreenActivity::class.java)
                val bundle = Bundle()
                bundle.putString("Difficulty", difficulty)
                intent.putExtras(bundle)
                startActivity(intent)
                finish()
            }
            if(difficulty == "6x6" && gameMode == "Multi"){
                val intent = Intent(this, MultiGame6x6ScreenActivity::class.java)
                val bundle = Bundle()
                bundle.putString("Difficulty", difficulty)
                intent.putExtras(bundle)
                startActivity(intent)
                finish()
            }

        }
    }
}