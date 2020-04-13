package com.sp62b21.myapp.ui.recycler

//import android.provider.SyncStateContract.Helpers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.SectionIndexer
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sp62b21.myapp.R
import com.sp62b21.myapp.data.models.Brand


//import sun.reflect.annotation.AnnotationParser.toArray


class RecyclerBrandViewAdapter : RecyclerView.Adapter<RecyclerBrandViewAdapter.BrandViewHolder>() , SectionIndexer {
    private var mBrandList = arrayListOf<Brand>()
    private lateinit var mContext: Context
    lateinit var viewBackground: RelativeLayout
    lateinit var viewForeground: RelativeLayout

    private val mSections = "ABCDEFGHIJKLMNOPQRSTUVWXYZ#"
    private var sectionsTranslator: HashMap<Int, Int> = HashMap()
    private var mSectionPositions: ArrayList<Int>? = null

    fun updateData(brands: List<Brand>) {
        val result = DiffUtil.calculateDiff(BrandDiffUtilCallback(mBrandList, brands))
        mBrandList = brands as ArrayList<Brand>
        result.dispatchUpdatesTo(this)
    }

//    fun updateBrandOrder(fromPosition: Int, toPosition: Int) = notifyBrandMoved(fromPosition, toPosition)

    fun removeBrand(position: Int) {
        mBrandList.removeAt(position)
        notifyItemRemoved(position)
    }

//    fun removeBrand(id: Int) {
//        mBrandList.removeAt(id)
//        notifyItemRemoved(id)
//    }

//    fun reloadBrands() {
//        val backupList = ArrayList<Brand>()
//        backupList.addAll(mBrandList)
//        mBrandList.clear()
//
//        for (brand in backupList) {
//            mBrandList.add(brand)
//        }
//        notifyDataSetChanged()
//    }

    fun getBrandAtPosition(position: Int) = mBrandList[position]

    fun getBrandByID(id: Int) = mBrandList[id]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.brand, parent, false)
        val name = v.findViewById<TextView>(R.id.tvBrandName)
        mContext = parent.context
        return BrandViewHolder(v, name)
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        val brand = mBrandList[holder.adapterPosition]
        val itemView = holder.itemView
        itemView.setOnClickListener { view -> clickListener?.onBrandClick(view, holder.adapterPosition) }
        holder.itemView.isEnabled = true
        holder.name.text = brand.name

    }

    override fun getItemCount() = mBrandList.size

    fun setOnBrandClickListener(clickListener: ClickListener) {
        RecyclerBrandViewAdapter.clickListener = clickListener
    }

    interface ClickListener {
        fun onBrandClick(v: View, position: Int)
    }

    inner class BrandViewHolder internal constructor(itemView: View, internal var name: TextView) : RecyclerView.ViewHolder(itemView)

    companion object {
        private var clickListener: ClickListener? = null
    }

    override fun getSections(): Array<Any> {
        val sections: MutableList<String> = ArrayList(27)
        val alphabetFull: ArrayList<String> = ArrayList()

        mSectionPositions = arrayListOf()
        for ( brands in mBrandList) {
            var i = 0
            val size: Int = mBrandList.size
            while (i < size) {
                val section: String = mBrandList[i].name.get(0).toString().toUpperCase()
                if (!sections.contains(section)) {
                    sections.add(section)
                    mSectionPositions!!.add(i)
                }
                i++
            }
        }
        for (element in mSections) {
            alphabetFull.add(element.toString())
        }

        sectionsTranslator = Helpers.sectionsHelper(sections, alphabetFull)

        return alphabetFull.toArray(arrayOfNulls<String>(0))
    }


    override fun getSectionForPosition(position: Int): Int {
        return 0
    }

    override fun getPositionForSection(sectionIndex: Int): Int {
        return mSectionPositions!![sectionIndex];
    }
}