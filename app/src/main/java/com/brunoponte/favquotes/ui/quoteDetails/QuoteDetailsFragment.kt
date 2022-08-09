package com.brunoponte.favquotes.ui.quoteDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.brunoponte.favquotes.R
import com.brunoponte.favquotes.databinding.QuoteDetailsFragmentBinding
import com.brunoponte.favquotes.domainModels.Quote
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuoteDetailsFragment : Fragment() {

    private lateinit var binding: QuoteDetailsFragmentBinding

    private val viewModel: QuoteDetailsViewModel by viewModels()
    private val args: QuoteDetailsFragmentArgs by navArgs()

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

        setupViewModelObservers()
    }

    private fun setupViewModelObservers() {
        viewModel.selectedQuote.observe(viewLifecycleOwner) { quote ->
            setupView(quote)
        }
    }

    private fun setupView(quote: Quote?) {
        quote?.let {
            val notApplicableText = getString(R.string.not_applicable)

            binding.repoNameText.text = quote.body ?: notApplicableText
        }

    }
}