package com.example.magnarandroidcodechallenge.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.magnarandroidcodechallenge.api.RetrofitInstance
import com.example.magnarandroidcodechallenge.api.RetrofitService
import com.opencsv.CSVReader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Callback
import java.io.*
import java.util.*


class MagnarViewModel(
    private val fileDir: File
): ViewModel() {

    private var dirPath: String = "$fileDir/CSVFiles"
    private var fileName: String = "VCCL.csv"
    private var csvFileName: File

    val isFileDownloaded: MutableLiveData<Boolean> = MutableLiveData()

    private val _csvData: MutableLiveData<List<String>> = MutableLiveData()
    val csvData: LiveData<List<String>> get() = _csvData

    init {
        val dirFile = File(dirPath)
        if (!dirFile.exists()) {
            dirFile.mkdirs()
        }

        val file = "${dirPath}/${fileName}"
        csvFileName = File(file)

        if(csvFileName.exists()){
            Log.d("ExistingFile", "File Updated")
            csvFileName.delete()
        }

    }
    fun downloadFile() {
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitInstance.getRetrofitInstance().create(RetrofitService::class.java)
                call.downloadFile().enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: retrofit2.Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                    if (response.isSuccessful) {

                        Log.d("DownloadFile", "Server contacted and has file")

                        val file = response.body()?.byteStream()
                        file?.let {
                            writeToFile(it)
                        }?: run {
                            isFileDownloaded.postValue(false)
                        }
                        isFileDownloaded.postValue(true)
                    }
                }

                override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                    isFileDownloaded.postValue(false)
                }
            })
        }
    }

    private fun writeToFile(inputStream: InputStream){
        try {
            val outputStream: OutputStream = csvFileName.outputStream()
            inputStream.copyTo(outputStream)

            CoroutineScope(Dispatchers.IO).launch {
                val csvData = async { readFileData() }
                _csvData.postValue(csvData.await())
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun readFileData(): List<String> {
        val result = mutableListOf<String>()
        try {
            val bufferedReader = BufferedReader(FileReader(csvFileName))
            val csvReader = CSVReader(bufferedReader)
            val data = csvReader.readAll()
            for (row in data) {
                result.add(row[0].replace("=", ""))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        Log.d("File Read", "readFileData: ${result.size}")
        return result
    }
}