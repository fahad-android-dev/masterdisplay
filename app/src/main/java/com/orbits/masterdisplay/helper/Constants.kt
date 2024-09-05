package com.orbits.masterdisplay.helper

import android.os.Build
import android.os.Environment
import java.io.File


object Constants {

    var DEVICE_TOKEN = ""
    val DEVICE_MODEL: String = Build.MODEL
    const val DEVICE_TYPE = "A" //passed in banners
    val OS_VERSION = Build.VERSION.RELEASE
    const val DATE_FORMAT = "yyyy-MM-dd hh:mm:ss"
    var DEVICE_DENSITY = 0.0
    val fontBold = "bold"
    val fontRegular = "regular"
    val fontMedium = "medium"
    val fontLight = "light"
    val fontRegularRev = "regular_reverse"
    const val TOOLBAR_ICON_ONE = "iconOne"
    const val TOOLBAR_ICON_TWO = "iconTwo"
    const val TOOLBAR_ICON_MENU = "iconMenu"
    const val TOOLBAR_ICON_BACK = "iconBack"




    const val APP_SETTING_PASSWORD = "keypad"

    val configFile = File(
        Environment.getExternalStorageDirectory()
            .toString() + "/MasterDisplay_Config/Config.xls"
    )

}