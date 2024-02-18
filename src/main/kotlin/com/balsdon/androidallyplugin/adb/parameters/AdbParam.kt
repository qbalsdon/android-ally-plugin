package com.balsdon.androidallyplugin.adb.parameters

interface AdbParam {
    fun toAdbValue(): String
}

class UnitAdbParam: AdbParam {
    override fun toAdbValue(): String = Unit.toString()
}

enum class BooleanPrintMode{
    BINARY, BOOLEAN, ENGLISH, FLOAT
}

class AdbBoolean(private val data: Boolean, private val mode: BooleanPrintMode = BooleanPrintMode.BINARY): AdbParam {
    override fun toAdbValue(): String =
        when(mode) {
            BooleanPrintMode.BINARY -> if (data) "1" else "0"
            BooleanPrintMode.BOOLEAN -> data.toString()
            BooleanPrintMode.ENGLISH -> if (data) "yes" else "no"
            BooleanPrintMode.FLOAT -> if (data) "1.0" else "0.0"
        }
}

class AdbParamUsingString<T>(private val data: T, private val convertToLower: Boolean = false): AdbParam {
    override fun toAdbValue(): String = if (convertToLower) data.toString().lowercase() else data.toString()
}

class TypingTextAdbParam(private val text: String): AdbParam {
    override fun toAdbValue(): String = "\"${ text.replace(" ", "\\ ") }\""
}

class ColorCorrectionAdbParam(private val correction: ColorCorrectionType): AdbParam {
    override fun toAdbValue(): String = correction.adbValue.toString()
}

class AdbPositiveValue(private val data: Int, private val inNegativeCase: String): AdbParam {
    override fun toAdbValue(): String = if (data < 0) inNegativeCase else "\"${data}\""
}