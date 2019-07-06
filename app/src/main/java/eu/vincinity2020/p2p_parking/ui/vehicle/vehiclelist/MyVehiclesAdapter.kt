package eu.vincinity2020.p2p_parking.ui.vehicle.vehiclelist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.data.entities.Vehicles

import android.view.MenuItem
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView


class MyVehiclesAdapter(private val context: Context,
                        private val vehicleList: List<Vehicles>, private val mCallbacks: ClickCallbacks )
    : RecyclerView.Adapter<MyVehiclesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.item_vehicle, viewGroup, false), mCallbacks )
    }

    override fun getItemCount(): Int {
        return vehicleList.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.update(vehicleList[position])
    }

    class ViewHolder(itemView: View, mCallbacks: ClickCallbacks) : RecyclerView.ViewHolder(itemView) {

        init {
            ButterKnife.bind(this, itemView)
            ivDefault.setOnClickListener {
                //Creating the instance of PopupMenu
                val popup = PopupMenu(itemView.context, ivDefault)
                //Inflating the Popup using xml file
                popup.menuInflater.inflate(R.menu.menu_default_vehicle, popup.menu)



                popup.setOnMenuItemClickListener { item: MenuItem? ->

                    when (item!!.itemId) {
                        R.id.makeDefault -> {
                            mCallbacks.onSetDefaultClicked(adapterPosition);
                        }
                    }

                    true
                }

                popup.show()//showing popup menu

            }
        }

        @BindView(R.id.tvBrand)
        lateinit var tvBrand: TextView

        @BindView(R.id.tvModel)
        lateinit var tvModel: TextView

        @BindView(R.id.tvRegNo)
        lateinit var tvRegNo: TextView

        @BindView(R.id.tvDescription)
        lateinit var tvDescription: TextView

         @BindView(R.id.tvWidth)
        lateinit var tvWidth: TextView

         @BindView(R.id.tvHeight)
        lateinit var tvHeight: TextView

         @BindView(R.id.tvWeight)
        lateinit var tvWeight: TextView

         @BindView(R.id.tvLength)
        lateinit var tvLength: TextView

         @BindView(R.id.ivVehicleType)
        lateinit var ivVehicleType: ImageView

         @BindView(R.id.ivDefault)
        lateinit var ivDefault: ImageView


         @BindView(R.id.cardParent)
        lateinit var cardParent: CardView




        fun update(vehicle: Vehicles) {

            if (vehicle.isDefault)
                cardParent.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.gray_light))
            else
                cardParent.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.colorWhite))

            tvBrand.text = vehicle.brand

            tvModel.text = vehicle.model

            tvRegNo.text = vehicle.regno

            tvDescription.text = vehicle.description

            tvWidth.text = vehicle.width

            tvHeight.text = vehicle.height

            tvWeight .text = vehicle.weight

            tvLength.text = vehicle.length

            if (vehicle.vehicleType.id == 1)
                ivVehicleType.setImageDrawable  (ContextCompat.getDrawable(itemView.context, R.drawable.ic_parking_standard))
            else if (vehicle.vehicleType.id == 2)
                ivVehicleType.setImageDrawable  (ContextCompat.getDrawable(itemView.context, R.drawable.ic_van_outline))
            else if (vehicle.vehicleType.id == 3)
                ivVehicleType.setImageDrawable  (ContextCompat.getDrawable(itemView.context, R.drawable.ic_parking_electrical))
            else if (vehicle.vehicleType.id == 4)
                ivVehicleType.setImageDrawable  (ContextCompat.getDrawable(itemView.context, R.drawable.ic_parking_handicap))
            else if (vehicle.vehicleType.id == 5)
                ivVehicleType.setImageDrawable  (ContextCompat.getDrawable(itemView.context, R.drawable.ic_motorcycle))
            else if (vehicle.vehicleType.id == 6)
                ivVehicleType.setImageDrawable  (ContextCompat.getDrawable(itemView.context, R.drawable.ic_emergency))




        }
    }


    interface ClickCallbacks
    {
        fun onSetDefaultClicked(position: Int)
    }

}