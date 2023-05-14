package com.sreyans.discussondrawings.model

import java.io.Serializable

data class Drawing(var drawingImageUrl: String = "", var title: String = "", var createdOn: String = "", var markers: ArrayList<Marker> = ArrayList()) :Serializable