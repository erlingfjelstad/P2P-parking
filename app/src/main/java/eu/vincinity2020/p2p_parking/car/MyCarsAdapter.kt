package eu.vincinity2020.p2p_parking.car

import android.content.Context
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.entities.Car


class MyCarsAdapter(private val dataSet: ArrayList<Car>, val context: Context,
                    private val onCarClicked: MyCarsAdapter.OnCarClicked)
    : RecyclerView.Adapter<MyCarsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_my_car, viewGroup, false)
        return ViewHolder(view, onCarClicked)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.update(dataSet[position])
    }

    fun swapData(cars: List<Car>) {
        dataSet.clear()
        dataSet.addAll(cars)
        notifyDataSetChanged()
    }


    interface OnCarClicked {
        fun onCarDefaulted(car: Car)
    }

    class ViewHolder(itemView: View, private val onCarClicked: OnCarClicked)
        : RecyclerView.ViewHolder(itemView) {

        init {
            ButterKnife.bind(this, itemView)
        }

        @BindView(R.id.checkbox_view_my_car)
        lateinit var checkBox: AppCompatCheckBox

        fun update(car: Car) {
            checkBox.text = car.licencePlate
            checkBox.isChecked = car.isDefault

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    onCarClicked.onCarDefaulted(car)
                }
            }
        }
    }
}