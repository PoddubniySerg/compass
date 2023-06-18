package vac.test.data.di

import vac.test.core_api.MapFeaturesRepository
import vac.test.core_api.MapsComponent
import vac.test.data.repositories.MapFeaturesRepositoryImpl

internal class MapsComponentImpl : MapsComponent {

    private val mapFeaturesRepository = MapFeaturesRepositoryImpl()

    override fun getMapFeaturesRepository(): MapFeaturesRepository {
        return mapFeaturesRepository
    }
}