package com.sreyans.discussondrawings.viewmodel

import androidx.lifecycle.LiveData
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.autelsdk.util.Resource
import com.sreyans.discussondrawings.helper.BaseViewModel
import com.sreyans.discussondrawings.model.Drawing
import com.sreyans.discussondrawings.model.Marker
import com.sreyans.discussondrawings.repository.DrawingsRepository
import com.sreyans.discussondrawings.repository.DrawingsRepositoryImpl
import kotlinx.coroutines.launch

class DrawingsViewModel() : BaseViewModel() {

    val repository: DrawingsRepository = DrawingsRepositoryImpl()
    val imageUrl = MutableLiveData<String>("")
    var markers = ArrayList<Marker>()

    suspend fun uploadDrawing(title: String, imageUrl: String, markers: ArrayList<Marker>) : MutableLiveData<Resource<String>> {
        return repository.uploadDrawing(title, imageUrl, markers)
    }

    suspend fun getDrawingImageUrl(selectedImageUri: Uri) : MutableLiveData<Resource<String>> {
        return repository.getDrawingImageUrl(selectedImageUri)
    }

    fun getAllDrawings() : LiveData<ArrayList<Drawing>> {
        return repository.getAllDrawings()
    }

}