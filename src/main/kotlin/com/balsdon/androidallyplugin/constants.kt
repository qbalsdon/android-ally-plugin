package com.balsdon.androidallyplugin

const val elementMaxHeight = 40 // TODO: This should be a calculation

const val TB4DWebPage = "https://ally-keys.com/tb4d.html"
const val TB4DInstallHelpWebPage = "$TB4DWebPage#installation-help"
const val TB4DPackage = "com.android.talkback4d"
const val TB4DService = "com.developer.talkback.TalkBackDevService"
const val ScannerPackage = "com.google.android.apps.accessibility.auditor"
const val ScannerService = "com.google.android.apps.accessibility.auditor.ScannerService"

const val AdbBroadcast = "am broadcast -a"
const val AdbScriptTB4DParameter = "-e mode"
const val AdbScriptTB4DPackage = "com.a11y.adb"
const val AdbScriptTB4DNext = "$AdbScriptTB4DPackage.next"
const val AdbScriptTB4DPrevious = "$AdbScriptTB4DPackage.previous"
const val AdbScriptTB4DTap = "$AdbScriptTB4DPackage.perform_click_action"
const val AdbScriptTB4DLongTap = "$AdbScriptTB4DPackage.perform_long_click_action"
const val AdbScriptTB4DMenu = "$AdbScriptTB4DPackage.talkback_breakout"
const val AdbScriptTB4DActions = "$AdbScriptTB4DPackage.show_custom_actions"
