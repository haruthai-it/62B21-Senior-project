package com.sp62b21.myapp.ui.recycler

import androidx.recyclerview.widget.DiffUtil
import com.sp62b21.myapp.data.models.Product

class ProductDiffUtilCallback constructor(private val oldList: List<Product>, private val updatedList: List<Product>)
    : DiffUtil.Callback() {

    override fun areItemsTheSame(oldProductPosition: Int, newProductPosition: Int) =
            oldList[oldProductPosition].id == updatedList[newProductPosition].id

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = updatedList.size

    override fun areContentsTheSame(oldProductPosition: Int, newProductPosition: Int): Boolean {
        val oldProduct = oldList[oldProductPosition]
        val updatedProduct = updatedList[newProductPosition]
        val isTitleTheSame = oldProduct.title == updatedProduct.title
        val isBrandTheSame = oldProduct.brand == updatedProduct.brand
        val isCategoryTheSame = oldProduct.category == updatedProduct.category
        val isOpenedTheSame = oldProduct.openedDate == updatedProduct.openedDate
        val isPAOTheSame = oldProduct.pao == updatedProduct.pao
        val isBbTheSame = oldProduct.bb == updatedProduct.bb
        val isNoteTheSame = oldProduct.note == updatedProduct.note

        return isTitleTheSame && isBrandTheSame && isCategoryTheSame && isOpenedTheSame && isPAOTheSame && isBbTheSame && isNoteTheSame
    }
}