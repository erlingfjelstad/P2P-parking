package eu.vincinity2020.p2p_parking.ui.mylocations.locationlist

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.data.entities.MyLocation

class MyLocationsAdapter(private val context: Context,
                         private val recentTripList: List<MyLocation>)
    : RecyclerView.Adapter<MyLocationsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.item_my_location, viewGroup, false))
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

        @BindView(R.id.tvDescription)
        lateinit var tvDescription: TextView

        @BindView(R.id.tvLat)
        lateinit var tvLat: TextView

        @BindView(R.id.tvLng)
        lateinit var tvLng: TextView

        fun update(trip: MyLocation) {

            tvDescription.text = trip.description
            tvLat.text = trip.lat.toString()
            tvLng.text = trip.lon.toString()


        }
    }
}