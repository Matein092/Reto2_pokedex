package co.icesi.reto2_pokedex.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.icesi.reto2_pokedex.R

class PokemonViewHolder(itemView: View, listener: PokemonAdapter.IonItemClickListener) :
    RecyclerView.ViewHolder(itemView){

    //UI controllers
    var pokemontextrow : TextView = itemView.findViewById(R.id.pokemonNameRow)
    var pokemonimagerow : ImageView = itemView.findViewById(R.id.imagerow)

    init{
        itemView.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }

}