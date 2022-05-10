package co.icesi.reto2_pokedex.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.icesi.reto2_pokedex.R
import co.icesi.reto2_pokedex.model.Pokemon
import co.icesi.reto2_pokedex.util.LoadImage

class PokemonAdapter :  RecyclerView.Adapter<PokemonViewHolder>(){

    private var pokemons = ArrayList<Pokemon>()
    private lateinit var mListener: IonItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val inflater =  LayoutInflater.from(parent.context)
        val view  = inflater.inflate(R.layout.pokemonrow, parent, false)
        val pokemonvh = PokemonViewHolder(view, mListener)
        return pokemonvh
    }

    fun setOnItemClickListener(listener: IonItemClickListener){
        mListener = listener
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon1 = pokemons[position]
        holder.pokemontextrow.text = pokemon1.name

        var loadImage  = LoadImage(holder.pokemonimagerow)
        loadImage.execute(pokemon1.image)
    }

    fun addPokemon(newPokemon : Pokemon){
        pokemons.add(newPokemon)
    }

    fun getPokemon(position: Int): Pokemon {
        return pokemons.get(position)
    }

    fun clearPokemon(){
        pokemons.clear()
    }

    override fun getItemCount(): Int {
        return pokemons.size
    }

    interface IonItemClickListener{
        fun onItemClick(position: Int)
    }
}