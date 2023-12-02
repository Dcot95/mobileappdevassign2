package org.wit.golfcourse.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.wit.golfcourse.databinding.CardGolfcourseBinding
import org.wit.golfcourse.models.GolfcourseModel

interface GolfcourseListener {
    fun onGolfcourseClick(golfcourse: GolfcourseModel, position : Int)
}

class GolfcourseAdapter constructor(private var golfcourses: List<GolfcourseModel>,
                                   private val listener: GolfcourseListener) :
    RecyclerView.Adapter<GolfcourseAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardGolfcourseBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val golfcourse = golfcourses[holder.adapterPosition]
        holder.bind(golfcourse, listener)
    }

    override fun getItemCount(): Int = golfcourses.size

    class MainHolder(private val binding : CardGolfcourseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(golfcourse: GolfcourseModel, listener: GolfcourseListener) {
            binding.golfcourseTitle.text = golfcourse.title
            binding.description.text = golfcourse.description
            binding.dateplayed.text = golfcourse.dateplayed
            binding.price.text = golfcourse.price
            binding.ratingBar.rating = golfcourse.rating
            Picasso.get().load(golfcourse.image).resize(200,200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onGolfcourseClick(golfcourse,adapterPosition) }
        }
    }
}
