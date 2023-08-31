package com.example.proteinpro.util

import android.Manifest
import android.content.Context
import android.widget.Toast
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission


class permissionHelper(context: Context?) {

    var permissionlistener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
        }

        override fun onPermissionDenied(deniedPermissions: List<String>) {
            Toast.makeText(
                context,
                "Permission Denied\n$deniedPermissions",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun tedPermission (){
        val permissionResult =
            TedPermission.create()
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION)
                .check()
    }




}