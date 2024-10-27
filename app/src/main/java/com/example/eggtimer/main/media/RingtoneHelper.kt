package com.example.eggtimer.main.media

import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager

class RingtoneHelper(
    private val context: Context
) {

    private var ringtone: Ringtone? = null

    fun start() {
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(context, uri)
        ringtone?.play()
    }

    fun stop() {
        ringtone?.stop()
    }
}