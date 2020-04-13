package com.sp62b21.myapp.vm

import android.app.Application
import android.os.Environment
import com.sp62b21.myapp.data.models.Product
import com.sp62b21.myapp.service.alarm.AlarmHelper
import com.sp62b21.myapp.vm.base.BaseViewModel
import java.io.*

class RestoreBackupViewModel(val app: Application) : BaseViewModel(app) {
    private val mFile = File(Environment.getExternalStoragePublicDirectory("MyApp"), "Backup.ser")
    private var isRestoredSuccessfully = false

    fun isBackupExist() = mFile.exists()

    fun isBackupRestoredSuccessfully() = isRestoredSuccessfully

    fun restoreBackup() {
        val restoredProducts: List<Product>
        val alarmHelper = AlarmHelper.getInstance()

        isRestoredSuccessfully = try {
            val fileInputStream = FileInputStream(mFile)
            val objectInputStream = ObjectInputStream(fileInputStream)

            @Suppress("UNCHECKED_CAST")
            restoredProducts = objectInputStream.readObject() as List<Product>

            productRepository.deleteAllProducts()
            for (product in restoredProducts) {
                productRepository.saveProduct(product)

                if (product.expiredDate != 0L ) {
                    alarmHelper.setAlarm(product)
                }
            }
            objectInputStream.close()
            true
        } catch (exception: Exception) {
            exception.printStackTrace()
            false
        }
    }
}