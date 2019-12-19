package eu.vincinity2020.p2p_parking.ui.parking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.data.entities.parking.ParkingSpot
import eu.vincinity2020.p2p_parking.utils.gone
import kotlinx.android.synthetic.main.item_layout_place_list.view.*

class SelectParkingAdapter(val dataSource: ArrayList<ParkingSpot>) : RecyclerView.Adapter<SelectParkingAdapter.SelectParkingViewHolder>() {


    var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectParkingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_place_list, parent, false)
        return SelectParkingViewHolder(view)
    }

    override fun getItemCount(): Int = dataSource.size

    override fun onBindViewHolder(holder: SelectParkingViewHolder, position: Int) {
        setupParkingSpot(holder.itemView, position)
        holder.itemView.ctlPlaceListContainer.setOnClickListener {
            selectOrDeselectPlace(position)
            notifyDataSetChanged()
        }
    }

    private fun selectOrDeselectPlace(position: Int) {
        selectedPosition = if (selectedPosition == position) {
            -1
        } else {
            position
        }
    }

    private fun setupParkingSpot(itemView: View, position: Int) {
        itemView.txtSerialPlaceListItem.text = (position + 1).toString()
        itemView.txtNamePlaceListItem.text = dataSource[position].parkingLotName

        if (selectedPosition == position) {
            itemView.frameLayout2.setBackgroundResource(R.drawable.ic_quadilateral_yellow)
            itemView.txtNamePlaceListItem.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorYellowText))
        } else {
            itemView.frameLayout2.setBackgroundResource(R.drawable.ic_quadilateral_background)
            itemView.txtNamePlaceListItem.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorBlack))
        }
        itemView.imvDeletePlaceListItem.gone()
    }

    fun getSelectedParkingSpot(): ParkingSpot? = dataSource.getOrNull(selectedPosition)

    inner class SelectParkingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}