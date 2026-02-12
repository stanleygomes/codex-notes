package com.nazarethlabs.notes.state

import com.intellij.openapi.components.BaseState
import com.nazarethlabs.notes.dto.Note

class NoteState : BaseState() {
    var notes by list<Note>()
}
