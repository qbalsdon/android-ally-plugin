package com.balsdon.androidallyplugin.adb.parameters

enum class TalkBackGranularity {
    Default,
    Headings,
    Controls,
    Links,
    Words,
    Paragraphs,
    Characters,
    Lines,
    Window
    ;

    val talkBackValue = this.name.lowercase()
}