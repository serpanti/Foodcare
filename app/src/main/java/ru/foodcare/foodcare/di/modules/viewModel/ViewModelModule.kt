package ru.foodcare.foodcare.di.modules.viewModel

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.foodcare.foodcare.ui.viewModel.date.DateViewModel
import ru.foodcare.foodcare.ui.viewModel.meal.MealViewModel
import ru.foodcare.foodcare.ui.viewModel.product.ProductViewModel
import ru.foodcare.foodcare.ui.viewModel.theme.ThemeViewModel
import ru.foodcare.foodcare.ui.viewModel.weight.WeightViewModel

@Module
interface ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ProductViewModel::class)
    fun bindsProductViewModel(productViewModel: ProductViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MealViewModel::class)
    fun bindsMealViewModel(mealViewModel: MealViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DateViewModel::class)
    fun bindsDateViewModel(dateViewModel: DateViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WeightViewModel::class)
    fun bindsWeightViewModel(weightViewModel: WeightViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ThemeViewModel::class)
    fun bindsThemeViewModel(themeViewModel: ThemeViewModel): ViewModel
}
