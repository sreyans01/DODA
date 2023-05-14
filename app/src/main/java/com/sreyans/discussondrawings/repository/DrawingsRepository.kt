package com.sreyans.discussondrawings.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.autelsdk.util.Resource
import com.sreyans.discussondrawings.model.Drawing
import com.sreyans.discussondrawings.model.Marker

interface DrawingsRepository {

    suspend fun uploadDrawing(title: String, imageUrl: String, markers: ArrayList<Marker>) : MutableLiveData<Resource<String>>

    suspend fun updateDrawing(drawing: Drawing) : MutableLiveData<Resource<String>>

    suspend fun getDrawingImageUrl(selectedImageUri: Uri) :  MutableLiveData<Resource<String>>

    fun getAllDrawings() : LiveData<ArrayList<Drawing>>


}