package com.orbits.masterdisplay.mvvm.main.view

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.orbits.masterdisplay.R
import com.orbits.masterdisplay.databinding.FragmentLogoBinding
import com.orbits.masterdisplay.databinding.FragmentTopCounterBinding
import com.orbits.masterdisplay.helper.FileConfig.image_FilePaths
import com.orbits.masterdisplay.helper.FileConfig.readImageFile

class TopCounterFragment : Fragment() {
    private lateinit var mActivity: MainActivity
    private lateinit var binding : FragmentTopCounterBinding
    private var pos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as MainActivity

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_top_counter, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeFields()
    }

    private fun initializeFields(){
        readImageFile("/MasterDisplay_Config/TopImages")
        image_FilePaths.let { files ->
            if (files != null) {
                for (file in files) {
                    when (file?.substring(file.lastIndexOf("/") + 1, file.lastIndexOf("."))) {
                        "right" -> {
                           binding.img.setImageDrawable(Drawable.createFromPath(file))
                        }
                    }

                }
            }
        }
    }
}