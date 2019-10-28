package eu.vincinity2020.p2p_parking.ui.auth.launcher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import eu.vincinity2020.p2p_parking.ui.auth.login.LoginActivity
import eu.vincinity2020.p2p_parking.ui.dashboard.DashboardActivity
import eu.vincinity2020.p2p_parking.utils.isLoggedIn
import org.jetbrains.anko.startActivity

class LauncherActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(isLoggedIn()){
            startActivity<DashboardActivity>()
        }else{
            startActivity<LoginActivity>()
        }
        finish()
    }
}