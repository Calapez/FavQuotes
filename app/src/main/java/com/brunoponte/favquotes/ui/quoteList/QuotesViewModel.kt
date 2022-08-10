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
    val isContentFilterSelected: MutableLiveData<Boolean> = MutableLiveData(true)
    val isTagFilterSelected: MutableLiveData<Boolean> = MutableLiveData(false)
    val isAuthorFilterSelected: MutableLiveData<Boolean> = MutableLiveData(false)

    fun getFirstQuotes() {
        // Fetches the first page of the repos

        if (quotes.value?.isNotEmpty() == true) {
            // Already got repos
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

    fun onChangeQuoteScrollPosition(position: Int) {
        scrollPosition = position

        if (reachedEndOfList() && !isLoading.value!!) {
            // Reached end of current page and isn't loading quotes. Must load next page.
            getNextPage()
        }
    }

    fun onContentFilterClicked() {
        if (isContentFilterSelected.value == true) {
            return
        }

        isContentFilterSelected.value = true
        isTagFilterSelected.value = false
        isAuthorFilterSelected.value = false

        searchQuotes(query)
    }

    fun onTagFilterClicked() {
        if (isTagFilterSelected.value == true) {
            return
        }

        isContentFilterSelected.value = false
        isTagFilterSelected.value = true
        isAuthorFilterSelected.value = false

        searchQuotes(query)
    }

    fun onAuthorFilterClicked() {
        if (isAuthorFilterSelected.value == true) {
            return
        }

        isContentFilterSelected.value = false
        isTagFilterSelected.value = false
        isAuthorFilterSelected.value = true

        searchQuotes(query)
    }

    fun onTagClicked(tag: String) {
        isContentFilterSelected.value = false
        isTagFilterSelected.value = true
        isAuthorFilterSelected.value = false
    }

    private fun getNextPage() {
        CoroutineScope(Dispatchers.IO).launch {
            if (reachedEndOfList()) {
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
    }

    fun searchQuotes(newQuery: String?) {
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
        get() = if (isTagFilterSelected.value == true) { "tag" }
                else if (isAuthorFilterSelected.value == true) { "author" }
                else { null }

    private fun reachedEndOfList() = scrollPosition >= quotes.value!!.size - 1

    companion object {
        const val PAGE_SIZE = 25
    }
}