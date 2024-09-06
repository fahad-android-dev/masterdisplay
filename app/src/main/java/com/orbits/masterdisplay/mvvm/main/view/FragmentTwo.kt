package com.orbits.masterdisplay.mvvm.main.view

import android.content.ClipData.Item
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.orbits.masterdisplay.R
import com.orbits.masterdisplay.databinding.FragmentTwoBinding
import com.orbits.masterdisplay.helper.Extensions
import com.orbits.masterdisplay.helper.FileConfig.image_FilePaths
import com.orbits.masterdisplay.helper.FileConfig.readImageFile
import com.orbits.masterdisplay.helper.interfaces.ReconnectionListener
import com.orbits.masterdisplay.mvvm.main.adapter.CounterListAdapter
import com.orbits.masterdisplay.mvvm.main.model.ItemListDataModel

class FragmentTwo : Fragment() , ReconnectionListener{
    private lateinit var mActivity: MainActivity
    private lateinit var binding : FragmentTwoBinding
    private var adapter = CounterListAdapter()
    private var arrListItems = ArrayList<ItemListDataModel>()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as MainActivity

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_two, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvCounters.adapter = adapter

        Extensions.handler(500){
            mActivity.viewModel.setReconnectionListener(this)
        }

        initializeFields()
    }

    private fun initializeFields(){
        readImageFile("/MasterDisplay_Config/CounterTokenImages")
        image_FilePaths.let { files ->
            if (files != null) {
                for (file in files) {
                    when (file?.substring(file.lastIndexOf("/") + 1, file.lastIndexOf("."))) {
                        "counter" -> {
                            binding.ivCounter.setImageDrawable(Drawable.createFromPath(file))
                        }

                        "token" -> {
                            binding.ivToken.setImageDrawable(Drawable.createFromPath(file))
                        }
                    }

                }
            }
        }

        initData()

    }


    private fun initData(){
        Extensions.handler(700) {
            mActivity.hideProgressDialog()
            println("here is list ${mActivity.viewModel.dataList}")
            if (mActivity.viewModel.dataList.isNotEmpty()) {
                setData(mActivity.viewModel.dataList)
            }
        }
    }

    private fun setData(list: ArrayList<ItemListDataModel>) {
        arrListItems.clear()
        arrListItems.addAll(list)

        adapter.setData(arrListItems)
    }

    override fun onConnectionRestarted() {
        println("here is data is set")
        initData()
    }


}