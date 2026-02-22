package com.balsdon.androidallyplugin.model

data class BasicDeviceInfo(
    val name: String,
    val api: String,
    val sdk: String,
    val isWatch: Boolean,
    val packageList: List<String>,
    val tb4dVersion: String? = null
)