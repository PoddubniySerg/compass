package vac.test.core_api

interface MapFeature {

    val id: Long
    val type: String
    val cameraUrl: String
    val geometryType: String
    val longitude: Double
    val latitude: Double
}