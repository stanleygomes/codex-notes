package org.jetbrains.plugins.template.helper

import org.jetbrains.plugins.template.MyBundle

object MessageHelper {

    fun getMessage(key: String): String = MyBundle.message(key)
}
