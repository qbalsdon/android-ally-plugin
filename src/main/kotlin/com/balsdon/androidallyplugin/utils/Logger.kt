package com.balsdon.androidallyplugin.utils

import com.android.tools.idea.connection.assistant.actions.Logger

fun log(message: String) {
    if (Logger.isDebugEnabled) {
        println(message)
    }
}