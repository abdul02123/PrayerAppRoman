package com.roman.application.prayer.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.roman.application.databinding.ItemPrayerTimeBinding

class PrayerTimeAdapter(private val data: List<String>) :
    RecyclerView.Adapter<PrayerTimeAdapter.MySectionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySectionViewHolder {
        val binding = ItemPrayerTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MySectionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size ?: 0
    }

    override fun onBindViewHolder(holder: MySectionViewHolder, position: Int) {
        holder.bind(data[position], position)
    }

    inner class MySectionViewHolder(private val binding: ItemPrayerTimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(prayer: String, position: Int) {

            when(position){
                0 ->{
                    binding.tvPrayer.text = "Fajir"
                }
                1 ->{
                    binding.tvPrayer.text = "Dhuhar"
                }
                2 ->{
                    binding.tvPrayer.text = "Ars"
                }
                3 ->{
                    binding.tvPrayer.text = "Magrib"
                }
                4 ->{
                    binding.tvPrayer.text = "Isha"
                }
            }
            binding.tvPrayerTime.text =  prayer.substring(0, prayer.length - 3).trim()
            binding.tvIqama.text =  prayer.substring(0, prayer.length - 3).trim()

        }
    }
}
