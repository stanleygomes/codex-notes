package com.nazarethlabs.notes.dto

import com.intellij.openapi.components.BaseState
import com.nazarethlabs.notes.enum.SortTypeEnum

class Configuration : BaseState() {
    var defaultFileExtension by string(".md")
    var defaultSortType by enum(SortTypeEnum.DATE)
}
