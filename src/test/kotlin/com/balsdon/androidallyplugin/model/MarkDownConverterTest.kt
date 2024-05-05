package com.balsdon.androidallyplugin.model

import com.balsdon.androidallyplugin.data.checkListItemList
import com.google.common.truth.Truth.assertThat
import org.apache.commons.lang3.tuple.MutablePair
import org.junit.Test

class MarkDownConverterTest {
    private val selectionState: MutableMap<String, MutablePair<Boolean, Boolean>> =
        checkListItemList.associate { item -> item.id to MutablePair(false, false) }.toMutableMap()

    @Test
    fun no_elements_selected_generate_list_without_not_applicable_and_none_selected() {
        val testSubject = MarkDownConverter()

        val result = testSubject(selectionState)
        val lines = result.split("\n")
        lines.forEachIndexed { index, str -> println("line [${index.toString().padStart(2, '0')}]: $str") }
        assertThat(result.contains("[x]")).isFalse()
        assertThat(result.contains("Not Applicable")).isFalse()
        assertThat(lines.filter { line -> line == "# Accessibility Checklist" }.size)
            .isEqualTo(1)
        assertThat(lines.filter { line -> line == "## Assistive Technology" }.size)
            .isEqualTo(1)
        assertThat(lines.filter { line -> line == "- [ ] Accessibility Scanner: I have tested using the Google Accessibility Scanner ([accessibility scanner][0])" }.size)
            .isEqualTo(1)
        assertThat(lines.filter { line -> line == "## Settings" }.size)
            .isEqualTo(1)
        assertThat(lines.filter { line -> line == "- [ ] Orientation: Device orientation in landscape and portrait are supported ([orientation][7])" }.size)
            .isEqualTo(1)
        assertThat(lines.filter { line -> line == "## Guidance" }.size)
            .isEqualTo(1)
        assertThat(lines.filter { line -> line == "- [ ] Captions: Videos have non-automated captions ([caption preferences][10], [w3c captions][11], [3PlayMedia][12])" }.size)
            .isEqualTo(1)
        assertThat(lines.filter { line -> line == "- [ ] Touch Targets: Elements with which users interact have a minimum target size of 48 x 48dp ([touch targets][14])" }.size)
            .isEqualTo(1)
        assertThat(lines.filter { line -> line == "[0]: https://support.google.com/accessibility/android/answer/6376570?hl=en-GB" }.size)
            .isEqualTo(1)
    }

    @Test
    fun one_element_selected_generate_list_without_not_applicable_and_one_selected() {
        val testSubject = MarkDownConverter()

        val result = testSubject(selectionState.apply {
            this["actions"]!!.left = true
        })
        val lines = result.split("\n")
        lines.forEachIndexed { index, str -> println("line [${index.toString().padStart(2, '0')}]: $str") }
        assertThat(result.contains("[x]")).isTrue()
        assertThat(result.contains("Not Applicable")).isFalse()
        assertThat(lines.filter { line -> line == "# Accessibility Checklist" }.size)
            .isEqualTo(1)
        assertThat(lines.filter { line -> line == "## Assistive Technology" }.size)
            .isEqualTo(1)
        assertThat(lines.filter { line -> line == "- [ ] Accessibility Scanner: I have tested using the Google Accessibility Scanner ([accessibility scanner][0])" }.size)
            .isEqualTo(1)
        assertThat(lines.filter { line -> line == "## Settings" }.size)
            .isEqualTo(1)
        assertThat(lines.filter { line -> line == "- [ ] Orientation: Device orientation in landscape and portrait are supported ([orientation][7])" }.size)
            .isEqualTo(1)
        assertThat(lines.filter { line -> line == "## Guidance" }.size)
            .isEqualTo(1)
        assertThat(lines.filter { line -> line == "- [ ] Touch Targets: Elements with which users interact have a minimum target size of 48 x 48dp ([touch targets][14])" }.size)
            .isEqualTo(1)
        assertThat(lines.filter { line -> line == "- [x] Actions: Accessibility actions have been created for secondary actions on card list items ([actions][15])" }.size)
            .isEqualTo(1)
        assertThat(lines.filter { line -> line == "[0]: https://support.google.com/accessibility/android/answer/6376570?hl=en-GB" }.size)
            .isEqualTo(1)
    }

    @Test
    fun no_elements_selected_generate_list_with_not_applicable_and_none_selected() {
        val testSubject = MarkDownConverter()

        val result = testSubject(selectionState.apply {
            this["time"]!!.right = true
            this["pause"]!!.right = true
        })
        val lines = result.split("\n")
        lines.forEachIndexed { index, str -> println("line [${index.toString().padStart(2, '0')}]: $str") }
        assertThat(result.contains("[x]")).isFalse()
        assertThat(result.contains("Not Applicable")).isTrue()

        assertThat(lines.filter { line -> line == "# Accessibility Checklist" }.size)
            .isEqualTo(1)
        assertThat(lines.filter { line -> line == "## Assistive Technology" }.size)
            .isEqualTo(1)
        assertThat(lines.filter { line -> line == "- [ ] Accessibility Scanner: I have tested using the Google Accessibility Scanner ([accessibility scanner][0])" }.size)
            .isEqualTo(1)
        assertThat(lines.filter { line -> line == "## Settings" }.size)
            .isEqualTo(1)
        assertThat(lines.filter { line -> line == "- [ ] Orientation: Device orientation in landscape and portrait are supported ([orientation][7])" }.size)
            .isEqualTo(1)
        assertThat(lines.filter { line -> line == "## Guidance" }.size)
            .isEqualTo(1)
        assertThat(lines.filter { line -> line == "- [ ] Touch Targets: Elements with which users interact have a minimum target size of 48 x 48dp ([touch targets][14])" }.size)
            .isEqualTo(1)
        assertThat(lines.filter { line -> line == "## Not Applicable" }.size)
            .isEqualTo(1)
        assertThat(lines.filter { line -> line == "- ~~[ ] Enough time: Time to take action setting is respected ([enough time][8])~~" }.size)
            .isEqualTo(1)
        assertThat(lines.filter { line -> line == "- ~~[ ] Pause, Stop, Hide: All video content can be paused, stopped and hidden. No video flashes more than 3 times per second ([pause stop hide][17])~~" }.size)
            .isEqualTo(1)
        assertThat(lines.filter { line -> line == "[7]: https://www.w3.org/WAI/WCAG22/Understanding/orientation.html" }.size)
            .isEqualTo(1)
    }
}