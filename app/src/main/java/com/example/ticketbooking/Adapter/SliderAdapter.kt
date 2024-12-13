package com.example.ticketbooking.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.ticketbooking.Models.SliderItems
import com.example.ticketbookingapp.databinding.ViewholderSliderBinding
import com.bumptech.glide.request.RequestOptions

class SliderAdapter(
    private var sliderItems: MutableList<SliderItems>,
    private val viewPager2: ViewPager2
) : RecyclerView.Adapter<SliderAdapter.SlideViewHolder>() {

    private var context: Context? = null

    // Updated to fix the issue of endless item addition
    private val runnable = Runnable {
        val newItems = ArrayList(sliderItems)
        sliderItems.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class SlideViewHolder(private val binding: ViewholderSliderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(sliderItem: SliderItems) {
            val requestOptions=RequestOptions().transform(CenterCrop(), RoundedCorners(60))
            // Ensure context is non-null and correctly used
            val ctx = context ?: return
            Glide.with(ctx)
                .load(sliderItem.image)
                .apply(requestOptions)
                .into(binding.imageSlide)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SlideViewHolder {
        context = parent.context
        val binding = ViewholderSliderBinding.inflate(LayoutInflater.from(context), parent, false)
        return SlideViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SlideViewHolder, position: Int) {
        holder.bind(sliderItems[position])
        // Avoid unnecessary infinite loops by checking conditions carefully
        if (position == sliderItems.size - 2) {
            viewPager2.post(runnable)
        }
    }

    override fun getItemCount(): Int = sliderItems.size
}
