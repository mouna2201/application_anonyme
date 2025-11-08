package com.example.application_anonyme.utils

import android.widget.Toast
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), message, duration).show()
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