package eu.vincinity2020.p2p_parking.launcher

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.login.LoginActivity
import eu.vincinity2020.p2p_parking.navigation.NavigationActivity

class LauncherActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val loggedIn = App.get(this).isLoggedIn()

        val intent = if (loggedIn) {
            Intent(this, NavigationActivity::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }

        startActivity(intent)

    }
}