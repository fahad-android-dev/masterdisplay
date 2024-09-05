package com.orbits.masterdisplay.helper

import android.content.Context
import com.orbits.masterdisplay.helper.Extensions.asString
import com.orbits.masterdisplay.helper.helper_model.AppConfigModel
import com.orbits.masterdisplay.helper.helper_model.Device
import com.orbits.masterdisplay.helper.helper_model.ServerAddressModel
import com.orbits.masterdisplay.helper.helper_model.UserResponseModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

object PrefUtils {

    /**  -----------------------      USER DATA ---------------------------------- */
    fun Context.setUserDataResponse(result: UserResponseModel?) {
        val dt = DataStoreManager(this)
        runBlocking { dt.saveUserData(result) }
    }

    fun Context.getUserDataResponse(): UserResponseModel? {
        val dt = DataStoreManager(this)
        return runBlocking {
            dt.getUserData().first()
        }
    }

    fun Context.getConnectionCode(): String? {
        val dt = DataStoreManager(this)
        return runBlocking {
            dt.getUserData().firstOrNull()?.data?.connection_code
        }
    }

    fun Context.getUserId(): String {
        return getUserDataResponse()?.data?.id.asString()
    }

    fun Context.getUserName(): String {
        val userModel = getUserDataResponse()
        return userModel?.data?.firstName ?: ""
    }

    fun Context.isUserLoggedIn(): Boolean {
        return this.getUserDataResponse()?.data?.id != null
    }
    fun Context.isCodeVerified(): Boolean {
        return this.getUserDataResponse()?.data?.isCodeVerified != false
    }

    fun Context.getDeviceModel(): Device {
        return Device(
            device_model = Constants.DEVICE_MODEL,
            device_token = Constants.DEVICE_TOKEN,
            device_type = Constants.DEVICE_TYPE,
            //app_version = Constants.APP_VERSION,
            os_version = Constants.OS_VERSION
        )
    }


    /**  -----------------------  ------------------------------------  ---------------------------------- */

    /**  -----------------------      APP CONFIG         ---------------------------------- */

    fun Context.setAppConfig(result: AppConfigModel) {
        val dt = DataStoreManager(this)
        runBlocking { dt.saveAppConfig(result) }
    }

    fun Context.getAppConfig(): AppConfigModel? {
        val dt = DataStoreManager(this)
        return runBlocking { dt.getAppConfig().firstOrNull() }
    }



    fun Context.saveServerAddress(result: ServerAddressModel) {
        val dt = DataStoreManager(this)
        runBlocking { dt.saveServerAddress(result) }
    }

    fun Context.getServerAddress(): ServerAddressModel? {
        val dt = DataStoreManager(this)
        return runBlocking { dt.getServerAddress().firstOrNull() }
    }

    /**  -----------------------  ------------------------------------  ---------------------------------- */


}
