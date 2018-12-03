package eu.vincinity2020.p2p_parking.app.network

import eu.vincinity2020.p2p_parking.app.common.MvpView
import io.reactivex.observers.DisposableObserver
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException


abstract class NetworkResponse<T : Any>(private val view: MvpView?) : DisposableObserver<T>() {

    override fun onNext(t: T) {
        onSuccess(t)
    }

    override fun onComplete() {

    }

    override fun onError(t: Throwable) {
        when (t) {
            is HttpException -> {
                val responseBody = t.response().errorBody()
                responseBody?.let {
                    view?.onUnknownError(getErrorMessage(it))
                }

            }
            is SocketTimeoutException -> view?.onTimeout()
            is IOException -> view?.onNetworkError()
            else -> t.message?.let { view?.onUnknownError(it) }
        }
    }

    private fun getErrorMessage(responseBody: ResponseBody): String {
        return try {
            val jsonObject = JSONObject(responseBody.string())
            jsonObject.getString("message")
        } catch (e: Exception) {
            e.message.toString()
        }

    }

    abstract fun onSuccess(response: T)
}