package org.jetbrains.plugins.template.state

import com.intellij.openapi.components.BaseState
import org.jetbrains.plugins.template.entity.Note

class NoteState : BaseState() {
    var notes by list<Note>()
}

