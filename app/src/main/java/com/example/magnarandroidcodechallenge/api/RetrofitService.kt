package com.example.magnarandroidcodechallenge.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface RetrofitService {
    @GET("VCCL.CSV")
    suspend fun downloadFile(): Response<ResponseBody>
}