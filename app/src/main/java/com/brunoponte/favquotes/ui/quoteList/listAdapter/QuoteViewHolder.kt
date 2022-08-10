package com.brunoponte.favquotes.ui.quoteList.listAdapter

import android.os.Build
import android.text.Html
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brunoponte.favquotes.R
import com.brunoponte.favquotes.databinding.QuoteListItemBinding
import com.brunoponte.favquotes.domainModels.Quote
import com.brunoponte.favquotes.ui.TagListAdapter
import com.brunoponte.favquotes.ui.TagListInteraction

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

            textAuthor.text = quote.author?.let {
                "- ${quote.author}"
            } ?: notApplicableText

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
