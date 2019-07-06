package eu.vincinity2020.p2p_parking.ui.vehiclelist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.data.entities.Vehicle
import eu.vincinity2020.p2p_parking.ui.vehiclelist.adapter.VehicleListAdapter
import kotlinx.android.synthetic.main.activity_vehicle_list.*
import org.jetbrains.anko.startActivity
import java.util.ArrayList

class VehicleListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_list)

        initViews()
    }

    private fun initViews() {
        revVehicleList.layoutManager = LinearLayoutManager(this)
        val adapter = VehicleListAdapter(getDummyData()){
            startActivity<AddEditVehicleActivity>()
        }
        revVehicleList.adapter = adapter
    }

    private fun getDummyData(): ArrayList<Vehicle> {
        return arrayListOf(
                Vehicle(R.drawable.ic_car, "KE 38886 (Opel Ascona)"),
                Vehicle(R.drawable.ic_car, "BP 87746 (Volvo 700)"),
                Vehicle(R.drawable.ic_car, "KE 38886 (Opel Ascona)")

        )
    }
}
