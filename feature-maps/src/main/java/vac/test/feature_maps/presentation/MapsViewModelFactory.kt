package vac.test.feature_maps.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vac.test.core_api.MapsComponent
import vac.test.feature_maps.domain.GetMapFeaturesUseCase

internal class MapsViewModelFactory(
    private val mapsComponent: MapsComponent,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = MapsViewModel(
            mapsComponent.getMapFeaturesRepository(),
            GetMapFeaturesUseCase(mapsComponent.getMapFeaturesRepository())
        )
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return viewModel as T
        } else {
            throw IllegalArgumentException("Unknown class name")
        }
    }
}