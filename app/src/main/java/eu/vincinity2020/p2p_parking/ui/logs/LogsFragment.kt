package eu.vincinity2020.p2p_parking.ui.logs


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.ui.logs.visits.VisitsFragments
import eu.vincinity2020.p2p_parking.utils.addFragment
import kotlinx.android.synthetic.main.fragment_logs.*

/**
 * A simple [Fragment] subclass.
 */
class LogsFragment : Fragment() {

    private lateinit var listener: LogsListener

    companion object {
        operator fun invoke(listener: LogsListener): LogsFragment {
            val fragment = LogsFragment()
            fragment.setListener(listener)
            return fragment
        }
    }

    private fun setListener(listener: LogsListener) {
        this.listener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        ntsLogs.setOnTouchListener { _, _ ->
            true
        }
        ntsLogs.tabIndex = 0
        childFragmentManager.addFragment(R.id.frlLogs,VisitsFragments())
    }

}

interface LogsListener{

}
