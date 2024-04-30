package com.example.chessgpt.moves

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chessgpt.R

class MovesAdapter(private val data: List<Pair<String, String>>) :
    RecyclerView.Adapter<MovesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.move_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val topTextView: TextView = itemView.findViewById(R.id.top_item)
        private val bottomTextView: TextView = itemView.findViewById(R.id.bottom_item)

        fun bind(itemPair: Pair<String, String>) {
            topTextView.text = itemPair.first
            bottomTextView.text = itemPair.second
        }
    }
}