package com.brunoponte.favquotes.ui.quoteList.listAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.brunoponte.favquotes.databinding.QuoteListItemBinding
import com.brunoponte.favquotes.domainModels.Quote
import com.brunoponte.favquotes.ui.TagListInteraction


interface QuoteListInteraction {
    fun onClick(position: Int, quote: Quote)

    fun onEndReached()
}

class QuoteListAdapter(
    private val quoteListInteraction: QuoteListInteraction,
    private val tagListInteraction: TagListInteraction
) : ListAdapter<Quote, QuoteViewHolder>(QuoteListAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val itemBinding = QuoteListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuoteViewHolder(itemBinding, quoteListInteraction, tagListInteraction)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        holder.bind(getItem(position), position)
        if (position >= itemCount - 1) {
            quoteListInteraction.onEndReached()
        }
    }

    private companion object : DiffUtil.ItemCallback<Quote>() {

        override fun areItemsTheSame(oldItem: Quote, newItem: Quote): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Quote, newItem: Quote): Boolean {
            return oldItem == newItem
        }
    }
}