package com.roman.application.prayer.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.roman.application.databinding.ItemPrayerTimeBinding
import com.roman.application.home.domain.model.response.prayer.Prayers
import com.roman.application.util.formatDate

class PrayerTimeAdapter(private val data: ArrayList<Prayers>, private val onClick: (prayer: Prayers) -> Unit) :
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

        fun bind(prayer: Prayers, position: Int) {
            val dateFormat = prayer.namazTime.formatDate("EE MMM dd HH:mm:ss 'GMT'Z yyyy", "HH:mm")
            binding.tvPrayer.text = prayer.namazName
            binding.tvPrayerTime.text = dateFormat
            binding.tvIqama.text = dateFormat
            binding.switchHeros1.isChecked = prayer.isAlarmOn

            binding.switchHeros1.setOnCheckedChangeListener { buttonView, isChecked ->
                prayer.isAlarmOn = isChecked
                onClick.invoke(prayer)

            }

//            binding.tvPrayerTime.text =  prayer.substring(0, prayer.length - 3).trim()
//            binding.tvIqama.text =  prayer.substring(0, prayer.length - 3).trim()

        }
    }
}
