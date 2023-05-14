package com.sreyans.discussondrawings.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.autelsdk.util.Resource
import com.sreyans.discussondrawings.model.Drawing
import com.sreyans.discussondrawings.model.Marker
import com.sreyans.discussondrawings.repository.DrawingsRepository
import com.sreyans.discussondrawings.repository.DrawingsRepositoryImpl

class DrawingsViewModel() : BaseViewModel() {

    val repository: DrawingsRepository = DrawingsRepositoryImpl()
    val imageUrl = MutableLiveData<String>("")
    var markers = ArrayList<Marker>()

    suspend fun uploadDrawing(
        title: String,
        imageUrl: String,
        markers: ArrayList<Marker>,
    ): MutableLiveData<Resource<String>> {
        return repository.uploadDrawing(title, imageUrl, markers)
    }

    suspend fun updateDrawing(
        drawing: Drawing
    ): MutableLiveData<Resource<String>> {
        return repository.updateDrawing(drawing)
    }

    suspend fun getDrawingImageUrl(selectedImageUri: Uri): MutableLiveData<Resource<String>> {
        return repository.getDrawingImageUrl(selectedImageUri)
    }

    fun getAllDrawings(): LiveData<ArrayList<Drawing>> {
        return repository.getAllDrawings()
    }

}