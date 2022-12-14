package com.example.magnarandroidcodechallenge.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface RetrofitService {
    @GET("VCCL.CSV")
    fun downloadFile(): Call<ResponseBody>
}