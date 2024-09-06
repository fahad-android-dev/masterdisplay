package com.orbits.masterdisplay.helper

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.orbits.masterdisplay.R
import com.orbits.masterdisplay.databinding.LayoutConfigDialogFragmentBinding
import com.orbits.masterdisplay.helper.PrefUtils.getAppConfig
import com.orbits.masterdisplay.helper.PrefUtils.getServerAddress
import com.orbits.masterdisplay.helper.PrefUtils.getUserDataResponse
import com.orbits.masterdisplay.helper.PrefUtils.saveServerAddress
import com.orbits.masterdisplay.helper.PrefUtils.setAppConfig
import com.orbits.masterdisplay.helper.PrefUtils.setUserDataResponse
import com.orbits.masterdisplay.helper.helper_model.AppConfigModel
import com.orbits.masterdisplay.helper.helper_model.ServerAddressModel
import com.orbits.masterdisplay.helper.helper_model.UserResponseModel

class ConfigDialogFragment : BaseActivity() {
    private lateinit var binding: LayoutConfigDialogFragmentBinding
    var gender = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.layout_config_dialog_fragment)

        initializeFields()
        onClickListeners()
    }


    private fun initializeFields(){
        if (getServerAddress()?.ipAddress?.isNotEmpty() == true){
            binding.edtAddress.setText(getServerAddress()?.ipAddress)
            binding.edtPort.setText(getServerAddress()?.port)
        }
        binding.chkLogo.isChecked = getAppConfig()?.isLogoChecked == true

        // voice data

        binding.edtEnglishMessage.setText(getUserDataResponse()?.msg_en)
        binding.edtArabicMessage.setText(getUserDataResponse()?.msg_ar)

        if (getUserDataResponse()?.voice_gender == Constants.MALE){
            binding.switchMale.isChecked = true
            gender = Constants.MALE
        }else {
            binding.switchFemale.isChecked  = true
            gender = Constants.FEMALE
        }

        when (getUserDataResponse()?.voice_selected) {
            Constants.ENGLISH -> {
                showSelected(binding.ivEnglish)
                showSelectedEditText(listOf(binding.edtEnglishMessage))
                showSelectedTokenCounters(listOf(binding.linTokenCounterEn))
            }
            Constants.ARABIC -> {
                showSelected(binding.ivArabic)
                showSelectedEditText(listOf(binding.edtArabicMessage))
                showSelectedTokenCounters(listOf(binding.linTokenCounterAr))
            }
            Constants.ENGLISH_ARABIC -> {
                showSelected(binding.ivEnglishArabic)
                showSelectedEditText(listOf(binding.edtEnglishMessage,binding.edtArabicMessage))
                showSelectedTokenCounters(listOf(binding.linTokenCounterEn,binding.linTokenCounterAr))
            }
            Constants.ARABIC_ENGLISH -> {
                showSelected(binding.ivArabicEnglish)
                showSelectedEditText(listOf(binding.edtEnglishMessage,binding.edtArabicMessage))
                showSelectedTokenCounters(listOf(binding.linTokenCounterEn,binding.linTokenCounterAr))
            }
        }
    }

    private fun setPositiveButtonData(){
        setAppConfig(
            AppConfigModel(
                isLogoChecked = binding.chkLogo.isChecked
            )
        )

        saveServerAddress(
            ServerAddressModel(
                ipAddress = binding.edtAddress.text.toString(), port = binding.edtPort.text.toString()

            )
        )

        setUserDataResponse(
            UserResponseModel(
                voice_selected =
                when {
                    binding.ivEnglish.isVisible -> Constants.ENGLISH
                    binding.ivArabic.isVisible -> Constants.ARABIC
                    binding.ivEnglishArabic.isVisible -> Constants.ENGLISH_ARABIC
                    binding.ivArabicEnglish.isVisible -> Constants.ARABIC_ENGLISH
                    else -> Constants.ENGLISH
                },
                msg_en = binding.edtEnglishMessage.text.toString(),
                msg_ar = binding.edtArabicMessage.text.toString(),
                voice_gender = gender
            )
        )
    }

    private fun onClickListeners(){
        binding.conEnglish.setOnClickListener {
            showSelected(binding.ivEnglish)
            showSelectedEditText(listOf(binding.edtEnglishMessage))
            showSelectedTokenCounters(listOf(binding.linTokenCounterEn))
        }

        binding.conArabic.setOnClickListener {
            showSelected(binding.ivArabic)
            showSelectedEditText(listOf(binding.edtArabicMessage))
            showSelectedTokenCounters(listOf(binding.linTokenCounterAr))
        }

        binding.conEnglishArabic.setOnClickListener {
            showSelected(binding.ivEnglishArabic)
            showSelectedEditText(listOf(binding.edtEnglishMessage,binding.edtArabicMessage))
            showSelectedTokenCounters(listOf(binding.linTokenCounterEn,binding.linTokenCounterAr))
        }

        binding.conArabicEnglish.setOnClickListener {
            showSelected(binding.ivArabicEnglish)
            showSelectedEditText(listOf(binding.edtEnglishMessage,binding.edtArabicMessage))
            showSelectedTokenCounters(listOf(binding.linTokenCounterEn,binding.linTokenCounterAr))
        }
        binding.switchMale.setOnCheckedChangeListener { view, isChecked ->
            if (isChecked) {
                gender = Constants.MALE
                binding.switchFemale.isChecked = false
            }
        }
        binding.switchFemale.setOnCheckedChangeListener { view, isChecked ->
            if (isChecked) {
                gender = Constants.FEMALE
                binding.switchMale.isChecked = false
            }
        }

        binding.btnTokenEn.setOnClickListener {
            val currentText = binding.edtEnglishMessage.text.toString()
            val textToInsert = " <token>"
            val cursorPosition = binding.edtEnglishMessage.selectionStart
            val newText = StringBuilder(currentText).insert(cursorPosition, textToInsert).toString()
            binding.edtEnglishMessage.setText(newText)
            binding.edtEnglishMessage.setSelection(cursorPosition + textToInsert.length)
        }

        binding.btnCounterEn.setOnClickListener {
            val currentText = binding.edtEnglishMessage.text.toString()
            val textToInsert = " <counter>"
            val cursorPosition = binding.edtEnglishMessage.selectionStart
            val newText = StringBuilder(currentText).insert(cursorPosition, textToInsert).toString()
            binding.edtEnglishMessage.setText(newText)
            binding.edtEnglishMessage.setSelection(cursorPosition + textToInsert.length)
        }

        binding.btnTokenAr.setOnClickListener {
            val currentText = binding.edtArabicMessage.text.toString()
            val textToInsert = " <token>"
            val cursorPosition = binding.edtArabicMessage.selectionStart
            val newText = StringBuilder(currentText).insert(cursorPosition, textToInsert).toString()
            binding.edtArabicMessage.setText(newText)
            binding.edtArabicMessage.setSelection(cursorPosition + textToInsert.length)
        }

        binding.btnCounterAr.setOnClickListener {
            val currentText = binding.edtArabicMessage.text.toString()
            val textToInsert = " <counter>"
            val cursorPosition = binding.edtArabicMessage.selectionStart
            val newText = StringBuilder(currentText).insert(cursorPosition, textToInsert).toString()
            binding.edtArabicMessage.setText(newText)
            binding.edtArabicMessage.setSelection(cursorPosition + textToInsert.length)
        }

        binding.btnConfirm.setOnClickListener {
            setPositiveButtonData()
            finishAffinity()
            System.exit(0)
        }

    }

    private fun showSelected(imageViewToShow: ImageView) {
        val imageViews = listOf(
            binding.ivEnglish,
            binding.ivArabic,
            binding.ivEnglishArabic,
            binding.ivArabicEnglish
        )

        imageViews.forEach { it.visibility = ImageView.GONE }

        imageViewToShow.visibility = ImageView.VISIBLE
    }

    private fun showSelectedEditText(editTextsToShow: List<EditText>) {
        // List of all EditTexts you want to control
        val allEditTexts = listOf(
            binding.edtEnglishMessage,
            binding.edtArabicMessage,

            )

        allEditTexts.forEach { it.isVisible = false }

        editTextsToShow.forEach { it.isVisible = true }
    }

    private fun showSelectedTokenCounters(tokenCounters: List<LinearLayout>) {
        // List of all EditTexts you want to control
        val allLinear = listOf(
            binding.linTokenCounterEn,
            binding.linTokenCounterAr,

            )

        allLinear.forEach { it.isVisible = false }

        tokenCounters.forEach { it.isVisible = true }
    }
}