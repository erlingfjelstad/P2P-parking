package eu.vincinity2020.p2p_parking.ui.places.addplaces

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.data.entities.places.Place
import eu.vincinity2020.p2p_parking.utils.gone
import eu.vincinity2020.p2p_parking.utils.show
import kotlinx.android.synthetic.main.item_layout_place.view.*

class AddPlacesAdapter(
        val dataSource: ArrayList<Place>,
        val onItemClicked: (position: Int) -> Unit,
        val isSearchAdapter: Boolean = false
) : RecyclerView.Adapter<AddPlacesAdapter.AddPlacesViewHolder>() {

    var selectedPosition = -1
    var searchTextColor = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddPlacesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_place, parent, false)
        if(isSearchAdapter){
            searchTextColor = ContextCompat.getColor(parent.context, R.color.colorBlack)
        }
        return AddPlacesViewHolder(view)
    }

    override fun getItemCount(): Int = dataSource.size

    override fun onBindViewHolder(holder: AddPlacesViewHolder, position: Int) {

        holder.itemView.ctlPlaceContainer.setOnClickListener {
            if(this.isSearchAdapter){
                selectedPosition = position
            }
            onItemClicked(position)
            notifyDataSetChanged()
        }

        holder.itemView.txtPlace.text = dataSource[position].name

        if (selectedPosition != -1 && selectedPosition == position) {
            holder.itemView.linPlaceSelectionContainer.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.colorSelection))
        }else{
            holder.itemView.linPlaceSelectionContainer.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, android.R.color.transparent))
        }

        if (this.isSearchAdapter) {
            holder.itemView.imvPlace.gone()
            holder.itemView.txtPlace.setTextColor(searchTextColor)
        } else {
            holder.itemView.imvPlace.show()
        }
    }

    inner class AddPlacesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}