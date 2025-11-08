package com.example.application_anonyme.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), message, duration).show()
}

fun Context?.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    this?.let {
        Toast.makeText(it, message, duration).show()
    }
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun EditText.showKeyboard() {
    requestFocus()
    val imm = context.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun String.formatDate(): String {
    return toRelativeTime()
}

fun String.toRelativeTime(): String {
    return try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = dateFormat.parse(this)

        if (date != null) {
            val now = Date()
            val diff = now.time - date.time

            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24

            when {
                seconds < 60 -> "À l'instant"
                minutes < 60 -> "Il y a ${minutes}m"
                hours < 24 -> "Il y a ${hours}h"
                days < 7 -> "Il y a ${days}j"
                else -> {
                    val displayFormat = SimpleDateFormat("dd MMM yyyy", Locale.FRENCH)
                    displayFormat.format(date)
                }
            }
        } else {
            "Date inconnue"
        }
    } catch (e: Exception) {
        "Date inconnue"
    }
}