package com.nazarethlabs.notes.state

import com.intellij.openapi.components.BaseState
import com.nazarethlabs.notes.dto.Configuration

class ConfigurationState : BaseState() {
    var configuration by property(Configuration())
}
