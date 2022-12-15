package com.example.magnarandroidcodechallenge.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.magnarandroidcodechallenge.ValidateInput.validateId
import com.example.magnarandroidcodechallenge.databinding.ActivityMainBinding
import com.example.magnarandroidcodechallenge.viewmodel.MagnarViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val viewModel: MagnarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObservers()

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
    private fun initObservers() {
        viewModel.isFileDownloaded.observe(this) {
            if (it) {
                binding.tvFileStatus.text = "File downloaded"
                binding.progressBar.visibility = View.GONE
            }
            else {
                binding.tvFileStatus.text = "Loading..."
                binding.progressBar.visibility = View.VISIBLE
            }
        }
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