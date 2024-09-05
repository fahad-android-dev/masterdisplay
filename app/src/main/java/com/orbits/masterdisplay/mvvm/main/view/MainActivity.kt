package com.orbits.masterdisplay.mvvm.main.view

import android.Manifest
import android.app.DialogFragment
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.orbits.masterdisplay.R
import com.orbits.masterdisplay.databinding.ActivityMainBinding
import com.orbits.masterdisplay.helper.BaseActivity
import com.orbits.masterdisplay.helper.ConfigDialogFragment
import com.orbits.masterdisplay.helper.Constants
import com.orbits.masterdisplay.helper.Dialogs
import com.orbits.masterdisplay.helper.Extensions
import com.orbits.masterdisplay.helper.FileConfig.createExcelFile
import com.orbits.masterdisplay.helper.NetworkChecker
import com.orbits.masterdisplay.helper.NetworkMonitor
import com.orbits.masterdisplay.helper.PrefUtils.getAppConfig
import com.orbits.masterdisplay.helper.PrefUtils.getServerAddress
import com.orbits.masterdisplay.helper.interfaces.AlertDialogInterface
import com.orbits.masterdisplay.helper.interfaces.NetworkListener
import com.orbits.masterdisplay.mvvm.main.view_model.MainViewModel
import java.io.File
import java.io.FileNotFoundException

class MainActivity : BaseActivity()  , NetworkListener {
    private lateinit var binding: ActivityMainBinding
    private var networkChecker : NetworkChecker?= null
    lateinit var viewModel: MainViewModel
    private lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        networkChecker = NetworkChecker(this)
        networkChecker?.setNetworkListener(this)

        initializeFields()
        onClickListeners()


    }


    private fun initializeFields() {
        networkChecker?.start()

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermissions()) {
                val logFile = File(
                    Environment.getExternalStorageDirectory()
                        .toString() + "/MasterDisplay_Config"
                )
                if (!logFile.exists()) {
                    logFile.mkdir()
                }

                createDirIfNotExists()
            } else {
                //Toast.makeText(this, "already granted::", LENGTH_SHORT).show();
            }
        }

        initData()



        onBackPressedDispatcher.addCallback(this@MainActivity, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
            }
        })
    }

    private fun initData(){
        if (getAppConfig()?.isLogoChecked != true){
            hideLogoFragment()
        }

    }

    private fun hideLogoFragment() {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val fragment = fragmentManager.findFragmentById(R.id.fragmentLogo)
        fragment?.let { fragmentTransaction.hide(it) }
        fragmentTransaction.commit()
    }

    private fun onClickListeners(){
        binding.main.setOnLongClickListener {
            val newFragment = ConfigDialogFragment.newInstance()
            newFragment.show(supportFragmentManager, "ConfigDialogFragment")
            true
        }
    }

    private fun createDirIfNotExists() {
        try {
            if (isExternalStorageAvailable) {
                val companyLogoFile = File(
                    Environment.getExternalStorageDirectory()
                        .toString() + "/MasterDisplay_Config/Company_Logo"
                )
                if (!companyLogoFile.exists()) {
                    companyLogoFile.mkdirs()
                }

                if (!Constants.configFile.exists()) {
                    createExcelFile(Constants.configFile.absolutePath)
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
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

    override fun onSuccess() {
        println("here is connected")
        networkMonitor = NetworkMonitor(this) {
            // Network available, reconnect WebSocket
            if (!getServerAddress()?.ipAddress.isNullOrEmpty()){
                viewModel.connectWebSocket(
                    getServerAddress()?.ipAddress ?: "",
                    getServerAddress()?.port ?: ""
                )
            }
        }
        networkMonitor.registerNetworkCallback()
    }

    override fun onFailure() {
    }

}
