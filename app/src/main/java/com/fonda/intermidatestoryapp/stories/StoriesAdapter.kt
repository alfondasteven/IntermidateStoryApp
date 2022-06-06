package com.fonda.intermidatestoryapp.stories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fonda.intermidatestoryapp.databinding.ItemListStoryBinding
import com.fonda.intermidatestoryapp.model.ListStoryItem

class StoriesAdapter : PagingDataAdapter<ListStoryItem, StoriesAdapter.ViewHolder>(DIFF_CALLBACK) {

  //  private var oldStoryItem = emptyList<ListStoryItem>()
    private var onItemClickCallback : OnItemClickCallback? =null

    fun setOnItemClickCallback (onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ViewHolder(val binding: ItemListStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listStoryItem: ListStoryItem){
            binding.root.setOnClickListener{
                onItemClickCallback?.onItemClicked(listStoryItem)

            }
            binding.apply {
                Glide.with(itemView)
                    .load(listStoryItem.photoUrl)
                    .into(imgItemPhoto)
                tvItemName.text = listStoryItem.name
            }
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemListStoryBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)
        return  ViewHolder((view))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val data = getItem(position)
        if(data != null){
            viewHolder.bind(data)
        }
    }


    interface OnItemClickCallback{
        fun onItemClicked(data : ListStoryItem)

    }
    companion object{
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>(){
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}