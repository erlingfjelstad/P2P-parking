package eu.vincinity2020.p2p_parking.data.entities



data class Vehicles(val id: Long, val name: String, val regno: String, val brand: String, val model: String, val height: String,
                    val width: String, val length: String, val weight: String, val year: String, val description: String, val vehicleType: VehicleTypes)