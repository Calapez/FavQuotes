package com.brunoponte.favquotes.ui.quoteList

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
class QuotesViewModel
@Inject
constructor(
    private val quotesRepo: IQuotesRepository,
) : ViewModel() {

    private var query = ""
    private var scrollPosition = 0
    private var page = 1

    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val quotes: MutableLiveData<List<Quote>> = MutableLiveData(listOf())

    fun getFirstQuotes() {
        // Fetches the first page of the repos

        if (quotes.value?.isNotEmpty() == true) {
            // Already got repos
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            isLoading.postValue(true)
            val result = quotesRepo.getQuotes(PAGE_SIZE, 1, query)
            page += 1
            isLoading.postValue(false)

            quotes.postValue(result)
        }
    }

    fun onChangeQuoteScrollPosition(position: Int) {
        scrollPosition = position

        if (reachedEndOfList() && !isLoading.value!!) {
            // Reached end of current page and isn't loading quotes. Must load next page.
            getNextPage()
        }
    }

    private fun getNextPage() {
        CoroutineScope(Dispatchers.IO).launch {
            if (reachedEndOfList()) {
                isLoading.postValue(true)

                // Prevents this to be called on first page load
                if (page > 1) {
                    val result = quotesRepo.getQuotes(PAGE_SIZE, page, query)

                    // Append quotes
                    val current = ArrayList(quotes.value)
                    current.addAll(result)
                    quotes.postValue(current)

                    page += 1
                }
                isLoading.postValue(false)
            }
        }
    }

    fun searchQuotes(newQuery: String?) {
        query = newQuery ?: ""
        page = 1

        CoroutineScope(Dispatchers.IO).launch {
            isLoading.postValue(true)
            val result = quotesRepo.getQuotes(PAGE_SIZE, page, query)
            page += 1
            isLoading.postValue(false)

            quotes.postValue(result)
        }
    }

    private fun reachedEndOfList() = scrollPosition >= quotes.value!!.size - 1

    companion object {
        const val PAGE_SIZE = 25
    }
}