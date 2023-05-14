package com.sreyans.discussondrawings.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(
    itemView) {
    abstract fun onBind(position: Int, item: Any)
    abstract val bindingVariable: Int
}