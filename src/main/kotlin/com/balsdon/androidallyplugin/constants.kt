@file:Suppress("TopLevelPropertyNaming")
package com.balsdon.androidallyplugin

const val elementMaxHeight = 40 // TODO: This should be a calculation

const val TB4DWebPage = "https://ally-keys.com/tb4d.html"
const val TB4DWebPageNeedHelp = "https://ally-keys.com/need_help.html"
const val TB4DInstallHelpWebPage = "$TB4DWebPage#installation-help"
const val TB4DPackageName = "com.android.talkback4d"
const val TB4DExpectedVersion = "TfPu_release_15_1-2024_11_17_1446-TB4D_0_0_4"
const val LatestPhoneApkFileName = "talkback-phone-release-signed-95.apk"
const val LatestWearApkFileName = "talkback-wear-release-signed-95.apk"

fun isTb4dVersionMatching(installedVersion: String?): Boolean =
    installedVersion == TB4DExpectedVersion