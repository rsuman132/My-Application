package com.example.myapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.R
import com.example.myapp.view.model.WalkThrough

class WalkThroughAdapter(private val walkThrowList : List<WalkThrough>)
    : RecyclerView.Adapter<WalkThroughAdapter.WalkThroughViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalkThroughViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.walk_through_list, parent, false)
        return WalkThroughViewHolder(view)
    }

    override fun getItemCount(): Int {
        return walkThrowList.size
    }

    override fun onBindViewHolder(holder: WalkThroughViewHolder, position: Int) {
        val item = walkThrowList[position]
        holder.slideImage.setImageResource(item.image)
        holder.slideTitle.text = item.title
        holder.slideDescription.text = item.description
    }

    inner class WalkThroughViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val slideImage : ImageView = itemView.findViewById(R.id.slider_image)
        val slideTitle : TextView = itemView.findViewById(R.id.slider_title)
        val slideDescription : TextView = itemView.findViewById(R.id.slider_description)
    }
}