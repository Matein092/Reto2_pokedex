package co.icesi.reto2_pokedex.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import co.icesi.reto2_pokedex.databinding.ActivityCatchPokemonBinding
import co.icesi.reto2_pokedex.databinding.ActivityMainBinding
import co.icesi.reto2_pokedex.model.Pokemon
import co.icesi.reto2_pokedex.util.Constants
import co.icesi.reto2_pokedex.util.HTTPSWebUtilDomi
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CatchPokemon : AppCompatActivity() {

    private lateinit var binding : ActivityCatchPokemonBinding
    private lateinit var bindingMain : ActivityMainBinding
    private lateinit var trainerName : String
    private val adapter = PokemonAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatchPokemonBinding.inflate(layoutInflater)
        bindingMain = ActivityMainBinding.inflate(layoutInflater)


        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()
        setContentView(binding.root)
        trainerName =bindingMain.trainerName.toString()
        val pokemonList = binding.pokemonList
        pokemonList.setHasFixedSize(true)
        pokemonList.layoutManager = LinearLayoutManager(this)
        pokemonList.adapter = adapter
        adapter.setOnItemClickListener(object : PokemonAdapter.IonItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@CatchPokemon, PokemonView::class.java).apply {
                    putExtra("pokemon", adapter.getPokemon(position))
                }
                startActivity(intent)
            }
        })

        binding.catchBtm.setOnClickListener {
            var pokemonName = binding.pokemonName.text.toString()
            if (pokemonName != ""){
                pokemonName = pokemonName.lowercase()
                pokemonName = pokemonName.trim()
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        pokemonName = pokemonName.trim()
                        val response = HTTPSWebUtilDomi().GETRequest("${Constants.BASE_URL_POKEAPI}api/v2/pokemon/${pokemonName}")
                        val jsonObj: JsonObject = Gson().fromJson(response, JsonObject::class.java)
                        pokemonName = pokemonName.replaceFirstChar { it.uppercaseChar() }
                        val stats = jsonObj["stats"].toString()
                        val kinds = jsonObj["types"].toString()
                        val images = jsonObj["sprites"].toString()
                        var newPokemon = createPokemon(pokemonName, images, kinds ,stats)
                        Firebase.firestore.collection("pokemon").add(newPokemon)
                        withContext(Dispatchers.Main){
                            Toast.makeText(this@CatchPokemon, "${pokemonName} Has been captured!",
                                Toast.LENGTH_LONG)
                            addPokemonsToList()
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main){
                            Toast.makeText(this@CatchPokemon, "Error: The name of the pokemon that you want to catch is incorrect",
                                Toast.LENGTH_LONG)
                        }
                    }
                }
            } else{
                Toast.makeText(this, "Error: The name of the pokemon cant be empty", Toast.LENGTH_LONG)

            }
        }
        binding.goBtm.setOnClickListener {
            var pokemonName = binding.goBtm.text.toString()
            if (pokemonName != ""){
                pokemonName = pokemonName.trim()
                pokemonName = pokemonName.replaceFirstChar { it.uppercaseChar() }
                Firebase.firestore.collection("pokemon")
                    .whereEqualTo("trainerName", trainerName)
                    .whereEqualTo("name",pokemonName)
                    .get().addOnCompleteListener { task ->
                        if (task.result?.size() != 0){
                            adapter.clearPokemon()
                            for (document in task.result!!) {
                                var nPokemon = document.toObject(Pokemon::class.java)
                                adapter.addPokemon(nPokemon)
                                adapter.notifyDataSetChanged()
                            }
                        } else{
                            Toast.makeText(this@CatchPokemon, "Error: You dont catch the pokemon that you want", Toast.LENGTH_LONG)
                        }
                    }
            } else{
                addPokemonsToList()
            }



        }
    }
    override fun onResume() {
        super.onResume()
        addPokemonsToList()
    }
    fun createPokemon(name: String, images: String, kinds: String, stats: String) : Pokemon{

        var arrayStats = stats.split("},")
        var arrayKinds = kinds.split("},")
        var beginImage = images.indexOf("front_default")
        var endImage = images.indexOf("front_female")
        var urlImage = images.subSequence(beginImage, endImage).toString()
        urlImage = urlImage.replace("front_default","")
        urlImage = urlImage.replace("\":\"","")
        urlImage = urlImage.replace("\",\"","")
        var  i = 0
        val stats: IntArray = intArrayOf(0, 0, 0, 0, 0, 0)
        var kindsString = ""
        while (i<arrayStats.size){
            var stat = arrayStats[i]
            var begin = stat.indexOf(":")
            var end = stat.indexOf(",")

            stats[i] = stat.subSequence(begin+1, end).toString().toInt()
            i++

        }
        i = 0
        while (i<arrayKinds.size){
            var type = arrayKinds[i]
            var begin = type.indexOf("\":\"")
            var end = type.indexOf("\",")
            kindsString = kindsString+type.subSequence(begin+3, end).toString().replaceFirstChar { it.uppercaseChar() }
            kindsString = kindsString+"-"
            i++
        }
        kindsString = kindsString.subSequence(0, kindsString.length-1).toString()
        val nPokemon : Pokemon = Pokemon(
            urlImage,
            name,
            kindsString,
            stats[0].toDouble(),
            stats[1].toDouble(),
            stats[2].toDouble(),
            stats[5].toDouble(),
            trainerName,
            System.currentTimeMillis())
        return nPokemon
    }

    fun addPokemonsToList(){
        adapter.clearPokemon()
        Firebase.firestore.collection("pokemon").
        whereEqualTo("trainerName", trainerName).
        orderBy("timeAdded", Query.Direction.ASCENDING).
        get().addOnCompleteListener { task ->
            for (document in task.result!!) {
                var nPokemon = document.toObject(Pokemon::class.java)
                adapter.addPokemon(nPokemon)
                adapter.notifyDataSetChanged()
            }
        }
    }
}