package com.orbits.masterdisplay.helper.interfaces

interface AlertDialogInterface {
    fun onYesClick() {}
    fun onNoClick() {}
    fun onConnectionConfirm(ipAddress: String,port: String) {}
}