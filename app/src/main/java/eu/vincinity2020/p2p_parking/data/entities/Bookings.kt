package eu.vincinity2020.p2p_parking.data.entities


data class Bookings(val id: Long, val createdAtDate: String, val lastModifiedDate: String,
                    val user: User, val parkingSensor: ParkingSensor, val vehicle: Vehicles,
                    val fromTime: String, val toTime: String, var calculatedcost:Long
                    , val status: String, val bookingType: String)