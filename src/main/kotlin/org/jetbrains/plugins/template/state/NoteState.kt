package org.jetbrains.plugins.template.state

import com.intellij.openapi.components.BaseState
import org.jetbrains.plugins.template.dto.Note

class NoteState : BaseState() {
    var notes by list<Note>()
}

