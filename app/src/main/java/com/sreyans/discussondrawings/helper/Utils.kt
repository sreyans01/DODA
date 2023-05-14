package com.sreyans.discussondrawings.helper

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.*

object Utils {

    private const val SECOND = 1
    private const val MINUTE = 60 * SECOND
    private const val HOUR = 60 * MINUTE
    private const val DAY = 24 * HOUR
    private const val MONTH = 30 * DAY
    private const val YEAR = 12 * MONTH

    fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

    fun Long.toTimeAgo(): String {
        val time = this
        val now = System.currentTimeMillis()

        // convert back to second
        val diff = (now - time) / 1000

        return when {
            diff < MINUTE -> "Just now"
            diff < 2 * MINUTE -> "a minute ago"
            diff < 60 * MINUTE -> "${diff / MINUTE} minutes ago"
            diff < 2 * HOUR -> "an hour ago"
            diff < 24 * HOUR -> "${diff / HOUR} hours ago"
            diff < 2 * DAY -> "a day ago"
            diff < 30 * DAY -> "${diff / DAY} days ago"
            diff < 2 * MONTH -> "a month ago"
            diff < 12 * MONTH -> "${diff / MONTH} months ago"
            diff < 2 * YEAR -> "a year ago"
            else -> "${diff / YEAR} years ago"
        }
    }

}