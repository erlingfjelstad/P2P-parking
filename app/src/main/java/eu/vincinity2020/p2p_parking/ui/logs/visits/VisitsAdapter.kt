package eu.vincinity2020.p2p_parking.ui.logs.visits

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eu.vincinity2020.p2p_parking.R

class VisitsAdapter() : RecyclerView.Adapter<VisitsAdapter.VisitsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisitsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_visits_logs, parent, false)
        return VisitsViewHolder(view)
    }

    override fun getItemCount(): Int = 3

    override fun onBindViewHolder(holder: VisitsViewHolder, position: Int) {

    }


    inner class VisitsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}