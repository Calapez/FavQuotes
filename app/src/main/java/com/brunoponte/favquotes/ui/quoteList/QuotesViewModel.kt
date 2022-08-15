package com.brunoponte.favquotes.ui.quoteList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brunoponte.favquotes.domainModels.Quote
import com.brunoponte.favquotes.enums.FilterType
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
    private var page = 1

    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val quotes: MutableLiveData<List<Quote>> = MutableLiveData(listOf())
    val selectedFilter: MutableLiveData<FilterType> = MutableLiveData(FilterType.Quote)

    init {
        getFirstQuotes()
    }

    private fun getFirstQuotes() {
        // Fetches the first page of the quotes

        if (quotes.value?.isNotEmpty() == true) {
            // Already got Quotes
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            isLoading.postValue(true)
            val result = quotesRepo.getQuotes(PAGE_SIZE, 1, query, filterType)
            page += 1
            isLoading.postValue(false)

            quotes.postValue(result)
        }
    }

    fun onEndReached() {
        if (!isLoading.value!!) {
            // Reached end of current page and isn't loading quotes. Must load next page.
            getNextPage()
        }
    }

    fun onQuoteFilterClicked() {
        if (selectedFilter.value == FilterType.Quote) {
            return
        }

        selectedFilter.value = FilterType.Quote
        searchQuotes(query, forceSearch = true)
    }

    fun onTagFilterClicked() {
        if (selectedFilter.value == FilterType.Tag) {
            return
        }

        selectedFilter.value = FilterType.Tag
        searchQuotes(query, forceSearch = true)
    }

    fun onAuthorFilterClicked() {
        if (selectedFilter.value == FilterType.Author) {
            return
        }

        selectedFilter.value = FilterType.Author
        searchQuotes(query, forceSearch = true)
    }

    fun onTagClicked(tag: String) {
        selectedFilter.value = FilterType.Tag
        searchQuotes(tag, forceSearch = true)
    }

    private fun getNextPage() {
        CoroutineScope(Dispatchers.IO).launch {
            isLoading.postValue(true)

            // Prevents this to be called on first page load
            if (page > 1) {
                val result = quotesRepo.getQuotes(PAGE_SIZE, page, query, filterType)

                // Append quotes
                val current = ArrayList(quotes.value)
                current.addAll(result)
                quotes.postValue(current)

                page += 1
            }
            isLoading.postValue(false)
        }
    }

    fun searchQuotes(newQuery: String?, forceSearch: Boolean = false) {
        if (query == newQuery && !forceSearch) {
            return
        }

        query = newQuery ?: ""
        page = 1

        CoroutineScope(Dispatchers.IO).launch {
            isLoading.postValue(true)
            val result = quotesRepo.getQuotes(PAGE_SIZE, page, query, filterType)
            page += 1
            isLoading.postValue(false)

            quotes.postValue(result)
        }
    }

    private val filterType
        get() = when(selectedFilter.value) {
            FilterType.Tag -> "tag"
            FilterType.Author -> "author"
            else -> null
        }

    companion object {
        const val PAGE_SIZE = 25
    }
}