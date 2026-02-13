package com.nazarethlabs.notes.helper

import com.nazarethlabs.notes.MyBundle

object MessageHelper {
    fun getMessage(
        key: String,
        vararg params: Any,
    ): String = MyBundle.message(key, *params)
}
