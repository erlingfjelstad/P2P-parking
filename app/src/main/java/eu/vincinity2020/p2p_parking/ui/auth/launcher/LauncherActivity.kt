package eu.vincinity2020.p2p_parking.ui.auth.launcher

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.ui.auth.login.LoginActivity
import eu.vincinity2020.p2p_parking.ui.dashboard.DashboardActivity
import eu.vincinity2020.p2p_parking.ui.navigation.NavigationActivity

class LauncherActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!App.get(this).isLoggedOut())
        {
            App.get(this).setIsLoggedIn(false)
            App.get(this).setIsLoggedOut(true)
        }

        val loggedIn = App.get(this).isLoggedIn()



        val intent = if (loggedIn) {
            Intent(this, DashboardActivity::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }

        startActivity(intent)
        finish()

    }
}