package eu.vincinity2020.p2p_parking.ui.customviews.dialog.timer


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import eu.vincinity2020.p2p_parking.R
import kotlinx.android.synthetic.main.item_layout_timer.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.text.DecimalFormat
import java.text.NumberFormat

class TimerAdapter(private val dataSource: IntArray): RecyclerView.Adapter<TimerAdapter.TimerViewHolder>() {

    var activePosition = -1
    var yellowColor: Int? = null
    var whiteColor: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_layout_timer, parent, false)
        yellowColor = ContextCompat.getColor(parent.context, R.color.colorYellowText)
        whiteColor = ContextCompat.getColor(parent.context, R.color.colorWhite)
        return TimerViewHolder(view)
    }

    override fun getItemCount(): Int = dataSource.size

    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
        setupItem(holder.itemView, position)
    }

    private fun setupItem(itemView: View, position: Int) {
        if (dataSource[position] != -1) {
            val decimalFormat = NumberFormat.getInstance()
            decimalFormat.minimumIntegerDigits = 2
            itemView.txtTimerNumber.text = decimalFormat.format(dataSource[position])
        } else {
            itemView.txtTimerNumber.text = ""
        }

        if (activePosition != -1 && activePosition == position) {
            if (yellowColor != null) {
                itemView.txtTimerNumber.setTextColor(yellowColor!!)
            } else {
                doAsync {
                    val color = ContextCompat.getColor(itemView.context, R.color.colorYellowText)
                    uiThread {
                        itemView.txtTimerNumber.setTextColor(color)
                    }
                }
            }
        } else {
            if (whiteColor != null) {
                itemView.txtTimerNumber.setTextColor(whiteColor!!)
            } else {
                doAsync {
                    val color = ContextCompat.getColor(itemView.context, R.color.colorWhite)
                    uiThread {
                        itemView.txtTimerNumber.setTextColor(color)
                    }
                }
            }
        }
    }

    fun setActive(position: Int) {
        activePosition = position
        notifyDataSetChanged()
    }

    inner class TimerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}