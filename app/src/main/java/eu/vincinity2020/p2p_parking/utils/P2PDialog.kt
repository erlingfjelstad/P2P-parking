package eu.vincinity2020.p2p_parking.utils

import android.app.Activity
import android.content.Context
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import eu.vincinity2020.p2p_parking.R

object P2PDialog {

    fun errorDialog(context: Context, message: String,
                    dialogCallback: DialogCallback? = null): MaterialDialog {
        val dialog = MaterialDialog(context).title(res = R.string.dialog_title_error)
            .message(text = message)
        if (dialogCallback != null) {
            dialog.positiveButton(res = R.string.dialog_action_okay,
                click = dialogCallback)
        } else {
            dialog.positiveButton(res = R.string.dialog_action_okay,
                click = { it.dismiss() })
        }
        return dialog
    }

    fun infoDialog(context: Context, infoMessage: String): MaterialDialog {

        return MaterialDialog(context)
            .title(res = R.string.dialog_title_info)
            .message(text = infoMessage)
            .positiveButton(res = R.string.dialog_action_okay, click = { it.dismiss() })

    }

    fun infoDialog(context: Context, infoMessage: String, onPositiveCallback:DialogCallback): MaterialDialog {

        return MaterialDialog(context)
            .title(res = R.string.dialog_title_info)
            .message(text = infoMessage)
            .positiveButton(res = R.string.dialog_action_okay, click = onPositiveCallback)
    }

    fun infoDialog(context: Context, infoMessage: String, onPositiveCallback:DialogCallback,
                   onNegativeCallback:DialogCallback): MaterialDialog {

        return MaterialDialog(context)
            .title(res = R.string.dialog_title_info)
            .message(text = infoMessage)
            .positiveButton(res = R.string.dialog_action_okay, click = onPositiveCallback)
            .negativeButton(res = R.string.dialog_action_exit, click = onNegativeCallback)
    }

    fun getExitDialog(context: Context, exitMessage: String): MaterialDialog {
        return MaterialDialog(context)
            .title(res = R.string.dialog_title_confirmation)
            .message(text = exitMessage)
            .negativeButton(res = R.string.dialog_action_cancel, click = { it.dismiss() })
            .positiveButton(res = R.string.dialog_action_okay, click = {
                (context as Activity).finishAffinity()
            })
    }
}