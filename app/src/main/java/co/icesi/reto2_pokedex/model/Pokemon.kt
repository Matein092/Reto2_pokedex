package co.icesi.reto2_pokedex.model

import java.io.Serializable

data class Pokemon(val image:String = "",
              val name:String = "",
              val kind:String = "",
              val life:Double = 0.0,
              val attack:Double = 0.0,
              val defense:Double = 0.0,
              val velocity:Double = 0.0,
              val trainername:String = "",
              val timeAdded:Long = 0) : Serializable