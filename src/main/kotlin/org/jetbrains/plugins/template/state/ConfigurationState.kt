package org.jetbrains.plugins.template.state

import com.intellij.openapi.components.BaseState
import org.jetbrains.plugins.template.dto.Configuration

class ConfigurationState : BaseState() {
    var configuration by property(Configuration())
}
