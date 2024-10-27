package com.example.eggtimer.main.ui.main

import android.health.connect.datatypes.units.Percentage

sealed interface MainViewState

data class Initial(val time: Long) : MainViewState
data class Running(val time: Long, val percentage: Int) : MainViewState
object Done : MainViewState

enum class CookDegree(val time: Long) {
    SOFT(3 * 60 * 1_000L),
    MEDIUM(5 * 60 * 1_000L),
    HARD(9 * 60 * 1_000L)
}