package eu.vincinity2020.p2p_parking.ui.dashboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import eu.vincinity2020.p2p_parking.R
import kotlinx.android.synthetic.main.item_layout_nav.view.*

class DashboardNavigationAdapter(
        private val dataSource: ArrayList<Pair<String, Int>>,
        private val onItemClicked: (itemPosition: Int) -> Unit
): RecyclerView.Adapter<DashboardNavigationAdapter.DashboardNavigationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardNavigationViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_layout_nav, parent, false)
        return DashboardNavigationViewHolder(view)
    }

    override fun getItemCount(): Int = dataSource.size

    override fun onBindViewHolder(holder: DashboardNavigationViewHolder, position: Int) {
        holder.itemView.ctlNavItem.setOnClickListener { onItemClicked(position) }
        setupViewHolder(holder.itemView, dataSource[position])
    }

    private fun setupViewHolder(itemView: View, navItem: Pair<String, Int>) {
        itemView.imvItemIcon.setImageDrawable(ContextCompat.getDrawable(itemView.context, navItem.second))
        itemView.txtItemName.text = navItem.first
    }

    inner class DashboardNavigationViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}