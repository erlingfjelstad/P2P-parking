package eu.vincinity2020.p2p_parking.ui.vehiclelist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.data.entities.Vehicle
import kotlinx.android.synthetic.main.item_layout_vehicle_list.view.*

class VehicleListAdapter(private val dataSource: ArrayList<Vehicle>,
                         val onVehicleClicked: (position: Int) -> Unit) : RecyclerView.Adapter<VehicleListAdapter.VehicleListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_vehicle_list, parent, false)
        return VehicleListViewHolder(view)
    }

    override fun getItemCount(): Int = dataSource.size

    override fun onBindViewHolder(holder: VehicleListViewHolder, position: Int) {
        holder.itemView.ctlVehicleContainer.setOnClickListener { onVehicleClicked(position) }
        setupVehicle(holder.itemView, dataSource[position])
    }

    private fun setupVehicle(itemView: View, vehicle: Vehicle) {
        itemView.imvVehicleIcon.setImageResource(vehicle.resourceId)
        itemView.txtVehicleName.text = vehicle.name
    }

    inner class VehicleListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}