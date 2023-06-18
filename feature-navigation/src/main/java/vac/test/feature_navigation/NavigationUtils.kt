package vac.test.feature_navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation

fun Fragment.navigateToParent(
    actionId: Int,
    hostId: Int? = null,
    data: Bundle? = null
) {
    val navController =
        if (hostId == null) {
            Navigation.findNavController(requireActivity(), R.id.app_nav_host)
        } else {
            Navigation.findNavController(requireActivity(), hostId)
        }

    navController.navigate(actionId, data)
}