package eu.vincinity2020.p2p_parking.ui.customviews.dialog

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import eu.vincinity2020.p2p_parking.R


class FirstResponderAlertDialog(context: Context): AlertDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_dialog_first_responder)
        initViews()
    }

    private fun initViews() {

    }
}