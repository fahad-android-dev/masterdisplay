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


    /*Voices Config Ids*/

    const val ENGLISH = "English"
    const val ARABIC = "Arabic"
    const val ENGLISH_ARABIC = "English First Arabic Second"
    const val ARABIC_ENGLISH = "Arabic First English Second"

    const val MALE = "M"
    const val FEMALE = "F"



    val configFile = File(
        Environment.getExternalStorageDirectory()
            .toString() + "/MasterDisplay_Config/Config.xls"
    )

    const val SCROLL_TEXT = "ScrollText"
    const val SCROLL_TEXT_COLOR = "ScrollTextColor"
    const val SERVICES = "Services"




    const val COUNTER_TILE_COLOR = "CounterTileColor"
    const val COUNTER_TILE_TEXT_COLOR = "CounterTileTextColor"
    const val COUNTER_TILE_RADIUS = "CounterTileRadius"

    const val TOKEN_TILE_COLOR = "TokenTileColor"
    const val TOKEN_TILE_TEXT_COLOR = "TokenTileTextColor"
    const val TOKEN_TILE_RADIUS = "TokenTileRadius"

}