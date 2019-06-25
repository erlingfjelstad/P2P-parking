package eu.vincinity2020.p2p_parking.ui.auth.registeruser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.common.AppConstants
import kotlinx.android.synthetic.main.item_layout_nav.view.*

class NavigationAdapter(private val dataSource: ArrayList<String>,
                        val onItemClick: (position: Int) -> Unit):
        RecyclerView.Adapter<NavigationAdapter.NavigationViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavigationViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_layout_nav, parent, false)
        return NavigationViewHolder(view)
    }

    override fun getItemCount(): Int = dataSource.size

    override fun onBindViewHolder(holder: NavigationViewHolder, position: Int) {
        setupNavItem(holder.itemView, position)
    }

    private fun setupNavItem(itemView: View, position: Int) {
        itemView.imvItemIcon.setImageDrawable(ContextCompat.getDrawable(itemView.context, getIconForNav(position)))
        itemView.txtItemName.text = dataSource[position]
        itemView.ctlNavItem.setOnClickListener { onItemClick(position) }
    }

    private fun getIconForNav(position: Int): Int {
        return when (position) {
            0 -> {
                R.drawable.ic_nav_home
            }
            1 -> {
                R.drawable.ic_nav_privacy
            }
            2 -> {
                R.drawable.ic_nav_get_started
            }
            3 -> {
                R.drawable.ic_nav_about_us
            }
            else -> {
                R.drawable.ic_nav_home
            }
        }
    }

    inner class NavigationViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}