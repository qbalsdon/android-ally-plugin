package com.balsdon.androidallyplugin.ui.contract

interface MainPlugin {
    fun build(postConstruction: (content: Any) -> Unit)
}