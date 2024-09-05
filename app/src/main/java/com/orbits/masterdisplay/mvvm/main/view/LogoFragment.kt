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
import com.orbits.masterdisplay.helper.FileConfig.image_FilePaths
import com.orbits.masterdisplay.helper.FileConfig.readImageFile

class LogoFragment : Fragment() {
    private lateinit var mActivity: MainActivity
    private lateinit var binding : FragmentLogoBinding
    private var pos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as MainActivity

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_logo, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeFields()
    }

    private fun initializeFields(){
        readImageFile()
        if (image_FilePaths?.size == 1) {
            println("here is logo set")
            binding.ivCompanyLogo.setImageDrawable(Drawable.createFromPath(image_FilePaths?.get(pos)))
        }
    }
}