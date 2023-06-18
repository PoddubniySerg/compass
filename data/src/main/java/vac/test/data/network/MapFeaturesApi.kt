package vac.test.data.network

import retrofit2.Response
import retrofit2.http.GET
import vac.test.data.dto.MapFeaturesCollectionDto

internal interface MapFeaturesApi {

    companion object {
        private const val FEATURES_PATH =
        "/PoddubniySerg/ec24e9a786fa3d91201a1c7e70f13bbf/raw/356978c7053b610b373f877d8eb530d293092e9f/markers.json"
    }

    @GET(FEATURES_PATH)
    suspend fun getFeatures(): Response<MapFeaturesCollectionDto>
}