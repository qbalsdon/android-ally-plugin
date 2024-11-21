package com.balsdon.androidallyplugin.utils

fun String.isNumeric(): Boolean = this.isNotEmpty() && this
    .removePrefix("-")
    .removePrefix("+")
    .all { it in '0'..'9' }