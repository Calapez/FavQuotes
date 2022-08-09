package com.brunoponte.favquotes.ui.dashboard

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
class DashboardViewModel
@Inject
constructor(
    private val quotesRepo: IQuotesRepository
) : ViewModel() {

    val quote: MutableLiveData<Quote?> = MutableLiveData(null)
    val loading: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        loadData()
    }

    private fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            loading.postValue(true)
            quote.postValue(quotesRepo.getQuoteOfDayDay())
            loading.postValue(false)
        }
    }
}