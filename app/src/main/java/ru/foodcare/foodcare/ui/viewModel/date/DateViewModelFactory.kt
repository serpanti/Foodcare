package ru.foodcare.foodcare.ui.viewModel.date

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.foodcare.foodcare.domain.date.DateRepository
import kotlin.coroutines.CoroutineContext

class DateViewModelFactory(private val dateRepository: DateRepository,
                           private val context: CoroutineContext
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DateViewModel(dateRepository, context) as T
    }
}