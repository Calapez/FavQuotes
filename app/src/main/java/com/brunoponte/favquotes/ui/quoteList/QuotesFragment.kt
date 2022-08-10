package com.brunoponte.favquotes.ui.quoteList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brunoponte.favquotes.R
import com.brunoponte.favquotes.databinding.QuotesFragmentBinding
import com.brunoponte.favquotes.domainModels.Quote
import com.brunoponte.favquotes.ui.TagListInteraction
import com.brunoponte.favquotes.ui.quoteDetails.QuoteDetailsFragmentArgs
import com.brunoponte.favquotes.ui.quoteList.listAdapter.QuoteListAdapter
import com.brunoponte.favquotes.ui.quoteList.listAdapter.QuoteListInteraction
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuotesFragment : Fragment(), QuoteListInteraction, TagListInteraction {

    private lateinit var binding: QuotesFragmentBinding
    private val viewModel: QuotesViewModel by viewModels()
    private val args: QuotesFragmentArgs by navArgs()

    private val listAdapter = QuoteListAdapter(this, this).apply {
        stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy
            .PREVENT_WHEN_EMPTY
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getFirstQuotes()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = QuotesFragmentBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedTag = args.selectedTag
        if (selectedTag.isNotEmpty()) {
            onClick(-1, selectedTag)
        }

        binding.recyclerView.let { recyclerView ->
            recyclerView.layoutManager = LinearLayoutManager(context).also {
                it.orientation = LinearLayoutManager.VERTICAL
            }

            recyclerView.adapter = listAdapter
        }

        setupViewModelObservers()

        binding.searchView.doOnTextChanged { text, _, _, _ ->
            viewModel.searchQuotes(text.toString())
        }

        binding.cardContent.setOnClickListener {
            viewModel.onContentFilterClicked()
        }

        binding.cardTag.setOnClickListener {
            viewModel.onTagFilterClicked()
        }

        binding.cardAuthor.setOnClickListener {
            viewModel.onAuthorFilterClicked()
        }
    }

    override fun onClick(position: Int, quote: Quote) {
        // Navigate to Details Fragment
        val action = QuotesFragmentDirections.actionQuotesFragmentToQuoteDetailsFragment(quote.id)
        findNavController().navigate(action)
    }

    override fun onIndexReached(index: Int) {
        // Reached a new element in Recycler View, update scroll position in VM
        viewModel.onChangeQuoteScrollPosition(index)
    }

    override fun onClick(position: Int, tag: String?) {
        if (tag == null) {
            return
        }

        viewModel.onTagClicked(tag)
        binding.searchView.setText(tag)
        binding.searchView.setSelection(tag.length)
    }

    private fun setupViewModelObservers() {
        viewModel.quotes.observe(viewLifecycleOwner) { quotes ->
            listAdapter.submitList(quotes.map { it.copy() })
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.isContentFilterSelected.observe(viewLifecycleOwner) { isContentFilterSelected ->
            binding.cardContent.setCardBackgroundColor(resources.getColor(
                if (isContentFilterSelected) R.color.selectedColor
                else R.color.white
            ))

        }

        viewModel.isTagFilterSelected.observe(viewLifecycleOwner) { isTagFilterSelected ->
            binding.cardTag.setCardBackgroundColor(resources.getColor(
                if (isTagFilterSelected) R.color.selectedColor
                else R.color.white
            ))
        }

        viewModel.isAuthorFilterSelected.observe(viewLifecycleOwner) { isAuthorFilterSelected ->
            binding.cardAuthor.setCardBackgroundColor(resources.getColor(
                if (isAuthorFilterSelected) R.color.selectedColor
                else R.color.white
            ))
        }
    }
}