package com.balsdon.androidallyplugin.model

data class AccessibilityService(
    val friendlyNameReference: String,
    val packageName: String,
    val serviceName: String
)

/**
 * Why not "normal" TalkBack?
 *
 * Because there is no such thing.
 *
 * There are 2 versions of TalkBack, one by Google, one by Samsung.
 * There is also VoiceView for Amazon devices. There is also the
 * commentary screen reader, which some screen reader users prefer.
 *
 * TalkBack for Developers (TB4D) is easily installable on emulators
 * and devices, requires no play store, and has an ADB interface.
 * The ADB interface disambiguates between gesture and actions, even if
 * a helper service where created for TalkBack, you'd still need to
 * install that other service. We could try to use the keyboard shortcuts
 * but ADB can't reliably send combinations, plus it doesn't solve the
 * ambiguity problem: is that keystroke for an ACTION or for a GESTURE?
 *
 * Additionally, now that we have TB4D we can add features specifically
 * aimed at development and not pester Google or have to wait years for
 * updates. TB4D will always be behind the latest version of TalkBack,
 * as Google already takes years to update the open source version.
 */
val supportedAccessibilityServiceList = listOf(
    AccessibilityService(
        "android.accessibility.service.name.accessibility-menu",
        "com.android.systemui.accessibility.accessibilitymenu",
        "com.android.systemui.accessibility.accessibilitymenu.AccessibilityMenuService"
    ),
    AccessibilityService(
        "android.accessibility.service.name.voice-access",
        "com.google.android.apps.accessibility.voiceaccess",
        "com.google.android.apps.accessibility.voiceaccess.JustSpeakService"
    ),
    AccessibilityService(
        "android.accessibility.service.name.talkback-for-developers",
        "com.android.talkback4d",
        "com.developer.talkback.TalkBackDevService"
    ),
    AccessibilityService(
        "android.accessibility.service.name.accessibility-scanner",
        "com.google.android.apps.accessibility.auditor",
        "com.google.android.apps.accessibility.auditor.ScannerService"
    ),
    AccessibilityService(
        "android.accessibility.service.name.switch-access",
        "com.google.android.accessibility.switchaccess",
        "com.android.switchaccess.SwitchAccessService"
    ),
    AccessibilityService(
        "android.accessibility.service.name.display-service",
        "com.balsdon.ally.teaching",
        "com.balsdon.ally.teaching.DisplayAccessibilityService"
    ),
)

fun filterFromPackages(input: List<String>): List<AccessibilityService> {
    val validServices = supportedAccessibilityServiceList.map { it.packageName }
    val serviceNames = input.filter { nameWithPrefix ->
        nameWithPrefix.replace("package:", "") in validServices
    }.map { name ->
        name.replace("package:", "")
    }

    return supportedAccessibilityServiceList.filter { service -> service.packageName in serviceNames }
}