package org.jetbrains.plugins.template.dto

import com.intellij.openapi.components.BaseState
import org.jetbrains.plugins.template.enum.SortTypeEnum

class Configuration : BaseState() {
    var defaultFileExtension by string(".md")
    var defaultSortType by enum(SortTypeEnum.DATE)
}
