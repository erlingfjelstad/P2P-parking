package eu.vincinity2020.p2p_parking.ui.profile.edituser


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import com.labo.kaji.fragmentanimations.CubeAnimation
import com.labo.kaji.fragmentanimations.MoveAnimation
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.common.AppConstants.Companion.NAV_FRAGMENT_ANIMATION_DURATION

class EditUserFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_edit_user, container, false)

    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return if (enter) {
            MoveAnimation.create(MoveAnimation.RIGHT, enter, NAV_FRAGMENT_ANIMATION_DURATION)
        } else {
            MoveAnimation.create(MoveAnimation.RIGHT, enter, NAV_FRAGMENT_ANIMATION_DURATION)
        }
    }

}
