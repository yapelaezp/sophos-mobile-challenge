package com.example.sophos_mobile_app.utils

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation

import androidx.navigation.fragment.navArgs
import com.example.sophos_mobile_app.R


private val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE)

class PermissionsFragment(): Fragment(){

    companion object{
        private const val CAMERA_PERMISSION = 1
        private const val READ_EXTERNAL_PERMISSION = 2
    }

    private val args: PermissionsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verifyPermissions()
    }

    private fun verifyPermissions() {
        when(args.permissionCode){
            PERMISSIONS_REQUIRED[0]  -> {
                checkCameraPermission()
            }
            PERMISSIONS_REQUIRED[1] -> {
                checkReadExternalStoragePermission()
            }
        }
    }

    private fun checkReadExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestReadExternalStoragePermission()
        }
        else{
           openGallery()
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestCameraPermission()
        }
        else{
            openCamera()
        }
    }

    private fun requestReadExternalStoragePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(context, "Negaste el permiso previamente, sorry", Toast.LENGTH_LONG).show()
        }
        else {
            requestPermissions(PERMISSIONS_REQUIRED, READ_EXTERNAL_PERMISSION)
            //ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS_REQUIRED, CAMERA_PERMISSION)
        }
    }

    private fun requestCameraPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CAMERA)){
            Toast.makeText(context, "Negaste el permiso previamente, sorry", Toast.LENGTH_LONG).show()
        }
        else {
            requestPermissions(PERMISSIONS_REQUIRED, CAMERA_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CAMERA_PERMISSION -> {
                if (PackageManager.PERMISSION_GRANTED == grantResults.firstOrNull()) {
                    // Take the user to the success fragment when permission is granted
                    Toast.makeText(context, "Permission request granted", Toast.LENGTH_LONG).show()
                    openCamera()
                } else {
                    Toast.makeText(context, "Permission request denied", Toast.LENGTH_LONG).show()
                }
            }
            READ_EXTERNAL_PERMISSION -> {
                if (grantResults.isNotEmpty() && PackageManager.PERMISSION_GRANTED == grantResults[1]) {
                    // Take the user to the success fragment when permission is granted
                    Toast.makeText(context, "Permission request granted", Toast.LENGTH_LONG).show()
                    openGallery()
                } else {
                    Toast.makeText(context, "Permission request denied", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun openCamera() {
        lifecycleScope.launchWhenStarted {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(
                PermissionsFragmentDirections.actionPermissionsFragmentToCameraFragment())
        }
    }

    private fun openGallery() {
        lifecycleScope.launchWhenStarted {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(
                PermissionsFragmentDirections.actionPermissionsFragmentToGalleryFragment())
        }
    }
}