package com.sp62b21.myapp.vm

import android.app.Application
import android.os.Environment
import com.sp62b21.myapp.vm.base.BaseViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectOutputStream

class CreateBackupViewModel(val app: Application) : BaseViewModel(app) {
    private val mFile = File(Environment.getExternalStoragePublicDirectory("MyApp"), "Backup.ser")
    private var isCreatedSuccessfully = false

    fun isBackupCreatedSuccessfully() = isCreatedSuccessfully

    fun createBackup() {
        val tasks = productRepository.getAllProducts()
        val file = File(Environment.getExternalStorageDirectory().absolutePath, "MyApp")
        if (!file.exists()) file.mkdir()
        isCreatedSuccessfully = try {
            mFile.delete()

            val fileOutputStream = FileOutputStream(mFile, true)
            val objectOutputStream = ObjectOutputStream(fileOutputStream)

            objectOutputStream.writeObject(tasks)
            objectOutputStream.close()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }
}