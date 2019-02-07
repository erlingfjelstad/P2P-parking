package eu.vincinity2020.p2p_parking.ui.bookings

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.leondzn.simpleanalogclock.SimpleAnalogClock
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.data.entities.Bookings
import eu.vincinity2020.p2p_parking.utils.DateUtils
import java.util.*


class MyBookingsAdapter(private val context: Context,
                        private val recentTripList: List<Bookings>)
    : RecyclerView.Adapter<MyBookingsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.view_my_booking, viewGroup, false))
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


        @BindView(R.id.startTime)
        lateinit var startTime: SimpleAnalogClock

        @BindView(R.id.endTime)
        lateinit var endTime: SimpleAnalogClock

        @BindView(R.id.tvParking)
        lateinit var tvParking: TextView


        fun update(trip: Bookings) {
            var startHours = DateUtils.formatStringToDate("yyyy-MM-dd'T'HH:mm:ss", trip.fromTime)
            val calendar = Calendar.getInstance()
            calendar.time = (startHours)
            startTime.setTime(calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND))

            var endHours = DateUtils.formatStringToDate("yyyy-MM-dd'T'HH:mm:ss", trip.toTime)
            val calendar2 = Calendar.getInstance()
            calendar2.time = (endHours)
            endTime.setTime(calendar2.get(Calendar.HOUR), calendar2.get(Calendar.MINUTE), calendar2.get(Calendar.SECOND))

            tvParking.setText(trip.parkingSensor.parkingLotName)
        }
    }
}