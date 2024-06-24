package com.roman.application.home.presentation.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.roman.application.databinding.ItemCitiesBinding
import com.roman.application.home.domain.model.response.city.City

class CitiesAdapter(
    private val cities: ArrayList<City>?,
    private val onClick: (city: City) -> Unit
) :
    RecyclerView.Adapter<CitiesAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCitiesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cities?.size ?: 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        cities?.get(position)?.let { holder.bind(it) }
    }

    inner class MyViewHolder(private val binding: ItemCitiesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(city: City) {
            city.apply {
                binding.tvCity.text = nameEn
            }
            binding.lyParent.setOnClickListener {
                onClick.invoke(city)
            }
        }
    }
}
