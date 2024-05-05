package com.balsdon.androidallyplugin.data

import com.balsdon.androidallyplugin.localize
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CheckListItemListTest {

    @Test
    fun every_checklist_item_has_name_description_link() {
        checkListItemList.forEach { checkListItem ->
            println("every_checklist_item_has_name_description_link: ${checkListItem.id}")
            assertThat(checkListItem.nameId).isEqualTo("checklist.id.${checkListItem.id}.name")
            assertThat(checkListItem.descriptionId).isEqualTo("checklist.id.${checkListItem.id}.description")
            assertThat(checkListItem.linkId).isEqualTo("checklist.id.${checkListItem.id}.link")

            assertThat(localize(checkListItem.nameId).length).isGreaterThan(0)
            assertThat(localize(checkListItem.descriptionId).length).isGreaterThan(0)
            assertThat(localize(checkListItem.linkId).length).isGreaterThan(0)
        }
    }
}