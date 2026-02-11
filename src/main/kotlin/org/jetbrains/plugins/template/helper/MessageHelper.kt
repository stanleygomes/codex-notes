package org.jetbrains.plugins.template.helper

import org.jetbrains.plugins.template.MyBundle

object MessageHelper {

    fun getMessage(key: String, vararg params: Any): String = MyBundle.message(key, *params)
}
