package eu.vincinity2020.p2p_parking.ui.dashboard.home.fragmnet


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.data.entities.directions.UserStop
import eu.vincinity2020.p2p_parking.ui.customviews.dialog.timer.TimerDialog
import eu.vincinity2020.p2p_parking.utils.show
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.layout_pre_parking_status.*


/**
 * A simple [Fragment] subclass.
 */
class DirectionStatusFragment : Fragment() {

    private val allDisposables = CompositeDisposable()
    private val onViewCreatedObservable = PublishSubject.create<Boolean>()
    private lateinit var listener: DirectionStatusListener

    companion object {
        operator fun invoke(listener: DirectionStatusListener): DirectionStatusFragment {
            val fragment = DirectionStatusFragment()
            fragment.setlistener(listener)
            return fragment
        }
    }

    private fun setlistener(listener: DirectionStatusListener) {
        this.listener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_pre_parking_status, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreatedObservable.onNext(true)
        initViews()
    }

    private fun initViews() {

    }

    fun updateArrivalTime(duration: String) {
        allDisposables.add(
                onViewCreatedObservable.subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { isViewCreated ->
                            if (isViewCreated) {
                                txtMinuteArrivalETA?.text = duration
                            }
                        }
        )
    }

    fun showTimerButton() {
        allDisposables.add(
                onViewCreatedObservable.subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { isViewCreated ->
                            if (isViewCreated) {
                                crdTimerStatus.show()
                                crdTimerStatus.setOnClickListener{
                                    TimerDialog(requireContext()).show()
                                }
                            }
                        }
        )
    }

    fun showDirectionsButton(stops: ArrayList<UserStop>) {
        allDisposables.add(
                onViewCreatedObservable.subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { isViewCreated ->
                            if (isViewCreated) {
                                crdDirectionStatus.show()
                                crdDirectionStatus.setOnClickListener {
                                    val locations = stops.map { it.location }
                                    val gmmIntentUri = Uri.parse("google.navigation:q=${locations[1].lat},${locations[1].lng}")
                                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                    mapIntent.setPackage("com.google.android.apps.maps")
                                    startActivity(mapIntent)
                                }
                            }
                        }
        )
    }

    fun showCloseButton(){
        allDisposables.add(
                onViewCreatedObservable.subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe{isViewCreated ->
                            if(isViewCreated){
                                crdCloseStatus.show()
                                crdCloseStatus.setOnClickListener{
                                    listener.onCloseDirections()
                                }
                            }
                        }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        allDisposables.dispose()
    }

}

interface DirectionStatusListener{
    fun onCloseDirections()
}
