package com.orbits.masterdisplay.helper

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.orbits.masterdisplay.R
import com.orbits.masterdisplay.databinding.LayoutConfigDialogBinding
import com.orbits.masterdisplay.helper.Global.getDimension
import com.orbits.masterdisplay.helper.PrefUtils.getServerAddress
import com.orbits.masterdisplay.helper.interfaces.AlertDialogInterface

object Dialogs {

    var customDialog: Dialog? = null
    var configDialog: Dialog? = null
    var codeDialog: Dialog? = null



    fun showConfigDialog(
        activity: Activity,
        alertDialogInterface: AlertDialogInterface,
    ) {
        try {
            configDialog = Dialog(activity)
            configDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            configDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val binding: LayoutConfigDialogBinding = DataBindingUtil.inflate(
                LayoutInflater.from(activity),
                R.layout.layout_config_dialog, null, false
            )
            configDialog?.setContentView(binding.root)
            val lp: WindowManager.LayoutParams = WindowManager.LayoutParams()
            lp.copyFrom(configDialog?.window?.attributes)
            lp.width = getDimension(activity, 300.00)
            lp.height = getDimension(activity, 250.00)
            lp.gravity = Gravity.CENTER
            configDialog?.window?.attributes = lp
            configDialog?.setCanceledOnTouchOutside(true)
            configDialog?.setCancelable(true)

            binding.edtAddress.setText(activity.getServerAddress()?.ipAddress)
            binding.edtPort.setText(activity.getServerAddress()?.port)

            binding.btnAlertPositive.setOnClickListener {
                if (binding.edtAddress.text.isEmpty()){
                    Toast.makeText(activity,"Please enter ip address", Toast.LENGTH_SHORT).show()
                }else if (binding.edtPort.text.isEmpty()){
                    Toast.makeText(activity,"Please enter port number", Toast.LENGTH_SHORT).show()
                }
                else{
                    configDialog?.dismiss()
                    alertDialogInterface.onConnectionConfirm(
                        binding.edtAddress.text.toString(),
                        binding.edtPort.text.toString(),
                    )
                }
            }
            configDialog?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



}
