package com.sp62b21.myapp.ui.view


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.speech.RecognizerIntent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.sp62b21.myapp.R
import com.sp62b21.myapp.data.models.Product
import com.sp62b21.myapp.service.alarm.AlarmHelper
import com.sp62b21.myapp.service.alarm.AlarmReceiver
import com.sp62b21.myapp.ui.recycler.RecyclerViewAdapter
import com.sp62b21.myapp.ui.view.base.BaseActivity
import com.sp62b21.myapp.ui.view.product.AddProductActivity
import com.sp62b21.myapp.ui.view.settings.SettingsActivity
import com.sp62b21.myapp.utils.PreferenceHelper
import com.sp62b21.myapp.vm.product.ProductListViewModel
import com.sp62b21.myapp.utils.visible
import com.sp62b21.myapp.utils.gone
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.android.synthetic.main.activity_main.*
import kotterknife.bindView
import java.util.*

class MainActivity : BaseActivity() {

    private val mRecyclerView: RecyclerView by bindView(R.id.rvProductsList)
    private val mFab: FloatingActionButton by bindView(R.id.fabCreateNewProduct)
    private var mSnackbar: Snackbar? = null

    private lateinit var mNotificationManager: NotificationManager
    private lateinit var mViewModel: ProductListViewModel
    private lateinit var mPreferenceHelper: PreferenceHelper
    private lateinit var mAdapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolbar("B E A U T I P I R E D", null, babMainMenu)

        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        AlarmHelper.getInstance().init(applicationContext)

        PreferenceHelper.getInstance().init(applicationContext)
        mPreferenceHelper = PreferenceHelper.getInstance()

        mViewModel = createViewModel()
        mViewModel.liveData.observe(this, Observer<List<Product>> { response -> updateViewState(response) })

        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = RecyclerViewAdapter()
        mRecyclerView.adapter = mAdapter

        initListeners()
        createProductTouchHelper()
    }

    private fun updateViewState(products: List<Product>) = if (products.isEmpty()) showEmptyView() else showProductList(products)

    private fun showProductList(products: List<Product>) {
        mProductList = products as ArrayList<Product>
        llEmptyView.gone()
        mAdapter.updateData(mProductList)
        setAlarmsAfterNotificationTimeChanged()
    }


    private fun deleteProduct(position: Int) {
        val deletedProduct = mAdapter.getProductAtPosition(position)
        val alarmHelper = AlarmHelper.getInstance()
        alarmHelper.removeAlarm(deletedProduct.timeStamp)
        mAdapter.removeProduct(position)
        mViewModel.deleteProduct(deletedProduct)
        var isUndoClicked = false

        mSnackbar = Snackbar.make(mRecyclerView, R.string.snackbar_remove_product, Snackbar.LENGTH_LONG)
        mSnackbar?.setAction(R.string.snackbar_undo) {
            mViewModel.saveProduct(deletedProduct)
            if (deletedProduct.expiredDate != 0L) {
                alarmHelper.setAlarm(deletedProduct)
            }
            isUndoClicked = true

            Handler().postDelayed({
                val firstCompletelyVisibleProduct = (mRecyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                if (firstCompletelyVisibleProduct != 0 ) {
                    setToolbarShadow(0f, 10f)
                }
            }, 100)
        }

        mSnackbar?.view?.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(view: View) {
                val firstCompletelyVisibleProduct = (mRecyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                val lastCompletelyVisibleProduct = (mRecyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

                if (firstCompletelyVisibleProduct == 0 && lastCompletelyVisibleProduct == mProductList.size - 1 ) {
                    setToolbarShadow(10f, 0f)
                }
            }

            override fun onViewDetachedFromWindow(view: View) {
                if (!isUndoClicked) {
                    alarmHelper.removeNotification(deletedProduct.timeStamp, this@MainActivity)
                }
            }
        })
        mSnackbar?.anchorView = mFab
        mSnackbar?.show()
    }

    private fun setAlarmsAfterNotificationTimeChanged() {
        if (mPreferenceHelper.getBoolean(PreferenceHelper.IS_NOTIFICATION_TIME_CHANGED)) {
            val alarmHelper = AlarmHelper.getInstance()

            for (product in mProductList) {
                if (product.expiredDate != 0L) {
                    alarmHelper.removeNotification(product.timeStamp, this)
                    alarmHelper.setAlarm(product)
                }
            }
            mPreferenceHelper.putBoolean(PreferenceHelper.IS_NOTIFICATION_TIME_CHANGED, false)
        }
    }

    private fun createProductTouchHelper() {
        val helper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteProduct(viewHolder.adapterPosition)
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean ) {
                RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.round_delete_outline_black_24)
                    .setActionIconTint(ContextCompat.getColor(this@MainActivity, R.color.white))
                    .create()
                    .decorate();
                super.onChildDraw( c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        })
        helper.attachToRecyclerView(mRecyclerView)
    }


    private fun showEmptyView() {
        mProductList = arrayListOf()
        mAdapter.updateData(mProductList)
        llEmptyView.visible()
    }


    private fun initListeners() {
        mFab.setOnClickListener {
            if (mSnackbar != null && mSnackbar!!.isShown) {
                mSnackbar?.dismiss()
                mSnackbar = null
            }
                val position = mAdapter.itemCount
                val intent = Intent(this, AddProductActivity::class.java)
                intent.putExtra("position", position)
                startActivity(intent)
        }

    }

    private fun createViewModel() = ViewModelProviders.of(this).get(
        ProductListViewModel(
            application
        )::class.java)


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> startActivity(Intent(this, SettingsActivity::class.java))
            R.id.action_search -> startActivity(Intent(this, SearchActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        setAlarmsAfterNotificationTimeChanged()
        mAdapter.setOnProductClickListener(object : RecyclerViewAdapter.ClickListener {
            override fun onProductClick(v: View, position: Int) {
                showProductDetailsActivity(mAdapter.getProductAtPosition(position))
            }
        })
    }

    companion object {
        var mProductList = arrayListOf<Product>()
    }

}
