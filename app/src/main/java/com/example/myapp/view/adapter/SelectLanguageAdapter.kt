package com.example.myapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.R
import com.example.myapp.view.model.SelectLanguage

class SelectLanguageAdapter(private var context: Context, private var langList : ArrayList<SelectLanguage>, private var clickListener : LangClickListener)
    : RecyclerView.Adapter<SelectLanguageAdapter.ViewHolder>(), Filterable{

    private var selectedPos = -1
    var langFilterList : ArrayList<SelectLanguage> = langList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.choose_language_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return langFilterList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = langFilterList[position]
        holder.defaultText.text = item.defaultText
        holder.originalText.text = item.originalText
        holder.checkMark.visibility = (if (selectedPos == position) View.VISIBLE else View.GONE)

        holder.itemView.setOnClickListener {
            selectedPos = position
            langFilterList = langList
            clickListener.langClick(item)
        }

    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val defaultText : TextView = itemView.findViewById(R.id.language_large)
        val originalText : TextView = itemView.findViewById(R.id.language_small)
        val checkMark : ImageView = itemView.findViewById(R.id.check_mark_icon)

    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSequence = constraint.toString()
                val resultList = ArrayList<SelectLanguage>()
                if(charSequence.isEmpty()){
                    langFilterList = langList
                } else {
                    val searchChar = charSequence.toLowerCase()
                    for (i in langList){
                        if (i.originalText.toLowerCase().contains(searchChar) || i.defaultText.toLowerCase().contains(searchChar)){
                            resultList.add(i)
                        }
                    }
                    langFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = langFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                langFilterList = results?.values as ArrayList<SelectLanguage>
                notifyDataSetChanged()
            }

        }
    }

    fun showCheckMark(){
        notifyDataSetChanged()
    }

    interface LangClickListener{
        fun langClick(selectLanguage: SelectLanguage)
    }
}