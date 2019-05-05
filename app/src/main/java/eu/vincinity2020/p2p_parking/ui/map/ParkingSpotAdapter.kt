package eu.vincinity2020.p2p_parking.ui.map

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.button.MaterialButton
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.data.entities.ParkingSensor

class ParkingSpotAdapter(private val dataSet: ArrayList<ParkingSensor>, val context: Context,
                         private val onParkingSpotClickedListener: OnParkingSpotClickedListener)
    : RecyclerView.Adapter<ParkingSpotAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return dataSet.size
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_parking_spot_card, viewGroup, false)
        return ViewHolder(view, onParkingSpotClickedListener)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.update(dataSet[position])
    }

    fun getItemAtPosition(position: Int): ParkingSensor {
        return dataSet[position]
    }

    interface OnParkingSpotClickedListener {
        fun onParkingSpotClicked(parkingSensor: ParkingSensor)

        fun onCloseButtonClicked()
    }

    class ViewHolder(itemView: View,
                     private val onParkingSpotClickedListener: OnParkingSpotClickedListener)
        : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.text_view_parking_spot_name)
        lateinit var parkingSpotNameTextView: AppCompatTextView

        @BindView(R.id.button_register_parking_spot)
        lateinit var registerParkingSpotButton: MaterialButton

        @BindView(R.id.image_button_close)
        lateinit var closeImageButton: AppCompatImageButton

        init {
            ButterKnife.bind(this, itemView)
        }


        fun update(parkingSensor: ParkingSensor) {
            parkingSpotNameTextView.text = parkingSensor.status
            registerParkingSpotButton.setOnClickListener { onParkingSpotClickedListener.onParkingSpotClicked(parkingSensor) }
            closeImageButton.setOnClickListener { onParkingSpotClickedListener.onCloseButtonClicked() }


        }
    }

}
