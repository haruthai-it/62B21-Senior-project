package com.sp62b21.myapp.ui.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sp62b21.myapp.R
import com.sp62b21.myapp.data.models.PAO


class PAOAdapter : RecyclerView.Adapter<PAOAdapter.PaoViewHolder>() {
    private var mPAOsList = arrayListOf<PAO>()
    private lateinit var mContext: Context
    var context: Context? = null
    var selectedPosition = -1
//
    fun updateData(paos: List<PAO>) {
        val result = DiffUtil.calculateDiff(PAODiffUtilCallback(mPAOsList, paos))
        mPAOsList = paos as ArrayList<PAO>
        result.dispatchUpdatesTo(this)
    }

//    constructor(context: Context, PAOsList: ArrayList<PAO>) : super() {
//        this.context = context
//        this.PAOsList = PAOsList
//    }

//    override fun getCount(): Int {
//        return PAOsList.size
//    }
//
//    override fun getItem(position: Int): Any {
//        return PAOsList[position]
//    }

    fun getPAOAtPosition(position: Int) = mPAOsList[position]

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        val PAO = this.mPAOsList[position]
//
//        var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        var PAOView = inflator.inflate(R.layout.pao, null)
////        PAOView.ivPAO.setImageResource(PAO.image!!)
//        PAOView.tvPAO.text = PAO.month!!
//
//        return PAOView
//    }



    inner class PaoViewHolder internal constructor(itemView: View,internal var iv: ImageView, internal var month: TextView) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): PAOAdapter.PaoViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.pao, parent, false)
        val iv = v.findViewById<ImageView>(R.id.ivPAO)
        val month = v.findViewById<TextView>(R.id.tvPAO)
        mContext = parent.context
        return PaoViewHolder(v, iv, month)
    }

    override fun getItemCount() = mPAOsList.size


    override fun onBindViewHolder(holder: PaoViewHolder, position: Int) {
        val pao = mPAOsList[holder.adapterPosition]
        val itemView = holder.itemView
        itemView.setOnClickListener { view ->
            clickListener?.onPaoClick(view, holder.adapterPosition)
            selectedPosition = position
            notifyDataSetChanged()
        }
        if (selectedPosition == position || pao.id == position){
            holder.iv.setColorFilter(ContextCompat.getColor(itemView.context, R.color.blue))
            holder.month.setTextColor(ContextCompat.getColor(itemView.context, R.color.blue))
        }
        else{
            holder.iv.setColorFilter(ContextCompat.getColor(itemView.context, R.color.grey))
            holder.month.setTextColor(ContextCompat.getColor(itemView.context, R.color.grey))
        }
        holder.itemView.isEnabled = true
        holder.month.text = pao.month
    }

    fun setOnPaoClickListener(clickListener: PAOAdapter.ClickListener) {
        PAOAdapter.clickListener = clickListener
    }


    interface ClickListener {
        fun onPaoClick(v: View, position: Int)
    }

    companion object {
        private var clickListener: PAOAdapter.ClickListener? = null
    }
}

