package com.roman.application.athkar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.roman.application.athkar.domain.model.Athkar
import com.roman.application.databinding.ItemAkhtarBinding
import com.roman.application.home.domain.model.response.city.City

class AkhtarAdapter (
    private val items: ArrayList<Athkar?>?
) :
    RecyclerView.Adapter<AkhtarAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemAkhtarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        items?.get(position)?.let { holder.bind(it) }
    }

    inner class MyViewHolder(private val binding: ItemAkhtarBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Athkar) {
            item.apply {
                binding.tvDescription.text = text
            }
        }
    }
}