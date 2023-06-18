package vac.test.feature_maps

import android.Manifest
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.mapbox.android.core.permissions.PermissionsManager
import java.lang.ref.WeakReference

internal class LocationPermissionHelper(
    private val fragment: Fragment,
    private val onMapReady: () -> Unit
) {

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    private var launcher =
        fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map.isNotEmpty() && map.values.all { it }) {
                onMapReady()
            } else {
                Toast.makeText(
                    fragment.requireContext(),
                    "Permissions are not granted",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    fun checkPermissions() {
        val activity = WeakReference(fragment.requireActivity())
        if (PermissionsManager.areLocationPermissionsGranted(activity.get())) {
            onMapReady()
        } else {
            launcher.launch(REQUIRED_PERMISSIONS)
        }
    }
}