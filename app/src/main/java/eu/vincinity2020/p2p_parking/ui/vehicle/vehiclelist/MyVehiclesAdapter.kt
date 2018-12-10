package eu.vincinity2020.p2p_parking.ui.vehicle.vehiclelist

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.data.entities.Vehicles
import eu.vincinity2020.p2p_parking.utils.DateUtils
import java.util.*

class MyVehiclesAdapter(private val context: Context,
                        private val recentTripList: List<Vehicles>)
    : RecyclerView.Adapter<MyVehiclesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.view_recent_trip, viewGroup, false))
    }

    override fun getItemCount(): Int {
        return recentTripList.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.update(recentTripList[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            ButterKnife.bind(this, itemView)
        }

        @BindView(R.id.text_view_title_recent_trip)
        lateinit var titleTextView: AppCompatTextView

        @BindView(R.id.text_view_date_recent_trip)
        lateinit var dateTextView: AppCompatTextView

        @BindView(R.id.text_view_parking_spot_name_recent_trip)
        lateinit var parkingSpotTextView: AppCompatTextView

        fun update(vehicle: Vehicles) {

            titleTextView.text = vehicle.mobile

            parkingSpotTextView.text = vehicle.email

        }
    }
}