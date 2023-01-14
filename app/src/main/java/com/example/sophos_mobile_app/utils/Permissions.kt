package com.example.sophos_mobile_app.utils

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sophos_mobile_app.R

class PermissionsFragment : Fragment() {

    private val args: PermissionsFragmentArgs by navArgs()
    private lateinit var permissionsValues: HashMap<String, String>
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            when (args.permissionCode) {
                Manifest.permission.CAMERA -> navigateToCamera()
                Manifest.permission.READ_EXTERNAL_STORAGE -> navigateToGallery()
                Manifest.permission.ACCESS_COARSE_LOCATION -> navigateToMaps(true)
            }
        } else {
            when (args.permissionCode) {
                Manifest.permission.CAMERA -> {
                    showPermissionDeniedMessage(args.permissionCode)
                    navigateToMenu()
                }
                Manifest.permission.READ_EXTERNAL_STORAGE -> {
                    showPermissionDeniedMessage(args.permissionCode)
                    navigateToMenu()
                }
                Manifest.permission.ACCESS_COARSE_LOCATION -> {
                    navigateToMaps(false)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionsValues = hashMapOf(
            Manifest.permission.CAMERA to getString(R.string.camera_permission),
            Manifest.permission.READ_EXTERNAL_STORAGE to getString(R.string.read_external_permission),
            Manifest.permission.ACCESS_COARSE_LOCATION to getString(R.string.location_permission)
        )
        checkPermissions(args.permissionCode)
    }

    private fun checkPermissions(permission: String) {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                permission,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            checkPermission(permission)
        } else {
            navigateToFragment()
        }
    }

    private fun checkPermission(permission: String) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission)) {
            CustomDialog(
                getString(R.string.grant_permission),
                getString(
                    R.string.request_rationale_permission,
                    permissionsValues[args.permissionCode]
                )
            ) {
                if (args.permissionCode == Manifest.permission.ACCESS_COARSE_LOCATION) {
                    navigateToMaps(false)
                } else {
                    findNavController().popBackStack()
                }
            }.show(childFragmentManager, CustomDialog.TAG)
        } else {
            requestPermissionLauncher.launch(permission)
        }
    }

    private fun showPermissionDeniedMessage(permissionCode: String) {
        Toast.makeText(
            requireContext(),
            getString(R.string.permission_denied_format, permissionsValues[permissionCode]),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun navigateToFragment() {
        when (args.permissionCode) {
            Manifest.permission.CAMERA -> navigateToCamera()
            Manifest.permission.READ_EXTERNAL_STORAGE -> navigateToGallery()
            Manifest.permission.ACCESS_COARSE_LOCATION -> navigateToMaps(true)
        }
    }

    private fun navigateToMenu() {
        findNavController().navigateUp()
    }

    private fun navigateToMaps(isPermissionGranted: Boolean) {
        if (isPermissionGranted) {
            findNavController().navigate(
                PermissionsFragmentDirections.actionPermissionsFragmentToOfficesFragmentDestination(
                    true
                )
            )
        } else {
            findNavController().navigate(
                PermissionsFragmentDirections.actionPermissionsFragmentToOfficesFragmentDestination(
                    false
                )
            )
        }
    }

    private fun navigateToGallery() {
        findNavController().navigate(
            PermissionsFragmentDirections.actionPermissionsFragmentToGalleryFragment()
        )
    }

    private fun navigateToCamera() {
        findNavController().navigate(
            PermissionsFragmentDirections.actionPermissionsFragmentToCameraFragment()
        )
    }

}