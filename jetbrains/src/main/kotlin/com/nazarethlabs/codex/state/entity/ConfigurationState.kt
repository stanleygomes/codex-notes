package com.nazarethlabs.codex.state.entity

import com.intellij.openapi.components.BaseState
import com.nazarethlabs.codex.dto.Configuration

class ConfigurationState : BaseState() {
    var configuration by property(Configuration())
}
