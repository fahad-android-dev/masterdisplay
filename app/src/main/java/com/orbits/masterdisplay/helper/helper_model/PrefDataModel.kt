package com.orbits.masterdisplay.helper.helper_model

data class ServerAddressModel(
    val ipAddress: String? = null,
    val port: String? = null,
    var services: String? = null,
    val code: String? = null,

)

data class DeepLinkModel(
    val PB_target : String= "" ,
    val PB_target_id : String = "",
    val PB_secondary_id : String = "",
    val PB_title_en : String = "",
    val PB_title_ar : String = "",
)

data class Device(
    val device_token: String? = null,
    val device_type: String? = null,
    val device_model: String? = null,
    val os_version: String? = null,
    val app_version: String? = null
)

data class AppConfigModel(
    var isLogoChecked: Boolean = false,
    var isPortraitChecked: Boolean = false,
    var isTimeChecked: Boolean = false,
    var isScrollArabic: Boolean = false,
    var scrollText: String? = null,
)
