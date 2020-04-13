package com.sp62b21.myapp.ui.recycler

import android.content.Context
import android.text.format.DateUtils
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sp62b21.myapp.R
import com.sp62b21.myapp.data.models.Product
import com.sp62b21.myapp.utils.gone
import com.sp62b21.myapp.utils.visible
import com.sp62b21.myapp.utils.DateAndTime
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ProductViewHolder>() {
    private var mProductList = arrayListOf<Product>()
    private lateinit var mContext: Context

    fun updateData(products: List<Product>) {
        val result = DiffUtil.calculateDiff(ProductDiffUtilCallback(mProductList, products))
        mProductList = products as ArrayList<Product>
        result.dispatchUpdatesTo(this)
    }

    fun removeProduct(position: Int) {
        mProductList.removeAt(position)
        notifyItemRemoved(position)
    }


//    fun reloadProducts() {
//        val backupList = ArrayList<Product>()
//        backupList.addAll(mProductList)
//        mProductList.clear()
//
//        for (product in backupList) {
//            mProductList.add(product)
//        }
//        notifyDataSetChanged()
//    }

    fun getProductAtPosition(position: Int) = mProductList[position]

    fun getProductByID(id: Int) = mProductList[id]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.product, parent, false)
        val image = v.findViewById<ImageView>(R.id.imageProduct)
        val brand = v.findViewById<TextView>(R.id.tvProductBrand)
        val title = v.findViewById<TextView>(R.id.tvProductTitle)
        val expiredDate = v.findViewById<TextView>(R.id.tvProductExpiredDate)
        val datediff = v.findViewById<TextView>(R.id.tvDateDiff)
        mContext = parent.context
        return ProductViewHolder(v,image, brand, title, expiredDate, datediff)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = mProductList[holder.adapterPosition]
        val itemView = holder.itemView
        itemView.setOnClickListener { view -> clickListener?.onProductClick(view, holder.adapterPosition) }
        holder.itemView.isEnabled = true
        holder.title.text = product.title

        val mFromDate: LocalDate = DateAndTime.getLocalDateFromLong(Calendar.getInstance().timeInMillis)
        val mToDate: LocalDate = DateAndTime.getLocalDateFromLong(product.expiredDate)
        val mDateDiff: String = DateAndTime.getDateDif(mFromDate,mToDate)
        holder.datediff.text = mDateDiff

        if(product.image != null){
            holder.image.imageTintList = null
            Glide
                .with(mContext)
                .load(product.image)
                .centerCrop()
                .placeholder(R.drawable.cosmetics_outline_black)
                .into(holder.image)
        }

        if(product.brand != null){
            holder.brand.visible()
            holder.brand.text = product.brand
        } else {
            holder.brand.gone()
            holder.title.setPadding(0, 20, 0, 0)
        }

        if (product.expiredDate != 0L ) {
            holder.title.setPadding(0, 0, 0, 0)
            holder.title.gravity = Gravity.CENTER_VERTICAL
            holder.brand.setPadding(0, 0, 0, 0)
            holder.brand.gravity = Gravity.CENTER_VERTICAL
            holder.expiredDate.visible()
            when {
                // Today
                DateUtils.isToday(product.expiredDate) -> {
//                    holder.datediff.setBackgroundColor(ContextCompat.getColor(mContext, R.color.orange))
                    holder.expiredDate.text = mContext.getString(R.string.reminder_today)
                    holder.datediff.setBackgroundResource(R.drawable.rectangle_red_background)
                }

                // Yesterday
                DateUtils.isToday(product.expiredDate + DateUtils.DAY_IN_MILLIS) -> {
//                    holder.datediff.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red))
                    holder.datediff.setBackgroundResource(R.drawable.rectangle_red_background)
                    holder.expiredDate.text = mContext.getString(R.string.reminder_yesterday)
                }

                // Tomorrow
                DateUtils.isToday(product.expiredDate - DateUtils.DAY_IN_MILLIS) -> {
//                    holder.datediff.setBackgroundColor(ContextCompat.getColor(mContext, R.color.yellow))
                    holder.datediff.setBackgroundResource(R.drawable.rectangle_yellow_background)
                    holder.expiredDate.text = mContext.getString(R.string.reminder_tomorrow)
                }

                // Far past
                product.expiredDate < Calendar.getInstance().timeInMillis -> {
//                    holder.datediff.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red))
                    holder.datediff.setBackgroundResource(R.drawable.rectangle_red_background)
                    holder.expiredDate.text = DateAndTime.getFullDate(product.expiredDate)
                }

                // Far future
                product.expiredDate > Calendar.getInstance().timeInMillis -> {
//                    holder.datediff.setBackgroundColor(ContextCompat.getColor(mContext, R.color.blue))
                    holder.datediff.setBackgroundResource(R.drawable.rectangle_blue_background)
                    holder.expiredDate.text = mContext.getString(R.string.date_at, DateAndTime.getFullDate(product.expiredDate))
                }
            }
        }
        else {
            holder.datediff.gone()
            holder.expiredDate.gone()
            holder.title.setPadding(0, 0, 0, 20)
            holder.brand.setPadding(0, 20, 0, 0)
        }

        if(product.brand == null && product.expiredDate != 0L )
        {
            holder.title.setPadding(0, 20, 0, 0)
            holder.expiredDate.setPadding(0, 0, 0, 20)
        }

        if(product.brand == null && product.expiredDate == 0L )
        {
            holder.title.setPadding(0, 35, 0, 35)
        }
    }

    override fun getItemCount() = mProductList.size

    fun setOnProductClickListener(clickListener: ClickListener) {
        RecyclerViewAdapter.clickListener = clickListener
    }

    interface ClickListener {
        fun onProductClick(v: View, position: Int)
    }

    inner class ProductViewHolder internal constructor(itemView: View,
                                                       internal var image: ImageView,
                                                       internal var brand: TextView,
                                                       internal var title: TextView,
                                                       internal var expiredDate: TextView,
                                                       internal var datediff: TextView) : RecyclerView.ViewHolder(itemView)

    companion object {
        private var clickListener: ClickListener? = null
    }
}