package com.roman.application.home.presentation.adapter


import com.roman.application.home.domain.model.response.city.Cities

class PhotosAdapter(private val photoList: List<Cities>?) /*:
    RecyclerView.Adapter<PhotosAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemPhotosBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return photoList?.size ?: 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(photoList?.get(position))
    }

    inner class MyViewHolder(private val binding: ItemPhotosBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photoItem: PhotoResponse?) {
            photoItem?.apply {
                binding.tvTitle.text = title
                binding.tvUrl.text = thumbnailUrl
            }
        }
    }*/
