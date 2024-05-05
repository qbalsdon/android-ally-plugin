package com.balsdon.androidallyplugin.data

import com.balsdon.androidallyplugin.model.AccessibilityPrinciple
import com.balsdon.androidallyplugin.model.AssistiveTechnologyType
import com.balsdon.androidallyplugin.model.CheckListItem

val checkListItemList = listOf(
    // Accessibility Services
    CheckListItem("scanner", AccessibilityPrinciple.Operable, AssistiveTechnologyType.AccessibilityService),
    CheckListItem("screenreader", AccessibilityPrinciple.Operable, AssistiveTechnologyType.AccessibilityService),
    CheckListItem("magnification", AccessibilityPrinciple.Operable, AssistiveTechnologyType.AccessibilityService),
    CheckListItem("voice", AccessibilityPrinciple.Operable, AssistiveTechnologyType.AccessibilityService),
    CheckListItem("switch", AccessibilityPrinciple.Operable, AssistiveTechnologyType.AccessibilityService),
    CheckListItem("keyboard", AccessibilityPrinciple.Operable, AssistiveTechnologyType.AccessibilityService),

    // User Settings / choices
    CheckListItem("fontScale", AccessibilityPrinciple.Perceivable, AssistiveTechnologyType.AccessibilitySetting),
    CheckListItem("orientation", AccessibilityPrinciple.Perceivable, AssistiveTechnologyType.AccessibilitySetting),
    CheckListItem("time", AccessibilityPrinciple.Operable, AssistiveTechnologyType.AccessibilitySetting),

    // Elements to consider
    CheckListItem("text", AccessibilityPrinciple.Perceivable, AssistiveTechnologyType.AccessibilityRule),
    CheckListItem("caption", AccessibilityPrinciple.Perceivable, AssistiveTechnologyType.AccessibilityRule),
    CheckListItem("colorAlone", AccessibilityPrinciple.Perceivable, AssistiveTechnologyType.AccessibilityRule),

    CheckListItem("size", AccessibilityPrinciple.Operable, AssistiveTechnologyType.AccessibilityRule),
    CheckListItem("actions", AccessibilityPrinciple.Operable, AssistiveTechnologyType.AccessibilityRule),
    CheckListItem("headings", AccessibilityPrinciple.Operable, AssistiveTechnologyType.AccessibilityRule),
    CheckListItem("pause", AccessibilityPrinciple.Operable, AssistiveTechnologyType.AccessibilityRule),

    CheckListItem("errorIdentification", AccessibilityPrinciple.Understandable, AssistiveTechnologyType.AccessibilityRule),
    CheckListItem("inputLabels", AccessibilityPrinciple.Understandable, AssistiveTechnologyType.AccessibilityRule),

    CheckListItem("role", AccessibilityPrinciple.Robust, AssistiveTechnologyType.AccessibilityRule),
    CheckListItem("name", AccessibilityPrinciple.Robust, AssistiveTechnologyType.AccessibilityRule),
    CheckListItem("value", AccessibilityPrinciple.Robust, AssistiveTechnologyType.AccessibilityRule),
)