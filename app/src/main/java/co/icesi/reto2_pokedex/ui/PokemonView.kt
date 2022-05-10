package co.icesi.reto2_pokedex.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import co.icesi.reto2_pokedex.R
import co.icesi.reto2_pokedex.databinding.ActivityPokemonViewBinding
import co.icesi.reto2_pokedex.model.Pokemon
import co.icesi.reto2_pokedex.util.LoadImage
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PokemonView : AppCompatActivity() {
    private lateinit var binding: ActivityPokemonViewBinding

    private lateinit var pokemon: Pokemon

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonViewBinding.inflate(layoutInflater)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()
        setContentView(binding.root)
        pokemon = (intent.extras?.get("pokemon") as Pokemon?)!!
        var loadImage  = LoadImage(binding.pokemonImage)
        loadImage.execute(pokemon.image)
        binding.namePokemon.text =  "${pokemon.name} (${pokemon.kind})"
        binding.valueAttack.text = pokemon.attack.toString()
        binding.valueDefense.text = pokemon.defense.toString()
        binding.valueLife.text = pokemon.life.toString()
        binding.valueVelocity.text = pokemon.velocity.toString()
        binding.releaseBtm.setOnClickListener {
            Firebase.firestore.collection("pokemon").
            whereEqualTo("trainername", pokemon.trainername).
            whereEqualTo("name", pokemon.name).
            get().addOnCompleteListener { task ->
                for (document in task.result!!) {
                    document.reference.delete()
                }
            }
            this.finish()
        }
    }
}