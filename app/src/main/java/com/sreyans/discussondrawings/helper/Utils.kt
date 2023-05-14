package com.sreyans.discussondrawings.helper

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

object Utils {

    fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

    fun getUploadIdFromDate(date: String): String {
        var uploadId = date
        uploadId.replace(",","")
        uploadId.replace(":","")
        uploadId.replace("/","")
        uploadId.replace(" ","")
        uploadId.replace("-","")
        return  uploadId

    }

}