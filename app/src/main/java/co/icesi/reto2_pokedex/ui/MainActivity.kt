package co.icesi.reto2_pokedex.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import co.icesi.reto2_pokedex.R
import co.icesi.reto2_pokedex.databinding.ActivityMainBinding
import co.icesi.reto2_pokedex.model.Trainer
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()
        setContentView(binding.root)

        binding.loginBtm.setOnClickListener{
            var nameTrainer = binding.trainerName.text.toString()
            nameTrainer = nameTrainer.trim()
            if(nameTrainer != " "){
                val intent = Intent(this, CatchPokemon::class.java).apply {
                    putExtra("TrainerName", nameTrainer)
                }
                val query = Firebase.firestore.collection("trainers").whereEqualTo("TrainerName", nameTrainer)
                query.get().addOnCompleteListener { task ->
                    if (task.result?.size() == 0){
                        Firebase.firestore.collection("trainers").add(Trainer(nameTrainer))
                        startActivity(intent)
                    } else{
                        startActivity(intent)
                    }
                }
            } else{
                Toast.makeText(this, "Error: You don´t write a trainer name",
                    Toast.LENGTH_LONG).show()
            }
            }
        }
    }
