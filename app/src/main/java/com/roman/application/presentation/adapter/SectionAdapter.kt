package com.roman.application.presentation.adapter

import com.roman.application.domain.model.response.city.Cities

class SectionAdapter(private val data: Map<Int?, List<Cities>>?)/* :
    RecyclerView.Adapter<SectionAdapter.MySectionViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySectionViewHolder {
        val binding = ItemSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MySectionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: MySectionViewHolder, position: Int) {
        data?.apply {
            val section = keys.elementAt(position)
            holder.bind(get(section) ?: emptyList())
        }
    }

    inner class MySectionViewHolder(private val binding: ItemSectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photoList: List<PhotoResponse>?) {
            val itemAdapter = PhotosAdapter(photoList)
            binding.recyclerView.adapter = itemAdapter

        }
    }*/
