package com.brunoponte.favquotes.ui.quoteList.listAdapter

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.brunoponte.favquotes.R
import com.brunoponte.favquotes.databinding.QuoteListItemBinding
import com.brunoponte.favquotes.databinding.TagListItemBinding
import com.brunoponte.favquotes.domainModels.Quote

class QuoteViewHolder(
    private val binding: QuoteListItemBinding,
    private val quoteListInteraction: QuoteListInteraction,
    private val tagListInteraction: TagListInteraction
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(quote: Quote, position: Int) {
        val listAdapter = TagListAdapter(tagListInteraction).apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy
                .PREVENT_WHEN_EMPTY
        }

        val notApplicableText = itemView.context.getString(R.string.not_applicable)

        binding.apply {
            textBody.text =  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(quote.body ?: "", Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(quote.body ?: "")
            }

            textAuthor.text = "By ${quote.author ?: notApplicableText}"

            tagsRecyclerView.let { recyclerView ->
                recyclerView.layoutManager = LinearLayoutManager(itemView.context).also {
                    it.orientation = LinearLayoutManager.HORIZONTAL
                }

                recyclerView.adapter = listAdapter
            }

            root.setOnClickListener {
                quoteListInteraction.onClick(position, quote)
            }
        }

        quote.tags?.let {
            listAdapter.submitList(it)
        }
    }
}

interface TagListInteraction {
    fun onClick(position: Int, quote: String?)
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