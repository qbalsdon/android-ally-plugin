package com.balsdon.androidallyplugin.utils


// TODO: Fix this: Logger.isDebugEnabled always returns false
fun log(message: String) {
//    if (Logger.isDebugEnabled) {
        println(message)
//    }
}

fun log(exception: Exception) {
//    if (Logger.isDebugEnabled) {
        println("!! EXCEPTION [${exception::class.java.simpleName}] !!")
        println("${exception.javaClass.name} ${exception.message}")
        println(exception.stackTrace)
        println("---------------------")
//    }
}