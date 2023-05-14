package com.sreyans.discussondrawings.model

import java.io.Serializable

data class Marker(var x: Float = 0f, var y: Float = 0f, var title: String = "", var description: String = "", var createdOn: String = "") : Serializable