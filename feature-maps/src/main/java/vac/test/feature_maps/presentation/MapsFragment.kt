package vac.test.feature_maps.presentation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.ViewAnnotationAnchor
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.viewannotation.ViewAnnotationManager
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import vac.test.core_api.MapFeature
import vac.test.core_di.MapsInjector
import vac.test.feature_maps.LocationPermissionHelper
import vac.test.feature_maps.R
import vac.test.feature_maps.databinding.MapsFragmentBinding
import vac.test.feature_maps.databinding.MarkerViewBinding
import vac.test.feature_maps.databinding.PlayerViewBinding

class MapsFragment : Fragment() {

    companion object {
        const val GEO_POINT_KEY = "geo_point"
        const val CAMERA_ZOOM_KEY = "camera_zoom"
    }

    private lateinit var mapView: MapView
    private lateinit var locationPermissionHelper: LocationPermissionHelper

    private val viewModel by viewModels<MapsViewModel> {
        val mapsComponent = requireContext().applicationContext as MapsInjector
        MapsViewModelFactory(mapsComponent.getMapsDiComponent())
    }

    private val onIndicatorBearingChangedListener = OnIndicatorBearingChangedListener {
        mapView.getMapboxMap().setCamera(CameraOptions.Builder().bearing(it).build())
    }

    private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
        mapView.getMapboxMap().setCamera(CameraOptions.Builder().center(it).build())
        mapView.gestures.focalPoint = mapView.getMapboxMap().pixelForCoordinate(it)
    }

    private val onMoveListener = object : OnMoveListener {
        override fun onMoveBegin(detector: MoveGestureDetector) {
            onCameraTrackingDismissed()
        }

        override fun onMove(detector: MoveGestureDetector): Boolean {
            return false
        }

        override fun onMoveEnd(detector: MoveGestureDetector) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = MapsFragmentBinding.inflate(inflater, container, false)
        mapView = binding.mapView
        setLocation(savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.mapFeaturesFlow.onEach { markers ->
            addAnnotationToMap(markers)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    @SuppressLint("Lifecycle")
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    @SuppressLint("Lifecycle")
    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    @SuppressLint("Lifecycle")
    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    @SuppressLint("Lifecycle")
    override fun onDestroy() {
        super.onDestroy()
        mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.gestures.removeOnMoveListener(onMoveListener)
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val center = mapView.getMapboxMap().cameraState.center
        val zoom = mapView.getMapboxMap().cameraState.zoom
        outState.putSerializable(GEO_POINT_KEY, center)
        outState.putDouble(CAMERA_ZOOM_KEY, zoom)
    }

    private fun setLocation(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            locationPermissionHelper =
                LocationPermissionHelper(this) { onMapReady() }
            locationPermissionHelper.checkPermissions()
        } else {
            val zoom = savedInstanceState.getDouble(CAMERA_ZOOM_KEY)
            val point = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                savedInstanceState.getSerializable(GEO_POINT_KEY, Point::class.java)
            } else {
                savedInstanceState.getSerializable(GEO_POINT_KEY) as Point
            }
            initLocationMarker()
            mapView.getMapboxMap()
                .setCamera(CameraOptions.Builder().center(point).zoom(zoom).build())
            viewModel.getMarkers()
        }
    }

    private fun onMapReady() {
        mapView.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .zoom(14.0)
                .build()
        )
        mapView.getMapboxMap().loadStyleUri(
            Style.MAPBOX_STREETS
        ) {
            initLocationMarker()
            initLocationComponent()
            setupGesturesListener()
            viewModel.getMarkers()
        }
    }

    private fun addAnnotationToMap(markers: List<MapFeature>) {
        val viewAnnotationsManager = mapView.viewAnnotationManager
        markers.forEach { marker ->
            val view = viewAnnotationsManager.addViewAnnotation(
                R.layout.marker_view,
                viewAnnotationOptions {
                    geometry(Point.fromLngLat(marker.longitude, marker.latitude))
                    allowOverlap(true)
                    anchor(ViewAnnotationAnchor.BOTTOM)
                })
            val markerViewBinding = MarkerViewBinding.bind(view)
            markerViewBinding.markerButton.setOnClickListener { onClickMarker(marker) }
        }
        setPlayers()
    }

    private fun setPlayers() {
        if (viewModel.players.isNotEmpty()) {
            val manager = mapView.viewAnnotationManager
            viewModel.players.entries.forEach { playersEntry ->
                val view = getPlayerView(manager, playersEntry.key)
                PlayerViewBinding.bind(view).apply {
                    val player = playersEntry.value
                    videoView.player = player
                    closeButton.setOnClickListener {
                        manager.removeViewAnnotation(view)
                        player.stop()
                        viewModel.removePlayer(playersEntry.key)
                    }
                }
            }
        }
    }

    private fun setupGesturesListener() {
        mapView.gestures.addOnMoveListener(onMoveListener)
    }

    private fun initLocationMarker() {
        val locationComponentPlugin = mapView.location
        locationComponentPlugin.updateSettings {
            this.enabled = true
            this.locationPuck = LocationPuck2D(
                bearingImage = AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.my_location_icon,
                ),
                shadowImage = AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.mapbox_user_icon_shadow,
                ),
                scaleExpression = interpolate {
                    linear()
                    zoom()
                    stop {
                        literal(0.0)
                        literal(0.6)
                    }
                    stop {
                        literal(20.0)
                        literal(1.0)
                    }
                }.toJson()
            )
        }
    }

    private fun initLocationComponent() {
        val locationComponentPlugin = mapView.location
        locationComponentPlugin.addOnIndicatorPositionChangedListener(
            onIndicatorPositionChangedListener
        )
        locationComponentPlugin.addOnIndicatorBearingChangedListener(
            onIndicatorBearingChangedListener
        )
    }

    private fun onCameraTrackingDismissed() {
        Toast.makeText(requireContext(), "onCameraTrackingDismissed", Toast.LENGTH_SHORT).show()
        mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView.gestures.removeOnMoveListener(onMoveListener)
    }

    private fun onClickMarker(marker: MapFeature) {
        val point = Point.fromLngLat(marker.longitude, marker.latitude)
        mapView.getMapboxMap().setCamera(CameraOptions.Builder().center(point).build())
        val viewAnnotationsManager = mapView.viewAnnotationManager
        val view = getPlayerView(viewAnnotationsManager, point)
        PlayerViewBinding.bind(view).apply {
            closeButton.setOnClickListener {
                viewAnnotationsManager.removeViewAnnotation(view)
                val player = videoView.player as ExoPlayer
                player.stop()
                viewModel.removePlayer(point)
            }
            play(this, marker.cameraUrl, point)
        }
    }

    private fun getPlayerView(viewAnnotationsManager: ViewAnnotationManager, point: Point): View {
        return viewAnnotationsManager.addViewAnnotation(
            R.layout.player_view,
            viewAnnotationOptions {
                geometry(point)
                allowOverlap(true)
            })
    }

    private fun play(playerViewBinding: PlayerViewBinding, url: String, point: Point) {
        val player = ExoPlayer.Builder(requireContext()).build()
        playerViewBinding.videoView.player = player
        player.setMediaItem(MediaItem.fromUri(url))
        player.prepare()
        viewModel.addPlayer(point, player)
        player.play()
    }
}