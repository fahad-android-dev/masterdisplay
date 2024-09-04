package com.orbits.masterdisplay

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermissions()) {
            } else {
                //Toast.makeText(this, "already granted::", LENGTH_SHORT).show();
            }
        }

    }



    val isExternalStorageReadOnly: Boolean
        get() {
            val extStorageState = Environment.getExternalStorageState()
            if (Environment.MEDIA_MOUNTED_READ_ONLY == extStorageState) {
                return true
            }
            return false
        }

    val isExternalStorageAvailable: Boolean
        get() {
            val extStorageState = Environment.getExternalStorageState()
            if (Environment.MEDIA_MOUNTED == extStorageState) {
                return true
            }
            return false
        }


    private fun checkPermissions(): Boolean {
        val permission1 =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permission2 =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE)
        val permission3 =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
        val permission4 =
            ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE)
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (permission1 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (permission2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_WIFI_STATE)
        }
        if (permission3 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_NETWORK_STATE)
        }
        if (permission4 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CHANGE_WIFI_STATE)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray<String>(),
                100
            )
            return false
        }
        return true
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.size > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
            }
            return
        }
    }

}
