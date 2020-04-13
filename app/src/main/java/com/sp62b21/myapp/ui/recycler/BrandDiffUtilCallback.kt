package com.sp62b21.myapp.ui.recycler

import androidx.recyclerview.widget.DiffUtil
import com.sp62b21.myapp.data.models.Brand

class BrandDiffUtilCallback constructor(private val oldList: List<Brand>, private val updatedList: List<Brand>)
    : DiffUtil.Callback() {

    override fun areItemsTheSame(oldBrandPosition: Int, newBrandPosition: Int) =
            oldList[oldBrandPosition].id == updatedList[newBrandPosition].id

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = updatedList.size

    override fun areContentsTheSame(oldBrandPosition: Int, newBrandPosition: Int): Boolean {
        val oldBrand = oldList[oldBrandPosition]
        val updatedBrand = updatedList[newBrandPosition]

        return oldBrand.name == updatedBrand.name
    }
}