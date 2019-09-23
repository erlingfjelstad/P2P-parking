package eu.vincinity2020.p2p_parking.ui.progress

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.data.entities.directions.UserStop
import kotlinx.android.synthetic.main.item_layout_progress.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ProgressAdapter(val dataSource: ArrayList<UserStop>) : RecyclerView.Adapter<ProgressAdapter.ProgressViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgressViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_progress, parent, false)

        return ProgressViewHolder(view)
    }

    override fun getItemCount(): Int = dataSource.size

    override fun onBindViewHolder(holder: ProgressViewHolder, position: Int) {
        setupStop(holder, position)
    }

    private fun setupStop(holder: ProgressViewHolder, position: Int) {
        val stop = dataSource[position]
        holder.itemView.txtStopNameProgress.text = stop.name
        if (stop.isStopDone) {
            holder.itemView.txtEtaProgress.text = "-"
            holder.itemView.txtDistanceProgress.text = "-"
            doAsync {
                val drawable = ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_progress_finished)
                uiThread {
                    holder.itemView.imvStopProgress.setImageDrawable(drawable)
                }
            }
        } else {
            holder.itemView.txtEtaProgress.text = stop.eta
            holder.itemView.txtDistanceProgress.text = stop.distance
            doAsync {
                val drawable = ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_progress_unfinished)
                uiThread {
                    holder.itemView.imvStopProgress.setImageDrawable(drawable)
                }
            }
        }
    }


    inner class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}