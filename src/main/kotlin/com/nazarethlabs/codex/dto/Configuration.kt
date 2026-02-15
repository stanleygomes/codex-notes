package com.nazarethlabs.codex.dto

import com.intellij.openapi.components.BaseState
import com.nazarethlabs.codex.enum.SortTypeEnum
import com.nazarethlabs.codex.enum.SortTypeEnum.DATE

class Configuration : BaseState() {
    var defaultFileExtension: String = ".md"
    var defaultSortType: SortTypeEnum = DATE
}
