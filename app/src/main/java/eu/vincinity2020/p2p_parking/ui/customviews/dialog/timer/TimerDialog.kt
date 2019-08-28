package eu.vincinity2020.p2p_parking.ui.customviews.dialog.timer

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import eu.vincinity2020.p2p_parking.R
import kotlinx.android.synthetic.main.layout_dialog_timer.*

class TimerDialog(context: Context): AlertDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_dialog_timer)
        initViews()
    }

    private fun initViews() {
        initHourRecycler()
        initMinuteRecycler()
        sw24HrsTimer.setOnCheckedChangeListener { _, isChecked ->
            val adapter = TimerAdapter(if (isChecked) get24Hours() else get12Hours())
            recTimerHour.adapter = adapter
        }
    }

    private fun initHourRecycler() {
        val adapter = TimerAdapter(get12Hours())
        recTimerHour.layoutManager = LinearLayoutManager(context)
        recTimerHour.adapter = adapter
        val helper = LinearSnapHelper()
        helper.attachToRecyclerView(recTimerHour)
        recTimerHour.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                (recTimerHour.adapter as TimerAdapter).setActive((recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() + 3)
            }
        })
    }

    private fun initMinuteRecycler() {
        val adapter = TimerAdapter(getMinutes())
        recTimerMinute.layoutManager = LinearLayoutManager(context)
        recTimerMinute.adapter = adapter
        val helper = LinearSnapHelper()
        helper.attachToRecyclerView(recTimerMinute)
        recTimerMinute.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //context.toast("${(recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()}")
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //context.toast("$dy")
                adapter.setActive((recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() + 3)
            }
        })
    }
}

fun get12Hours(): IntArray {
    val hours = (1..12).toList().toIntArray()
    return IntArray(18) { i ->
        when {
            i < 3 -> -1
            i > 14 -> -1
            else -> hours[i - 3]
        }
    }
}

fun get24Hours(): IntArray {
    val hours = (0..23).toList().toIntArray()
    return IntArray(30) { i ->
        when {
            i < 3 -> -1
            i > 26 -> -1
            else -> hours[i - 3]
        }
    }
}

fun getMinutes(): IntArray {
    val hours = (0..59).toList().toIntArray()
    return IntArray(66) { i ->
        when {
            i < 3 -> -1
            i > 62 -> -1
            else -> hours[i - 3]
        }
    }
}
