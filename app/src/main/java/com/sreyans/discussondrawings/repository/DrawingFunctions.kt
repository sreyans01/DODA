package com.sreyans.discussondrawings.repository

interface DrawingFunctions {
    fun onAddMarkerListener(x:Float, y: Float, title: String, description: String, timestamp: String)
}