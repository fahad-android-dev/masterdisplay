package com.orbits.masterdisplay.mvvm.main.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.orbits.masterdisplay.R
import com.orbits.masterdisplay.databinding.LvItemCountersBinding
import com.orbits.masterdisplay.databinding.LvItemMainBinding
import com.orbits.masterdisplay.helper.Constants
import com.orbits.masterdisplay.helper.Extensions.asFloat
import com.orbits.masterdisplay.helper.FileConfig.image_FilePaths
import com.orbits.masterdisplay.helper.FileConfig.readExcelFile
import com.orbits.masterdisplay.helper.FileConfig.readImageFile
import com.orbits.masterdisplay.helper.interfaces.CommonInterfaceClickEvent
import com.orbits.masterdisplay.mvvm.main.model.ItemListDataModel

class CounterListAdapter() : RecyclerView.Adapter<CounterListAdapter.MyViewHolder>() {

    var arrClientList: ArrayList<ItemListDataModel> = ArrayList()
    var onClickEvent: CommonInterfaceClickEvent? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: LvItemMainBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.lv_item_main,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val a = arrClientList[position]


        val excelValue = readExcelFile(
            Environment.getExternalStorageDirectory()
            .toString() + "/MasterDisplay_Config/Config.xls")



        // Counter data
        val counterTileColor = excelValue[Constants.COUNTER_TILE_COLOR]
        val counterTileRadius = excelValue[Constants.COUNTER_TILE_RADIUS]
        val counterTileTextColor = excelValue[Constants.COUNTER_TILE_TEXT_COLOR]
        if (counterTileColor != null) {
            holder.binding.cdCounter.setBackgroundColor(Color.parseColor(counterTileColor))
        }
        if (counterTileRadius != null) {
            holder.binding.cdCounter.radius = counterTileRadius.asFloat()
        }
        if (counterTileTextColor != null) {
            holder.binding.txtCounterNumber.setTextColor(Color.parseColor(counterTileTextColor))
        }



        // Token Data
        val tokenTileColor = excelValue[Constants.TOKEN_TILE_COLOR]
        val tokenTileRadius = excelValue[Constants.TOKEN_TILE_RADIUS]
        val tokenTileTextColor = excelValue[Constants.TOKEN_TILE_TEXT_COLOR]
        if (tokenTileColor != null) {
            holder.binding.cdToken.setBackgroundColor(Color.parseColor(tokenTileColor))
        }
        if (tokenTileRadius != null) {
            holder.binding.cdToken.radius = tokenTileRadius.asFloat()
        }
        if (tokenTileTextColor != null) {
            holder.binding.txtTokenNumber.setTextColor(Color.parseColor(tokenTileTextColor))
        }


        readImageFile("/MasterDisplay_Config/CounterTokenImages")
        image_FilePaths.let { files ->
            if (files != null) {
                for (file in files) {
                    when (file?.substring(file.lastIndexOf("/") + 1, file.lastIndexOf("."))) {
                        "arrow" -> {
                            holder.binding.ivArrow.setImageDrawable(Drawable.createFromPath(file))
                        }
                    }

                }
            }
        }



        holder.binding.txtCounterNumber.text = a.counterId ?: ""
        holder.binding.txtTokenNumber.text = a.keypadToken ?: ""


    }

    override fun getItemCount(): Int {
        return arrClientList.size
    }

    class MyViewHolder(var binding: LvItemMainBinding) :
        RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: ArrayList<ItemListDataModel>) {
        if (data.isNullOrEmpty()) {
            arrClientList = ArrayList()
        }
        arrClientList = data
        notifyDataSetChanged()
    }
}