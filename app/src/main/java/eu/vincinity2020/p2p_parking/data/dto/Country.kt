package eu.vincinity2020.p2p_parking.data.dto

/**
 * Created by Ankush on 19/11/18.
 */
data class Country(val id: Long, val country: String,
                   val descriptor: String?, val code: String?, val uncode: String?):BaseDTO()
{
    override fun toString(): String {
        return country
    }
}