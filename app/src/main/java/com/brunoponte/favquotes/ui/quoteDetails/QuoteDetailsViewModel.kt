package com.brunoponte.favquotes.ui.quoteDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brunoponte.favquotes.domainModels.Quote
import com.brunoponte.favquotes.repository.IQuotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuoteDetailsViewModel
@Inject
constructor(
    private val quotesRepo: IQuotesRepository
) : ViewModel() {

    val selectedQuote: MutableLiveData<Quote?> = MutableLiveData(null)

    fun getQuoteFromId(quoteId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            val quote = quotesRepo.getQuoteById(quoteId)
            selectedQuote.postValue(quote)
        }
    }
}