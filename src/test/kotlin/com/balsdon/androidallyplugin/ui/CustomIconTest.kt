package com.balsdon.androidallyplugin.ui

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.io.File

class CustomIconTest {
    @Test
    fun creates_file_name() {
        val testSubject = CustomIcon::class
            .java
            .getDeclaredMethod("createFileReference")
            .apply { isAccessible = true }
        val testObject = CustomIcon.PHONE

        assertThat(testSubject.invoke(testObject)).isEqualTo("/icons/phone.svg")
    }

    @Test
    fun every_icon_has_light_and_dark() {
        val iconDir = "${System.getProperty("user.dir")}/src/main/resources/icons"
        val names = File(iconDir).listFiles()!!

        val darkFiles = names.filter { it.name.endsWith("_dark.svg") }
        val lightFiles = names.filterNot { it.name.endsWith("_dark.svg") }

        //assertThat(darkFiles.size).isEqualTo(lightFiles.size)
        assertThat(
            darkFiles.map { it.name.replace("_dark", "") }.sorted()
        ).isEqualTo(
            lightFiles.map { it.name }.sorted()
        )
    }

    @Test
    fun every_icon_light_and_dark_specific_colors() {
        val iconDir = "${System.getProperty("user.dir")}/src/main/resources/icons"

        File(iconDir).listFiles()!!.forEach { file ->
            val fileText = file.readText()
            if (file.name.endsWith("_dark.svg")) {
                assertThat(validateColor(fileText, "#afb1b3")).isEmpty()
                assertThat(validateColor(fileText, "#6e6e6e")).isNotEmpty()
            } else {
                assertThat(validateColor(fileText, "#6e6e6e")).isEmpty()
                assertThat(validateColor(fileText, "#afb1b3")).isNotEmpty()
            }
        }
    }

    private fun validateColor(text: String, desiredHex: String): List<String> {
        val regex = Regex("""fill="([^"]*)"""")
        return regex
            .findAll(text)
            .filterNot { match ->
                match.value.substringAfter('=') == "\"none\"" || match.value.substringAfter('=') == "\"$desiredHex\""
            }
            .map { it.value }
            .toList()
    }
}