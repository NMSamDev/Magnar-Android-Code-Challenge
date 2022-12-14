package com.example.magnarandroidcodechallenge.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.magnarandroidcodechallenge.api.RetrofitInstance
import com.example.magnarandroidcodechallenge.api.RetrofitService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.opencsv.CSVReader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Callback
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class MagnarViewModel(
    private val fileDir: File,
    private val sharedPreferences: SharedPreferences
): ViewModel() {

    private var dirPath: String = "$fileDir/CSVFiles"
    private var fileName: String = "VCCL.csv"
    private var csvFileName: File

    val isFileDownloaded: MutableLiveData<Boolean> = MutableLiveData()

    private val _csvData: MutableLiveData<List<String>> = MutableLiveData()
    val csvData: LiveData<List<String>> get() = _csvData

    private val lastUpdated = Date(sharedPreferences.getLong("lastUpdated", 0))
    // 16693457
    init {
        val dirFile = File(dirPath)
        if (!dirFile.exists()) {
            dirFile.mkdirs()
        }

        val file = "${dirPath}/${fileName}"
        csvFileName = File(file)

        if(csvFileName.exists()){
            if(lastUpdated.time + TimeUnit.DAYS.toMillis(1) < Date().time){
                csvFileName.delete()
                downloadFile()
            } else {
                loadList()
            }
        }
        else{
            downloadFile()
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

            sharedPreferences.edit().putLong("lastUpdated", Date().time).apply()
            CoroutineScope(Dispatchers.IO).launch {
                val csvData = async { readFileData() }
                _csvData.postValue(csvData.await())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private suspend fun readFileData(): List<String> {
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
        storeList(result)

        return result
    }

    private fun storeList(list: List<String>){
        val gson = Gson()
        val json = gson.toJson(list)
        sharedPreferences.edit().putString("csvData", json).apply()
    }

    private fun loadList(){
        val gson = Gson()
        val json = sharedPreferences.getString("csvData", null)
        val type = object : TypeToken<List<String>>() {}.type
        val list = gson.fromJson<List<String>>(json, type)
        _csvData.postValue(list)
    }
}