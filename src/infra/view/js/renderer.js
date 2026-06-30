const notesList = document.getElementById('notesList');

function renderNotes(notes) {
  if (notes.length === 0) {
    notesList.innerHTML = `
      <div class="empty-state">
        <p>No notes yet.</p>
        <button class="primary-btn" id="btnCreateEmpty">Create your first note</button>
      </div>
    `;
    document.getElementById('btnCreateEmpty').addEventListener('click', () => {
      vscode.postMessage({ command: 'createNote' });
    });
    return;
  }
  notesList.innerHTML = notes
    .map((note) => {
      const colorClass = `note-color-${String(note.color || 'NONE').toLowerCase()}`;
      const colorBar = `<div class="note-color-bar ${escapeHtml(colorClass)}"></div>`;
      const favIcon = note.isFavorite
        ? '<span class="note-favorite" title="Favorite">★</span>'
        : '';
      return `
        <div class="note-item" data-id="${note.id}">
          ${colorBar}
          <div class="note-body">
            <div class="note-header">
              <span class="note-title">${escapeHtml(note.title)}</span>
              <div class="note-meta">
                ${favIcon}
                <span class="note-date">${note.dateLabel}</span>
              </div>
            </div>
            ${note.preview ? `<div class="note-preview">${escapeHtml(note.preview)}</div>` : ''}
          </div>
        </div>
      `;
    })
    .join('');

  notesList.querySelectorAll('.note-item').forEach((item) => {
    item.addEventListener('click', (e) => {
      vscode.postMessage({
        command: 'openNote',
        noteId: item.dataset.id,
      });
    });
    item.addEventListener('contextmenu', (e) => {
      e.preventDefault();
      showContextMenu(e.clientX, e.clientY, item.dataset.id);
    });
  });
}

window.addEventListener('message', (event) => {
  const message = event.data;
  if (message.command === 'updateNotes') {
    renderNotes(message.notes);
  }
});
