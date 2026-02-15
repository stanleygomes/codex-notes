package com.nazarethlabs.notes.dto

import com.intellij.openapi.components.BaseState
import com.nazarethlabs.notes.enum.SortTypeEnum
import com.nazarethlabs.notes.enum.SortTypeEnum.DATE

class Configuration : BaseState() {
    var defaultFileExtension: String = ".md"
    var defaultSortType: SortTypeEnum = DATE
}
