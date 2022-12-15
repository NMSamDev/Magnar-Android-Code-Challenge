package com.example.magnarandroidcodechallenge.repository

import java.io.InputStream

interface MagnarRepository {
    suspend fun downloadFile(): InputStream
}