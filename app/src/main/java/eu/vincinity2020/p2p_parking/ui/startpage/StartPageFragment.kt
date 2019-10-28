package eu.vincinity2020.p2p_parking.ui.startpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import eu.vincinity2020.p2p_parking.R
import kotlinx.android.synthetic.main.fragment_start_page.*

class StartPageFragment: Fragment() {

    companion object{
        val backstackTag = "frag_start"
    }

    private lateinit var listener: StartPageListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    fun setListener(listener: StartPageListener){
        this.listener = listener
    }

    private fun initViews() {

        linPlaces.setOnClickListener {
            listener.onPlacesClicked()
        }

        linProgress.setOnClickListener {
            listener.onProgressClicked()
        }

        linSettings.setOnClickListener {
            listener.onSettingsClicked()
        }

        ctlBeginVisit.setOnClickListener{
            listener.onBeginVisitClicked()
        }
    }
}

interface StartPageListener{
    fun onPlacesClicked()

    fun onProgressClicked()

    fun onSettingsClicked()

    fun onBeginVisitClicked()
}
