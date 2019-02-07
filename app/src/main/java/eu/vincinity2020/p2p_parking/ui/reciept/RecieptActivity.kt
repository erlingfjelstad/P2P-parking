package eu.vincinity2020.p2p_parking.ui.reciept

import android.content.Context
import android.content.Intent
import android.os.Bundle
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.common.BaseActivity
import eu.vincinity2020.p2p_parking.data.entities.ParkingSensor
import eu.vincinity2020.p2p_parking.ui.navigation.NavigationActivity
import kotlinx.android.synthetic.main.activity_reciept.*

class RecieptActivity : BaseActivity() {

    companion object {
        private const val ARG_PARKING_SPOT = "arg:ParkingSensor"
        private const val ARG_TO_TIME = "arg:ToTime"
        private const val ARG_FROM_TIME = "arg:FromTime"

        fun getLaunchIntent(context: Context, parkingSensor: ParkingSensor, fromTime: String, toTime: String): Intent {
            return Intent(context, RecieptActivity::class.java).apply {
                putExtra(ARG_PARKING_SPOT, parkingSensor)
                putExtra(ARG_FROM_TIME, fromTime)
                putExtra(ARG_TO_TIME, toTime)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reciept)

        val parkingSpot = intent.getParcelableExtra<ParkingSensor>(ARG_PARKING_SPOT)
        val fromTime = intent.getStringExtra(ARG_FROM_TIME)
        val toTime = intent.getStringExtra(ARG_TO_TIME)


        val content = getString(R.string.content_reciept)

        text_view_content_reciept.text = String.format(content, parkingSpot.status, fromTime, toTime)

        button_ok_reciept.setOnClickListener {
            val intent = Intent(this, NavigationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}