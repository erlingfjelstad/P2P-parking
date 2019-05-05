package eu.vincinity2020.p2p_parking.app.common

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import eu.vincinity2020.p2p_parking.utils.NetworkUtils

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    fun isNetworkConnected(): Boolean = NetworkUtils.isNetworkConnected(applicationContext)

    fun showToast(text: String)
    {
        Toast.makeText(this,text,Toast.LENGTH_SHORT ).show()
    }
}