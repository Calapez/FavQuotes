package com.brunoponte.favquotes.ui.quoteList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brunoponte.favquotes.R
import com.brunoponte.favquotes.databinding.QuotesFragmentBinding
import com.brunoponte.favquotes.domainModels.Quote
import com.brunoponte.favquotes.ui.quoteList.listAdapter.QuoteListAdapter
import com.brunoponte.favquotes.ui.quoteList.listAdapter.QuoteListInteraction
import com.brunoponte.favquotes.ui.quoteList.listAdapter.TagListInteraction
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuotesFragment : Fragment(), QuoteListInteraction, TagListInteraction {

    private lateinit var binding: QuotesFragmentBinding
    private val viewModel: QuotesViewModel by viewModels()

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
            viewModel.onContentClicked()
        }

        binding.cardTag.setOnClickListener {
            viewModel.onTagClicked()
        }

        binding.cardAuthor.setOnClickListener {
            viewModel.onAuthorClicked()
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

    private fun setupViewModelObservers() {
        viewModel.quotes.observe(viewLifecycleOwner) { quotes ->
            listAdapter.submitList(quotes.map { it.copy() })
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.isContentSelected.observe(viewLifecycleOwner) { isContentSelected ->
            binding.cardContent.setCardBackgroundColor(resources.getColor(
                if (isContentSelected) R.color.selectedColor
                else R.color.white
            ))

        }

        viewModel.isTagSelected.observe(viewLifecycleOwner) { isTagSelected ->
            binding.cardTag.setCardBackgroundColor(resources.getColor(
                if (isTagSelected) R.color.selectedColor
                else R.color.white
            ))
        }

        viewModel.isAuthorSelected.observe(viewLifecycleOwner) { isAuthorSelected ->
            binding.cardAuthor.setCardBackgroundColor(resources.getColor(
                if (isAuthorSelected) R.color.selectedColor
                else R.color.white
            ))
        }
    }

    override fun onClick(position: Int, tag: String?) {
        Toast.makeText(requireContext(), "Clicked Tag!", Toast.LENGTH_SHORT).show()
    }
}