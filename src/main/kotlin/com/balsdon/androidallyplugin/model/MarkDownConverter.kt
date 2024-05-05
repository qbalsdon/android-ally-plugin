package com.balsdon.androidallyplugin.model

import com.balsdon.androidallyplugin.data.checkListItemList
import com.balsdon.androidallyplugin.localize
import org.apache.commons.lang3.tuple.MutablePair

/*
* This is a classic example of something that would be a great object class
* The reason why state isn't managed here is getting references to swing
* objects and toggling their state is a slight nightmare.
*/
class MarkDownConverter {

    private fun List<CheckListItem>.processItems(
        isIgnored: Boolean,
        links: Set<String>,
        state: MutableMap<String, MutablePair<Boolean, Boolean>>
    ) = this
        .filter { checkListItem -> state[checkListItem.id]?.right == isIgnored }
        .joinToString("\n") { checkListItem ->
            val itemState = state[checkListItem.id] ?: MutablePair(false, false)
            val done = itemState.left
            val references = localize(checkListItem.linkId)
                .split(",")
                .joinToString(", ") { rawLink ->
                    val linkDetails = rawLink.split("|")
                    val linkDescription = linkDetails[0].replace('_',' ')
                    val linkURL = linkDetails[1]
                    "[$linkDescription][${links.indexOf(linkURL)}]"
                }

            val sugar = if (isIgnored) "~~" else ""
            "- $sugar[${if (done) "x" else " "}] ${localize(checkListItem.nameId)}: ${localize(checkListItem.descriptionId)} ($references)$sugar"
        }

    operator fun invoke(state: MutableMap<String, MutablePair<Boolean, Boolean>>): String {
        val linkReference = mutableSetOf<String>()
        checkListItemList.forEach { checkListItem ->
            localize(checkListItem.linkId).split(",").forEach { linkText ->
                val linkDetails = linkText.split("|")
                val linkURL = linkDetails[1]
                linkReference.add(linkURL)
            }
        }

        val techList = checkListItemList.filter { it.assistiveType == AssistiveTechnologyType.AccessibilityService }
        val settingList = checkListItemList.filter { it.assistiveType == AssistiveTechnologyType.AccessibilitySetting }
        val ruleList = checkListItemList.filter { it.assistiveType == AssistiveTechnologyType.AccessibilityRule }

        val activeTechList = techList.processItems(false, linkReference, state)
        val activeSettingList = settingList.processItems(false, linkReference, state)
        val activeRuleList = ruleList.processItems(false, linkReference, state)
        val inactiveTechList = techList.processItems(true, linkReference, state)
        val inactiveSettingList = settingList.processItems(true, linkReference, state)
        val inactiveRuleList = ruleList.processItems(true, linkReference, state)

        val references = linkReference.mapIndexed { index, link ->
            "[$index]: $link"
        }.joinToString("\n")

        return listOf(
            if (activeTechList.isEmpty() && activeSettingList.isEmpty() && activeRuleList.isEmpty()) "" else "# $activeGroupHeading",
            if (activeTechList.isEmpty()) "" else "## $serviceGroupHeading",
            activeTechList,
            if (activeSettingList.isEmpty()) "" else "## $settingGroupHeading",
            activeSettingList,
            if (activeRuleList.isEmpty()) "" else "## $ruleGroupHeading",
            activeRuleList,
            if (inactiveTechList.isEmpty() && inactiveSettingList.isEmpty() && inactiveRuleList.isEmpty()) "" else "## $inactiveGroupHeading",
            listOf(inactiveTechList, inactiveSettingList, inactiveRuleList).filter { it.isNotEmpty() }.joinToString("\n"),
            references
        )
            .filter { it.isNotEmpty() }
            .joinToString("\n\n")
    }

    private val activeGroupHeading = localize("panel.checklist.label.active")
    private val inactiveGroupHeading = localize("panel.checklist.label.inactive")
    private val serviceGroupHeading = localize("panel.checklist.label.service")
    private val settingGroupHeading = localize("panel.checklist.label.setting")
    private val ruleGroupHeading = localize("panel.checklist.label.rule")
}
