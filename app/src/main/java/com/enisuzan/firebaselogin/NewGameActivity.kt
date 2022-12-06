package com.enisuzan.firebaselogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.enisuzan.firebaselogin.databinding.ActivityMainBinding
import com.enisuzan.firebaselogin.databinding.ActivityNewGameBinding
import kotlinx.android.synthetic.main.activity_new_game.*

class NewGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityNewGameBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var difficulty : String = ""

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

        binding.singleGameButton.setOnClickListener {
            val intent = Intent(this, SingleGameScreenActivity::class.java)
            val bundle = Bundle()
            bundle.putString("Difficulty", difficulty)
            intent.putExtras(bundle)
            startActivity(intent)
            finish()
        }
    }
}