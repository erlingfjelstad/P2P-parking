package eu.vincinity2020.p2p_parking.data.entities.directions

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class UserStop(
        val name: String = "",
        @PrimaryKey val location: LatLng,
        var isStopDone:Boolean = false
): Parcelable{
    var eta: String? = null
    var distance:String? = null
}