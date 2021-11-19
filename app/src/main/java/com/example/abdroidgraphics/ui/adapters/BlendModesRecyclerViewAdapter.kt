package com.example.abdroidgraphics.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.abdroidgraphics.databinding.BlendItemBinding
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class BlendModesRecyclerViewAdapter :
    ListAdapter<String, BlendModesRecyclerViewAdapter.ViewHolder>(DiffCallBack()) {
    private val clickChannel: Channel<String> = Channel()
    fun clicksFlow(): Flow<String> = clickChannel.receiveAsFlow()
    private var currentPosition = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            BlendItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = currentList[position]
        holder.bind(currentItem)
    }

    inner class ViewHolder(private val binding: BlendItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var mode = ""
            init {
                itemView.setOnClickListener {
                    currentPosition = currentList.indexOf(mode)
                    notifyDataSetChanged()
                    clickChannel.trySend(mode)
                }
            }

        fun bind(name: String) {
            binding.blendName.text = name
            mode = name
            if(adapterPosition != currentPosition) {
                binding.blendName.setTextColor(Color.WHITE)
            } else {
                binding.blendName.setTextColor(Color.GRAY)
            }
        }

    }

    class DiffCallBack : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem
    }

}