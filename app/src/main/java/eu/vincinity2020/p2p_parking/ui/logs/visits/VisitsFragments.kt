package eu.vincinity2020.p2p_parking.ui.logs.visits


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import eu.vincinity2020.p2p_parking.R
import kotlinx.android.synthetic.main.fragment_visits_fragments.*

/**
 * A simple [Fragment] subclass.
 */
class VisitsFragments : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_visits_fragments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        val adapter = VisitsAdapter()
        recVisitsLogs.layoutManager = LinearLayoutManager(context)
        recVisitsLogs.adapter = adapter
    }


}
