package com.sreyans.discussondrawings.model

data class Drawing(var drawingImageUrl: String = "", var title: String = "", var createdOn: String = "", var markers: ArrayList<Marker> = ArrayList())