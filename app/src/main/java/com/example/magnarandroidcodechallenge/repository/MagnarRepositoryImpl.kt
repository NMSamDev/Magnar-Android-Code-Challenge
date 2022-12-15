package com.example.magnarandroidcodechallenge.repository

import com.example.magnarandroidcodechallenge.api.RetrofitService
import retrofit2.awaitResponse
import java.io.InputStream

class MagnarRepositoryImpl(private val service: RetrofitService) : MagnarRepository {
    override suspend fun downloadFile(): InputStream {
        val response = service.downloadFile()
        return response.body()!!.byteStream()
    }
}