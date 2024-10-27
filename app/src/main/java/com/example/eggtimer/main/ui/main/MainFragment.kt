package com.example.eggtimer.main.ui.main

import android.animation.ObjectAnimator
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.eggtimer.R
import com.example.eggtimer.databinding.FragmentMainBinding
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels { MainViewModel.Factory(requireContext().applicationContext) }
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.startButton.setOnClickListener{ viewModel.onButtonClicked() }
        binding.bottomNavBar.setOnItemSelectedListener { item ->
            viewModel.onItemSelected(item.itemId)
            return@setOnItemSelectedListener true
        }

        //viewLifecycleOwner - lifecycle для view, а не для fragment
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect { state ->
                    when (state) {
                        is Initial -> setupInitial(state)
                        is Running -> setupRunning(state)
                        Done -> setupDone()
                    }
                }
            }
        }
    }

    private fun setupDone() {
        with(binding) {
            message.text = getString(R.string.done)
            progressBar.progress = 0
            startButton.text = getString(R.string.ok_button_title)
            bottomNavBar.menu.setGroupEnabled(R.id.main_group, false)
        }
    }

    private fun setupRunning(state: Running) {
        val timeString = getString(R.string.time_template, state.time)
        with(binding) {
            message.text = timeString
            progressBar.progress = state.percentage
            startButton.text = getString(R.string.stop_button_title)
            bottomNavBar.menu.setGroupEnabled(R.id.main_group, false)
        }
    }

    private fun setupInitial(state: Initial) {
        val timeString = getString(R.string.time_template, state.time)
        with(binding) {
            message.text = timeString
            progressBar.progress = 100
            startButton.text = getString(R.string.start_button_title)
            bottomNavBar.menu.setGroupEnabled(R.id.main_group, true)
        }
    }

}