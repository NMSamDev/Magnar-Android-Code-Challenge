package com.example.magnarandroidcodechallenge

import org.junit.Assert.*
import org.junit.Before

import org.junit.Test

class ValidateInputTest {

    @Test
    fun `8 characters and in the list is Invalid ID`() {
        val list = listOf("19746822", "87654321", "12345679", "87654320")
        val result = ValidateInput.validateId("11111111", list)
        assertTrue(result)
    }

    @Test
    fun `7 characters`() {
        val list = listOf("19746822", "87654321", "12345679", "87654320")
        val result = ValidateInput.validateId("1234567", list)
        assertFalse(result)
    }

    @Test
    fun `8 characters and not in the list is Valid ID`() {
        val list = listOf("19746822", "87654321", "12345679", "87654320")
        val result = ValidateInput.validateId("19746822", list)
        assertFalse(result)
    }
}