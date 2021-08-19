package com.abdullah.githubusers.extenstion

import android.graphics.Color
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.abdullah.githubusers.R
import com.google.android.material.snackbar.Snackbar

fun Fragment.showErrorMessage(message: String?) {
    val errorMessage =
        if (message.isNullOrEmpty()) getString(R.string.something_went_wrong) else message
    this.view?.let {
        Snackbar.make(it, errorMessage, Snackbar.LENGTH_LONG).apply {
            this.view.apply {
                val tvMessage =
                    findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                tvMessage?.setTextColor(Color.WHITE)
                setBackgroundColor(Color.RED)
            }
        }.show()
    }
}