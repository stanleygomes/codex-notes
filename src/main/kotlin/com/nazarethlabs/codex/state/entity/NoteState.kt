package com.nazarethlabs.codex.state.entity

import com.intellij.openapi.components.BaseState
import com.nazarethlabs.codex.dto.Note

class NoteState : BaseState() {
    var notes by list<Note>()
}
