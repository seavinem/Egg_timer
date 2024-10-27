package com.example.eggtimer.main.ui.main

import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eggtimer.R
import com.example.eggtimer.main.media.RingtoneHelper
import com.example.eggtimer.main.media.VibratorHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel(
    private val ringtoneHelper: RingtoneHelper,
    private val vibratorHelper: VibratorHelper
) : ViewModel() {

    private val _viewState = MutableStateFlow<MainViewState>(Initial(CookDegree.SOFT.time))
    val viewState: Flow<MainViewState> get() = _viewState

    private var timer = createTimer(CookDegree.SOFT.time)
    private var previousTime = CookDegree.SOFT.time

    private fun createTimer(time: Long): CountDownTimer {
        _viewState.value = Initial(time)

        return object : CountDownTimer(time, 1_000) {
            override fun onTick(millisUntilFinished: Long) {
                val progressPercentage = ((millisUntilFinished.toFloat() / time) * 100).toInt()
                _viewState.value = Running(millisUntilFinished, progressPercentage)
            }

            override fun onFinish() {
                _viewState.value = Done
                ringtoneHelper.start()
                vibratorHelper.start()
            }
        }
    }

    fun onButtonClicked() {
        when (_viewState.value) {
            is Initial -> timer.start()
            is Running -> {
                timer.cancel()
                _viewState.value = Done
            }
            Done -> {
                _viewState.value = Initial(previousTime)
                ringtoneHelper.stop()
                vibratorHelper.stop()
            }
        }
    }

    fun onItemSelected(itemId: Int) {
        val time = when(itemId) {
            R.id.soft -> CookDegree.SOFT.time
            R.id.medium ->CookDegree.MEDIUM.time
            R.id.hard -> CookDegree.HARD.time
            else -> CookDegree.SOFT.time
        }

        previousTime = time
        timer = createTimer(time)
    }

    companion object {
        fun Factory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val ringtoneHelper = RingtoneHelper(context)
                    val vibratorHelper = VibratorHelper(context)
                    return MainViewModel(ringtoneHelper, vibratorHelper) as T
                }
            }
        }
    }
}