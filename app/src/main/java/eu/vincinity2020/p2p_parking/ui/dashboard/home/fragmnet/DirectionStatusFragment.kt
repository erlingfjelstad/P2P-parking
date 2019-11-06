package eu.vincinity2020.p2p_parking.ui.dashboard.home.fragmnet


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.data.entities.directions.UserStop
import eu.vincinity2020.p2p_parking.data.repositories.UserStopRepository
import eu.vincinity2020.p2p_parking.ui.customviews.dialog.timer.TimerDialog
import eu.vincinity2020.p2p_parking.utils.show
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.layout_pre_parking_status.*
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.support.v4.runOnUiThread


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
        showNextStopDoneButton()
    }

    fun updateArrivalTime(duration: String) {
        if (isAdded) {
            runOnUiThread {
                txtMinuteArrivalETA?.text = duration
            }
        } else {
            allDisposables.add(
                    onViewCreatedObservable.subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { isViewCreated ->
                                if (isViewCreated) {
                                    runOnUiThread {
                                        txtMinuteArrivalETA?.text = duration
                                    }
                                }
                            }
            )
        }
    }

    fun showTimerButton() {
        allDisposables.add(
                onViewCreatedObservable.subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { isViewCreated ->
                            if (isViewCreated) {
                                crdTimerStatus.show()
                                crdTimerStatus.setOnClickListener {
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

    fun showCloseButton() {
        allDisposables.add(
                onViewCreatedObservable.subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { isViewCreated ->
                            if (isViewCreated) {
                                crdCloseStatus.show()
                                crdCloseStatus.setOnClickListener {
                                    MaterialDialog(requireContext()).show {
                                        title(text = "Close directions")
                                        message(text = "Are you sure you want to close directions?")
                                        positiveButton(text = "Yes", click = {
                                            listener.onCloseDirections()
                                            UserStopRepository.deleteAll()
                                        })
                                        negativeButton(text = "No", click = {
                                            dismiss()
                                        })
                                    }
                                }
                            }
                        }
        )
    }

    fun showNextStopDoneButton() {
        allDisposables.addAll(
                UserStopRepository.getAllUserStops()
                        .subscribe { stops ->
                            crdMarkNextStopDone.show()
                            crdMarkNextStopDone.setOnClickListener {
                                MaterialDialog(requireContext()).show {
                                    title(text = "Mark stop done")
                                    message(text = "Are you sure you want to mark the next stop as done?")
                                    positiveButton(text = "Yes", click = {

                                        val nextStop = stops.find { !it.isStopDone }
                                        if (nextStop != null) {
                                            nextStop.isStopDone = true
                                            UserStopRepository.updateUserStop(nextStop)
                                        } else {
                                            listener.onCloseDirections()
                                            UserStopRepository.deleteAll()
                                        }
                                    })
                                    negativeButton(text = "No", click = {
                                        dismiss()
                                    })
                                }
                            }
                        }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        allDisposables.dispose()
    }

    fun showNextStop(name: String?) {
        if (name != null) {
            context?.runOnUiThread {
                txtNextStopStatus.text = "Next stop: ${name.substringBefore(',')}"
            }
        }
    }

}

interface DirectionStatusListener {
    fun onCloseDirections()
}
