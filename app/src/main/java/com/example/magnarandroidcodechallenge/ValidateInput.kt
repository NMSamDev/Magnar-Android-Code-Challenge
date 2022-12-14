package com.example.magnarandroidcodechallenge

object ValidateInput {
    fun validateId(id: String, list: List<String>): Boolean {
        return if(id.length != 8) false
        else !list.contains(id)
    }
}