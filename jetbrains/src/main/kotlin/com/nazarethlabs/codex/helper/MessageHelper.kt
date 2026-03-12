package com.nazarethlabs.codex.helper

import com.nazarethlabs.codex.MyBundle

object MessageHelper {
    fun getMessage(
        key: String,
        vararg params: Any,
    ): String = MyBundle.message(key, *params)
}
