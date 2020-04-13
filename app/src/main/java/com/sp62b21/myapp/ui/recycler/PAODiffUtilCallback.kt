package com.sp62b21.myapp.ui.recycler

import androidx.recyclerview.widget.DiffUtil
import com.sp62b21.myapp.data.models.PAO

class PAODiffUtilCallback constructor(private val oldList: List<PAO>, private val updatedList: List<PAO>)
    : DiffUtil.Callback() {

    override fun areItemsTheSame(oldPaoPosition: Int, newPaoPosition: Int) =
            oldList[oldPaoPosition].id == updatedList[newPaoPosition].id

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = updatedList.size

    override fun areContentsTheSame(oldPaoPosition: Int, newPaoPosition: Int): Boolean {
        val oldPao = oldList[oldPaoPosition]
        val updatedPao = updatedList[newPaoPosition]

        return oldPao.month == updatedPao.month
    }
}