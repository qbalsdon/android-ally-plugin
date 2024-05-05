package com.balsdon.androidallyplugin.model

enum class AssistiveTechnologyType {
    AccessibilityService,
    AccessibilitySetting,
    AccessibilityRule
}

enum class AccessibilityPrinciple(val webReferenceId: String) {
    Perceivable("link.perceivable"),
    Operable("link.operable"),
    Understandable("link.understandable"),
    Robust("link.robust")
}

data class CheckListItem(
    val id: String,
    val accessibilityPrinciple: AccessibilityPrinciple,
    val assistiveType: AssistiveTechnologyType,
) {
    val nameId = "checklist.id.${id}.name"
    val descriptionId = "checklist.id.${id}.description"
    val linkId = "checklist.id.${id}.link"
}