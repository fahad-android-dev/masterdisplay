package com.orbits.masterdisplay.mvvm.main.view

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.orbits.masterdisplay.R
import com.orbits.masterdisplay.databinding.ActivityMainBinding
import com.orbits.masterdisplay.helper.BaseActivity
import com.orbits.masterdisplay.helper.ConfigDialogFragment
import com.orbits.masterdisplay.helper.Constants
import com.orbits.masterdisplay.helper.FileConfig.createExcelFile
import com.orbits.masterdisplay.helper.FileConfig.image_FilePaths
import com.orbits.masterdisplay.helper.FileConfig.readExcelFile
import com.orbits.masterdisplay.helper.FileConfig.readImageFile
import com.orbits.masterdisplay.helper.NetworkChecker
import com.orbits.masterdisplay.helper.NetworkMonitor
import com.orbits.masterdisplay.helper.PrefUtils.getAppConfig
import com.orbits.masterdisplay.helper.PrefUtils.getServerAddress
import com.orbits.masterdisplay.helper.PrefUtils.getUserDataResponse
import com.orbits.masterdisplay.helper.interfaces.NetworkListener
import com.orbits.masterdisplay.mvvm.main.view_model.MainViewModel
import java.io.File
import java.io.FileNotFoundException
import java.util.Locale

class MainActivity : BaseActivity()  , NetworkListener, TextToSpeech.OnInitListener {
    private lateinit var binding: ActivityMainBinding
    private var networkChecker : NetworkChecker?= null
    lateinit var viewModel: MainViewModel
    private lateinit var networkMonitor: NetworkMonitor
    private var pos = 0
    private var isAppStarted = false
    private lateinit var textToSpeech: TextToSpeech
    private var maleVoice: Voice? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        networkChecker = NetworkChecker(this)
        networkChecker?.setNetworkListener(this)
        textToSpeech = TextToSpeech(this, this)

        if (getAppConfig()?.isPortraitChecked != true){
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }


        initializeFields()
        onClickListeners()

    }


    private fun initializeFields() {
        if (!getServerAddress()?.ipAddress.isNullOrEmpty()){
            viewModel.connectWebSocket(
                getServerAddress()?.ipAddress ?: "",
                getServerAddress()?.port ?: ""
            )
        }

        isAppStarted = true
        networkChecker?.start()

        if (checkPermissions()) {
            val logFile = File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/MasterDisplay_Config"
            )
            if (!logFile.exists()) {
                logFile.mkdir()
            }
            createDirIfNotExists()
            initData()
        } else {
            //Toast.makeText(this, "already granted::", LENGTH_SHORT).show();
        }


        onBackPressedDispatcher.addCallback(this@MainActivity, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
            }
        })
    }

    private fun initData(){
        readImageFile("/MasterDisplay_Config/Background")
        if (image_FilePaths?.size == 1) {
            binding.main.background = Drawable.createFromPath(image_FilePaths?.get(pos))
        }

        if (getAppConfig()?.isLogoChecked != true){
            hideLogoFragment()
        }

        val excelValue = readExcelFile(
            Environment.getExternalStorageDirectory()
                .toString() + "/MasterDisplay_Config/Config.xls")


        val scrollTextValue = excelValue[Constants.SCROLL_TEXT]
        val scrollTextColor = excelValue[Constants.SCROLL_TEXT_COLOR]
        if (scrollTextValue != null) {
            binding.scrollText.startScroll()
            binding.scrollText.text = scrollTextValue
//            binding.scrollText.setTextColor(Color.parseColor(scrollTextColor))
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
            val intent = Intent(this@MainActivity, ConfigDialogFragment::class.java)
            startActivity(intent)
            true
        }
    }

    private fun createDirIfNotExists() {
        try {
            if (isExternalStorageAvailable) {
                val topImages = File(
                    Environment.getExternalStorageDirectory()
                        .toString() + "/MasterDisplay_Config/TopImages"
                )
                val counterTokenImages = File(
                    Environment.getExternalStorageDirectory()
                        .toString() + "/MasterDisplay_Config/CounterTokenImages"
                )
                val fileVideoDir = File(
                    Environment.getExternalStorageDirectory()
                        .toString() + "/MasterDisplay_Config/Advertise videos"
                )
                val fileBg = File(
                    Environment.getExternalStorageDirectory()
                        .toString() + "/MasterDisplay_Config/Background"
                )

                if (!fileVideoDir.exists()) {
                    fileVideoDir.mkdirs()
                }
                if (!fileBg.exists()) {
                    fileBg.mkdirs()
                }
                if (!fileBg.exists()) {
                    fileBg.mkdirs()
                }
                if (!topImages.exists()) {
                    topImages.mkdirs()
                }
                if (!counterTokenImages.exists()) {
                    counterTokenImages.mkdirs()
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
            if (!isAppStarted){
                if (!getServerAddress()?.ipAddress.isNullOrEmpty()){
                    viewModel.sendReConnectionMessage()
                }
            }
            isAppStarted = false
        }
        networkMonitor.registerNetworkCallback()
    }

    override fun onFailure() {

    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            var langResult = textToSpeech.setLanguage(Locale.US)


            when (getUserDataResponse()?.voice_selected) {
                Constants.ENGLISH -> {
                    langResult = textToSpeech.setLanguage(Locale.US)
                }
                Constants.ARABIC -> {
                    langResult = textToSpeech.setLanguage(Locale("ar"))
                }
                Constants.ENGLISH_ARABIC -> {
                    langResult = textToSpeech.setLanguage(Locale.ENGLISH)
                }
                Constants.ARABIC_ENGLISH -> {
                    langResult = textToSpeech.setLanguage(Locale.ENGLISH)
                }
            }

            if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                println("here is text speech 111")
            }
        } else {
            println("here is text speech 222")
        }
    }

    private fun speakText(text: String , id : String ?= "") {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, id)
    }


    fun callTokens(token:String,counterId: String){
        println("here is token called")
        val customMsgEn =
            getUserDataResponse()?.msg_en
                ?.replace("<token>", token)
                ?.replace("<counter>", counterId)

        val customMsgAr =
            getUserDataResponse()?.msg_ar
                ?.replace("<token>", token)
                ?.replace("<counter>", counterId)

        println("here is token called voice ${getUserDataResponse()?.voice_selected}")

        when (getUserDataResponse()?.voice_selected) {
            Constants.ENGLISH -> {
                textToSpeech.language = Locale.US
                setEnMaleVoice()
                speakText(customMsgEn ?: "")
            }
            Constants.ARABIC -> {
                textToSpeech.language = Locale.US
                setArMaleVoice()
                speakText(customMsgAr ?: "")
            }
            Constants.ENGLISH_ARABIC -> {
                println("here is gender 000 ${getUserDataResponse()?.voice_gender}")
                textToSpeech.language = Locale.US
                setEnMaleVoice()
                speakText(customMsgEn ?: "", Constants.ENGLISH)
                textToSpeech.setOnUtteranceCompletedListener { id ->
                    if (id == Constants.ENGLISH){
                        setArMaleVoice()
                        speakText(customMsgAr ?: "")
                    }
                }

            }
            Constants.ARABIC_ENGLISH -> {
                setArMaleVoice()
                textToSpeech.speak(customMsgAr, TextToSpeech.QUEUE_FLUSH, null, Constants.ARABIC)
                textToSpeech.setOnUtteranceCompletedListener { id ->
                    if (id == Constants.ARABIC){
                        textToSpeech.language = Locale.US
                        setEnMaleVoice()
                        speakText(customMsgEn ?: "")
                    }

                }

            }
        }
    }

    private fun setEnMaleVoice() {
        val voices = textToSpeech.voices
        for (voice in voices) {
            if (getUserDataResponse()?.voice_gender == Constants.MALE) {
                println("Selected voice: ${voice.name}")
                println("Selected voice: 111")
                if (voice.name.contains("en-in-x-ene-network", ignoreCase = true)) {
                    maleVoice = voice
                    textToSpeech.voice = maleVoice
                    println("Selected male voice: ${voice.name}")
                    println("Selected voice: 222")
                }
            }
        }
    }


    private fun setArMaleVoice() {
        val voices = textToSpeech.voices
        for (voice in voices) {
            if (getUserDataResponse()?.voice_gender == Constants.MALE) {
                println("Selected voice: ${voice.name}")
                if (voice.name.contains("ar-xa-x-ard-network", ignoreCase = true)) {
                    maleVoice = voice
                    textToSpeech.voice = maleVoice
                    println("Selected male voice: ${voice.name}")
                }
            }
        }
    }

}
