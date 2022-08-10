package com.brunoponte.favquotes.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.brunoponte.favquotes.R
import com.brunoponte.favquotes.databinding.TagListItemBinding

interface TagListInteraction {
    fun onClick(position: Int, tag: String?)
}

class TagListAdapter(
    private val interaction: TagListInteraction
) : ListAdapter<String, TagViewHolder>(TagListAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val itemBinding = TagListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TagViewHolder(itemBinding, interaction)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    private companion object : DiffUtil.ItemCallback<String>() {

        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}

class TagViewHolder(
    private val binding: TagListItemBinding,
    private val interaction: TagListInteraction
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(tag: String?, position: Int) {
        val notApplicableText = itemView.context.getString(R.string.not_applicable)

        binding.apply {
            textTag.text = tag ?: notApplicableText

            root.setOnClickListener {
                interaction.onClick(position, tag)
            }
        }
    }
}