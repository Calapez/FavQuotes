package com.brunoponte.favquotes.ui.quoteDetails

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brunoponte.favquotes.R
import com.brunoponte.favquotes.databinding.QuoteDetailsFragmentBinding
import com.brunoponte.favquotes.domainModels.Quote
import com.brunoponte.favquotes.ui.TagListAdapter
import com.brunoponte.favquotes.ui.TagListInteraction
import com.brunoponte.favquotes.ui.dashboard.DashboardFragmentDirections
import com.brunoponte.favquotes.ui.quoteList.QuotesFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuoteDetailsFragment : Fragment(), TagListInteraction {

    private lateinit var binding: QuoteDetailsFragmentBinding

    private val viewModel: QuoteDetailsViewModel by viewModels()
    private val args: QuoteDetailsFragmentArgs by navArgs()

    val listAdapter = TagListAdapter(this).apply {
        stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy
            .PREVENT_WHEN_EMPTY
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = QuoteDetailsFragmentBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val quoteId = args.quoteId
        viewModel.getQuoteFromId(quoteId)

        binding.tagsRecyclerView.let { recyclerView ->
            recyclerView.layoutManager = LinearLayoutManager(context).also {
                it.orientation = LinearLayoutManager.HORIZONTAL
            }

            recyclerView.adapter = listAdapter
        }

        setupViewModelObservers()
    }

    override fun onClick(position: Int, tag: String?) {
        if (tag == null) {
            return
        }

        findNavController().popBackStack(R.id.dashboardFragment, false)
        val action = DashboardFragmentDirections.actionDashboardFragmentToQuotesFragment(tag)
        findNavController().navigate(action)
    }

    private fun setupViewModelObservers() {
        viewModel.selectedQuote.observe(viewLifecycleOwner) { quote ->
            setupView(quote)
        }
    }

    private fun setupView(quote: Quote?) {
        quote?.let {
            val notApplicableText = getString(R.string.not_applicable)

            binding.textQuote.text =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(quote.body ?: notApplicableText, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    Html.fromHtml(quote.body ?: notApplicableText)
                }

            binding.textAuthor.text = quote.author?.let {
                "- ${quote.author}"
            } ?: notApplicableText

            quote.tags?.let {
                listAdapter.submitList(it)
            }

            binding.textDialogue.text = if (quote.isDialogue == true) "Yes" else "No"

            binding.textFavorites.text = quote.favorites?.toString() ?: notApplicableText

            binding.textUpvoted.text = quote.upvotes?.toString() ?: notApplicableText

            binding.textDownvotes.text = quote.downvotes?.toString() ?: notApplicableText

            binding.textCreatedBy.text = quote.authorLink ?: notApplicableText
        }

    }
}