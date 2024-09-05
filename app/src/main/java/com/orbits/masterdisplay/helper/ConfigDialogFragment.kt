package com.orbits.masterdisplay.helper

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.orbits.masterdisplay.R
import com.orbits.masterdisplay.databinding.LayoutConfigDialogFragmentBinding
import com.orbits.masterdisplay.helper.PrefUtils.getServerAddress
import com.orbits.masterdisplay.helper.PrefUtils.saveServerAddress
import com.orbits.masterdisplay.helper.helper_model.ServerAddressModel
import com.orbits.masterdisplay.mvvm.main.view.MainActivity

class ConfigDialogFragment : DialogFragment() {
    private lateinit var mActivity: MainActivity
    private lateinit var binding: LayoutConfigDialogFragmentBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as MainActivity

    }


    companion object {
        // Factory method to create a new instance of ConfigDialogFragment
        fun newInstance(): ConfigDialogFragment {
            return ConfigDialogFragment()
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.layout_config_dialog_fragment,
            null,
            false
        )

        val builder = AlertDialog.Builder(requireContext())
        val title = TextView(requireContext()).apply {
            text = mActivity.getString(R.string.connectivity_settings)
            setPadding(10, 10, 10, 10)
            gravity = Gravity.CENTER
            setTextColor(Color.parseColor("#17B0DE"))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f)
            setTypeface(null, Typeface.BOLD)
        }
        builder.setCustomTitle(title)
        builder.setView(binding.root)

        // All Methods
        initializeFields()
        onClickListeners()

        builder.setPositiveButton("Submit") { dialog, id ->

            dialog.cancel()

        }
        builder.setNegativeButton("Cancel") { dialog, id ->
            dialog.cancel()
        }

        return builder.create()
    }

    private fun initializeFields(){
        if (mActivity.getServerAddress()?.ipAddress?.isNotEmpty() == true){
            binding.edtAddress.setText(mActivity.getServerAddress()?.ipAddress)
            binding.edtPort.setText(mActivity.getServerAddress()?.port)
        }
    }

    private fun onClickListeners(){
        binding.edtPort.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed before text change
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Check if the port has at least 4 digits
                if ((s?.length ?: 0) >= 4) {
                    mActivity.saveServerAddress(
                        ServerAddressModel(
                            ipAddress = binding.edtAddress.text.toString(), port = binding.edtPort.text.toString()

                        )
                    )
                    mActivity.viewModel.connectWebSocket(
                        binding.edtAddress.text.toString(),
                        binding.edtPort.text.toString()
                    ) // Pass necessary
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // No action needed after text change
            }
        })
    }
}