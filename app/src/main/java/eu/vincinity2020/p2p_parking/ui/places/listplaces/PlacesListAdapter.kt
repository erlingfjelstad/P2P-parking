package eu.vincinity2020.p2p_parking.ui.places.listplaces

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.data.entities.locations.PlaceRequest
import eu.vincinity2020.p2p_parking.utils.gone
import eu.vincinity2020.p2p_parking.utils.show
import kotlinx.android.synthetic.main.item_layout_place_list.view.*

class PlacesListAdapter(private val dataSource: ArrayList<PlaceRequest>,
                        private val onPlaceSelected: (places: List<PlaceRequest>) -> Unit,
                        private val onPlaceDeleted: (index: Int) -> Unit) : RecyclerView.Adapter<PlacesListAdapter.PlacesListViewHolder>() {

    val selectedPlaces = arrayListOf<Int>()
    var isEditing = false
    var shouldHideProgressBars = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_place_list, parent, false)
        return PlacesListViewHolder(view)
    }

    override fun getItemCount(): Int = dataSource.size

    override fun onBindViewHolder(holder: PlacesListViewHolder, position: Int) {
        holder.itemView.ctlPlaceListContainer.setOnClickListener {
            selectOrDeselectPlace(position)
            notifyDataSetChanged()
            onPlaceSelected(getSelectedPlaceObjects())
        }
        setupPlace(holder.itemView, position)
    }

    private fun selectOrDeselectPlace(position: Int) {
        if (selectedPlaces.contains(position)) {
            selectedPlaces.remove(position)
        } else {
            selectedPlaces.add(position)
        }
    }

    private fun setupPlace(itemView: View, position: Int) {
        itemView.txtSerialPlaceListItem.text = (position + 1).toString()
        itemView.txtNamePlaceListItem.text = dataSource[position].description
        if (selectedPlaces.contains(position)) {
            itemView.frameLayout2.setBackgroundResource(R.drawable.ic_quadilateral_yellow)
            itemView.txtNamePlaceListItem.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorYellowText))
        } else {
            itemView.frameLayout2.setBackgroundResource(R.drawable.ic_quadilateral_background)
            itemView.txtNamePlaceListItem.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorBlack))
        }
        if (isEditing) {
            itemView.imvDeletePlaceListItem.show()
            itemView.imvDeletePlaceListItem.setOnClickListener {
                onPlaceDeleted(position)
                itemView.pbPlaceListItem.show()
                itemView.imvDeletePlaceListItem.gone()
            }
        } else {
            itemView.imvDeletePlaceListItem.gone()
            itemView.pbPlaceListItem.gone()
        }

        if (shouldHideProgressBars) {
            itemView.pbPlaceListItem.gone()
        }
        if (position == dataSource.lastIndex) {
            shouldHideProgressBars = false
        }
    }

    fun deleteLocation(position: Int) {
        dataSource.removeAt(position)
        shouldHideProgressBars = true
        notifyDataSetChanged()
    }

    fun hideAllProgressBars() {
        shouldHideProgressBars = true
        notifyDataSetChanged()
    }

    fun toggleEditing() {
        isEditing = !isEditing
        notifyDataSetChanged()
    }

    fun getSelectedPlaceObjects(): List<PlaceRequest> {
        return selectedPlaces.map { dataSource[it] }
    }

    inner class PlacesListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}