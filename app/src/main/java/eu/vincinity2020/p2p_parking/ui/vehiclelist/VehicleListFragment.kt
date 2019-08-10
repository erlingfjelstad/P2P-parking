package eu.vincinity2020.p2p_parking.ui.vehiclelist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.labo.kaji.fragmentanimations.MoveAnimation
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.common.AppConstants
import eu.vincinity2020.p2p_parking.data.entities.Vehicle
import eu.vincinity2020.p2p_parking.ui.vehiclelist.adapter.VehicleListAdapter
import kotlinx.android.synthetic.main.activity_vehicle_list.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import java.util.ArrayList

class VehicleListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_vehicle_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        revVehicleList.layoutManager = LinearLayoutManager(context)
        val adapter = VehicleListAdapter(getDummyData()){
            startActivity<AddEditVehicleActivity>()
        }
        btnAddVehicleVehicleList.setOnClickListener {
            toast("Coming soon.")
        }
        revVehicleList.adapter = adapter
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return if (enter) {
            MoveAnimation.create(MoveAnimation.LEFT, enter, AppConstants.NAV_FRAGMENT_ANIMATION_DURATION)
        } else {
            MoveAnimation.create(MoveAnimation.LEFT, enter, AppConstants.NAV_FRAGMENT_ANIMATION_DURATION)
        }
    }

    private fun getDummyData(): ArrayList<Vehicle> {
        return arrayListOf(
                Vehicle(R.drawable.ic_car, "KE 38886 (Opel Ascona)"),
                Vehicle(R.drawable.ic_car, "BP 87746 (Volvo 700)"),
                Vehicle(R.drawable.ic_car, "KE 38886 (Opel Ascona)")

        )
    }
}
