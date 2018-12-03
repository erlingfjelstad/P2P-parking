package eu.vincinity2020.p2p_parking.ui.map

import android.content.Context
import android.support.design.button.MaterialButton
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.data.entities.ParkingSpot

class ParkingSpotAdapter(private val dataSet: ArrayList<ParkingSpot>, val context: Context,
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

    fun getItemAtPosition(position: Int): ParkingSpot {
        return dataSet[position]
    }

    interface OnParkingSpotClickedListener {
        fun onParkingSpotClicked(parkingSpot: ParkingSpot)

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


        fun update(parkingSpot: ParkingSpot) {
            parkingSpotNameTextView.text = parkingSpot.status
            registerParkingSpotButton.setOnClickListener { onParkingSpotClickedListener.onParkingSpotClicked(parkingSpot) }
            closeImageButton.setOnClickListener { onParkingSpotClickedListener.onCloseButtonClicked() }


        }
    }

}
