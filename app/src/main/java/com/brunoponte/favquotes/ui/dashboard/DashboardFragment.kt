package com.brunoponte.favquotes.ui.dashboard

import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.brunoponte.favquotes.R
import com.brunoponte.favquotes.databinding.DashboardFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private lateinit var binding: DashboardFragmentBinding

    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DashboardFragmentBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModelObservers()

        binding.buttonQuotes.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_quotesFragment)
        }
    }

    private fun setupViewModelObservers() {
        viewModel.quote.observe(viewLifecycleOwner) { quote ->
            binding.textQuote.visibility = if (quote == null) View.INVISIBLE else View.VISIBLE
            binding.textQuote.text =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(quote?.body ?: "", Html.FROM_HTML_MODE_COMPACT)
                } else {
                    Html.fromHtml(quote?.body ?: "")
                }
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressIndicator.visibility = if (loading) View.VISIBLE else View.INVISIBLE
        }
    }
}