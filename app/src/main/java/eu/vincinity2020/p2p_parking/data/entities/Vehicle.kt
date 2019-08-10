package eu.vincinity2020.p2p_parking.data.entities

import androidx.annotation.DrawableRes

data class Vehicle(
        @DrawableRes val resourceId:Int,
        val name:String
)