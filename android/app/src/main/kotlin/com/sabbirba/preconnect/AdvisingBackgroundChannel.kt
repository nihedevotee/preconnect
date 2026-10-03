package com.sabbirba.preconnect

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.WindowManager
import androidx.core.content.ContextCompat
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

internal class AdvisingBackgroundChannel(
    private val activity: Activity,
) {
    fun configure(messenger: BinaryMessenger) {
        MethodChannel(messenger, CHANNEL).setMethodCallHandler { call, result ->
            when (call.method) {
                "start" -> {
                    val title = call.argument<String>("title") ?: "Advising Helper"
                    val message = call.argument<String>("message") ?: "Monitoring sections..."
                    val intent = Intent(activity, AdvisingForegroundService::class.java).apply {
                        action = AdvisingForegroundService.ACTION_START
                        putExtra(AdvisingForegroundService.EXTRA_TITLE, title)
                        putExtra(AdvisingForegroundService.EXTRA_MESSAGE, message)
                    }
                    try {
                        ContextCompat.startForegroundService(activity, intent)
                        result.success(true)
                    } catch (_: Exception) {
                        result.success(false)
                    }
                }
                "update" -> {
                    val title = call.argument<String>("title") ?: "Advising Helper"
                    val message = call.argument<String>("message") ?: "Monitoring sections..."
                    val intent = Intent(activity, AdvisingForegroundService::class.java).apply {
                        action = AdvisingForegroundService.ACTION_UPDATE
                        putExtra(AdvisingForegroundService.EXTRA_TITLE, title)
                        putExtra(AdvisingForegroundService.EXTRA_MESSAGE, message)
                    }
                    try {
                        activity.startService(intent)
                        result.success(true)
                    } catch (_: Exception) {
                        result.success(false)
                    }
                }
                "stop" -> {
                    val intent = Intent(activity, AdvisingForegroundService::class.java).apply {
                        action = AdvisingForegroundService.ACTION_STOP
                    }
                    try {
                        activity.startService(intent)
                        result.success(true)
                    } catch (_: Exception) {
                        result.success(false)
                    }
                }
                "setKeepAwake" -> {
                    val enable = call.argument<Boolean>("enable") ?: false
                    activity.runOnUiThread {
                        if (enable) {
                            activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                        } else {
                            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                        }
                    }
                    result.success(true)
                }
                else -> result.notImplemented()
            }
        }
    }

    private companion object {
        const val CHANNEL = "preconnect/advising_background"
    }
}
