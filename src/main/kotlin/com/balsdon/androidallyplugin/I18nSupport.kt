package com.balsdon.androidallyplugin

import org.jetbrains.annotations.NonNls
import java.text.MessageFormat
import java.util.*

@NonNls
private val bundle = ResourceBundle.getBundle("Strings")

fun localize(stringName: String, vararg params: Any): String {
    val value = bundle.getString(stringName)
    return if (params.isNotEmpty()) MessageFormat.format(value, params) else value
}