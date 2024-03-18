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
        CustomIcon.entries.forEach { testObject ->
            val testSubject = CustomIcon::class
                .java
                .getDeclaredMethod("createFileReference")
                .apply { isAccessible = true }

            val reference = testSubject.invoke(testObject) as String
            val regularFile = File(
                CustomIcon::class
                    .java
                    .getResource(reference)!!.file!!
            )
            val darkFile = File(
                CustomIcon::class
                    .java
                    .getResource(reference.replace(".svg", "_dark.svg"))!!.file!!
            )
            assertThat(regularFile.exists()).isTrue()
            assertThat(darkFile.exists()).isTrue()
        }
    }

    @Test
    fun every_icon_light_and_dark_specific_colors() {
        CustomIcon.entries.forEach { testObject ->
            val testSubject = CustomIcon::class
                .java
                .getDeclaredMethod("createFileReference")
                .apply { isAccessible = true }

            val reference = testSubject.invoke(testObject) as String
            val regularFile = File(
                CustomIcon::class
                    .java
                    .getResource(reference)!!.file!!
            ).readText()
            val darkFile = File(
                CustomIcon::class
                    .java
                    .getResource(reference.replace(".svg", "_dark.svg"))!!.file!!
            ).readText()
            assertThat(validateColor(regularFile, "#6e6e6e")).isEmpty()
            assertThat(validateColor(darkFile, "#afb1b3")).isEmpty()
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