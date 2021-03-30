package com.example.myapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.R
import com.example.myapp.view.model.Gender
import com.example.myapp.view.util.RVClickListener

class GenderAdapter(private val context: Context, private val genderList : List<Gender>, private val onRecyclerListClick : RVClickListener) : RecyclerView.Adapter<GenderAdapter.GenderVH>() {

    var lastItemSelectedPos = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenderVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gender_register_list, parent, false)
        return GenderVH(view)
    }

    override fun getItemCount(): Int {
        return genderList.size
    }

    override fun onBindViewHolder(holder: GenderVH, position: Int) {
        val item = genderList[position]
        holder.genderImg.setImageResource(item.genderImage)
        holder.genderName.text = item.genderName

        if (lastItemSelectedPos == position){
            holder.itemView.setBackgroundResource(R.drawable.gender_selected_background)
        } else {
            holder.itemView.setBackgroundResource(R.drawable.edit_text_background)
        }
    }

    inner class GenderVH(itemView: View) : RecyclerView.ViewHolder(itemView){

        val genderImg : ImageView = itemView.findViewById(R.id.gender_img)
        val genderName : TextView = itemView.findViewById(R.id.gender_name)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                lastItemSelectedPos = position
                onRecyclerListClick.recyclerClickListener(it, position)
            }
        }
    }

    fun getBackgroundColor() {
        notifyDataSetChanged()
    }

}