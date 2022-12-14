package com.example.magnarandroidcodechallenge.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.magnarandroidcodechallenge.ValidateInput.validateId
import com.example.magnarandroidcodechallenge.databinding.ActivityMainBinding
import com.example.magnarandroidcodechallenge.viewmodel.MagnarViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MagnarViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewModel()
        viewModel.downloadFile()

        binding.apply {

            btnCheckId.setOnClickListener {
                val id = etId.text.toString()
                if (validateId(id, viewModel.csvData.value!!)){
                    Toast.makeText(this@MainActivity, "Valid ID", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Invalid ID", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun initViewModel() {
        viewModel = MagnarViewModel(filesDir)

        viewModel.csvData.observe(this) {
            binding.apply {
                if (it.isNotEmpty()){
                    tvFileStatus.text = "VCCL is up to date"
                    btnCheckId.isEnabled = true
                }
            }

        }
    }
}