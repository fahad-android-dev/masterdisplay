package com.orbits.masterdisplay.helper

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
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
import com.orbits.masterdisplay.mvvm.main.model.ItemListDataModel
import com.orbits.masterdisplay.mvvm.main.model.ServiceListDataModel
import com.orbits.masterdisplay.mvvm.main.view_model.MainViewModel

class ConfigDialogFragment : BaseActivity() {
    private lateinit var binding: LayoutConfigDialogFragmentBinding
    var gender = ""
    var selectedOption = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.layout_config_dialog_fragment)

        initializeFields()
        onClickListeners()
    }


    private fun initializeFields(){

        setSpinner()
        binding.edtServices.setText(getServerAddress()?.services)
        if (getServerAddress()?.ipAddress?.isNotEmpty() == true){
            binding.edtAddress.setText(getServerAddress()?.ipAddress)
            binding.edtPort.setText(getServerAddress()?.port)
        }
        binding.chkLogo.isChecked = getAppConfig()?.isLogoChecked == true
        binding.chkPortrait.isChecked = getAppConfig()?.isPortraitChecked == true
        binding.chkDateTime.isChecked = getAppConfig()?.isTimeChecked == true
        binding.chkScroll.isChecked = getAppConfig()?.isScrollArabic == true


        binding.edtEnglishMessage.setText(getUserDataResponse()?.msg_en)
        binding.edtArabicMessage.setText(getUserDataResponse()?.msg_ar)
        binding.edtScrollMessage.setText(getAppConfig()?.scrollText)

        if (getUserDataResponse()?.voice_gender == Constants.MALE){
            binding.switchMale.isChecked = true
            gender = Constants.MALE
        }else {
            binding.switchFemale.isChecked  = true
            gender = Constants.FEMALE
        }

        when (getUserDataResponse()?.voice_selected) {
            Constants.ENGLISH -> {
                showSelectedEditText(listOf(binding.edtEnglishMessage))
                showSelectedTokenCounters(listOf(binding.linTokenCounterEn))
            }
            Constants.ARABIC -> {
                showSelectedEditText(listOf(binding.edtArabicMessage))
                showSelectedTokenCounters(listOf(binding.linTokenCounterAr))
            }
            Constants.ENGLISH_ARABIC -> {
                showSelectedEditText(listOf(binding.edtEnglishMessage,binding.edtArabicMessage))
                showSelectedTokenCounters(listOf(binding.linTokenCounterEn,binding.linTokenCounterAr))
            }
            Constants.ARABIC_ENGLISH -> {
                showSelectedEditText(listOf(binding.edtEnglishMessage,binding.edtArabicMessage))
                showSelectedTokenCounters(listOf(binding.linTokenCounterEn,binding.linTokenCounterAr))
            }
        }

    }

    private fun setPositiveButtonData(){
        setAppConfig(
            AppConfigModel(
                isLogoChecked = binding.chkLogo.isChecked,
                isPortraitChecked = binding.chkPortrait.isChecked,
                isTimeChecked = binding.chkDateTime.isChecked,
                isScrollArabic = binding.chkScroll.isChecked,
                scrollText = binding.edtScrollMessage.text.toString()
            )
        )

        saveServerAddress(
            ServerAddressModel(
                ipAddress = binding.edtAddress.text.toString(),
                port = binding.edtPort.text.toString(),
                services = binding.edtServices.text.toString()

            )
        )
        println("here is selected option $selectedOption")
        setUserDataResponse(
            UserResponseModel(
                voice_selected = selectedOption,
                msg_en = binding.edtEnglishMessage.text.toString(),
                msg_ar = binding.edtArabicMessage.text.toString(),
                voice_gender = gender,
            )
        )
    }

    private fun onClickListeners(){

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

        binding.chkScroll.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                binding.edtScrollMessage.hint = "Please enter arabic scroll message"
            }else {
                binding.edtScrollMessage.hint = "Please enter english scroll message"
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


        binding.edtPort.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Check if four characters are written
                if (s?.length == 4) {
                    if (intent.getSerializableExtra("data") == null){
                        viewModel.connectWebSocket(
                            binding.edtAddress.text.toString(),
                            binding.edtPort.text.toString(),
                        )
                    }

                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do something before text is changed, if needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do something while text is being changed, if needed
            }
        })


        binding.edtServices.setOnClickListener {
            val list = intent.getSerializableExtra("data") as ArrayList<ServiceListDataModel>
            val selectedServices = BooleanArray(list.size) { false } // Keep track of selected services
            val selectedItems = mutableListOf<String>()

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Select Services")

            val serviceNames = list.map { it.id }.toTypedArray()
            println("here is service names ${serviceNames.size}")
            println("here is dataList ${list}")

            builder.setMultiChoiceItems(serviceNames, selectedServices) { dialog, which, isChecked ->
                // Update selected services
                if (isChecked) {
                    selectedItems.add(serviceNames[which] ?: "")
                } else {
                    selectedItems.remove(serviceNames[which])
                }
            }

            // Set positive button
            builder.setPositiveButton("OK") { dialog, _ ->
                // Update EditText with selected items, comma separated
                binding.edtServices.setText(selectedItems.joinToString(","))
            }

            // Set negative button
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

            // Show the dialog
            builder.show()
        }

    }


    private fun showSelectedEditText(editTextsToShow: List<EditText>) {
        val allEditTexts = listOf(
            binding.edtEnglishMessage,
            binding.edtArabicMessage,

            )

        allEditTexts.forEach { it.isVisible = false }

        editTextsToShow.forEach { it.isVisible = true }
    }

    private fun showSelectedTokenCounters(tokenCounters: List<LinearLayout>) {
        val allLinear = listOf(
            binding.linTokenCounterEn,
            binding.linTokenCounterAr,

            )

        allLinear.forEach { it.isVisible = false }

        tokenCounters.forEach { it.isVisible = true }
    }

    private fun setSpinner(){
        val options: MutableList<String> = ArrayList()
        options.add("English")
        options.add("Arabic")
        options.add("English First Arabic Second")
        options.add("Arabic First English Second")

        val topAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        topAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSelectConfig.adapter = topAdapter


        binding.spinnerSelectConfig.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedOption = parent.getItemAtPosition(position).toString()
                when (selectedOption) {
                    "English" -> {
                        showSelectedEditText(listOf(binding.edtEnglishMessage))
                        showSelectedTokenCounters(listOf(binding.linTokenCounterEn))
                    }
                    "Arabic" -> {
                        showSelectedEditText(listOf(binding.edtArabicMessage))
                        showSelectedTokenCounters(listOf(binding.linTokenCounterAr))
                    }
                    "English First Arabic Second" -> {
                        showSelectedEditText(listOf(binding.edtEnglishMessage,binding.edtArabicMessage))
                        showSelectedTokenCounters(listOf(binding.linTokenCounterEn,binding.linTokenCounterAr))
                    }
                    "Arabic First English Second" -> {
                        showSelectedEditText(listOf(binding.edtEnglishMessage,binding.edtArabicMessage))
                        showSelectedTokenCounters(listOf(binding.linTokenCounterEn,binding.linTokenCounterAr))
                    }
                    else -> {}
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when nothing is selected
            }
        }

        for (i in options.indices) {
            println("here is voice selected ${getUserDataResponse()?.voice_selected}")
            println("here is options selected ${options[i]}")
            if (getUserDataResponse()?.voice_selected == options[i]) {
                binding.spinnerSelectConfig.setSelection(i)
            }
        }
    }

}