package com.lovevery.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lovevery.databinding.ItemMessageBinding
import com.lovevery.domain.models.Message

class MessagesAdapter: ListAdapter<Message, MessagesAdapter.ViewHolder>(MESSAGES_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemMessageBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.apply {
                tvUsername.text = message.user
                tvSubject.text = message.subject
                tvMessage.text = message.message
            }
        }
    }

    companion object {
        private val MESSAGES_COMPARATOR = object: DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean =
                oldItem.message == newItem.message
                        && oldItem.user == newItem.user
                        && oldItem.subject == newItem.subject

        }
    }
}