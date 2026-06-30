let currentNoteId = null;

function showContextMenu(x, y, noteId) {
  currentNoteId = noteId;
  const contextMenu = document.getElementById('contextMenu');
  contextMenu.innerHTML = `
    <div class="context-menu-item" data-action="openNote">Open Note</div>
    <div class="context-menu-separator"></div>
    <div class="context-menu-item" data-action="renameNote">Rename</div>
    <div class="context-menu-item" data-action="duplicateNote">Duplicate</div>
    <div class="context-menu-item" data-action="toggleFavorite">Toggle Favorite</div>
    <div class="context-menu-item" data-action="changeColor">Change Color</div>
    <div class="context-menu-separator"></div>
    <div class="context-menu-item" data-action="openLocation">Open File Location</div>
    <div class="context-menu-separator"></div>
    <div class="context-menu-item" data-action="deleteNote">Delete</div>
  `;
  contextMenu.style.left = x + 'px';
  contextMenu.style.top = y + 'px';
  contextMenu.style.display = 'block';

  contextMenu.querySelectorAll('.context-menu-item').forEach((item) => {
    item.addEventListener('click', () => {
      const action = item.getAttribute('data-action');
      vscode.postMessage({ command: action, noteId: currentNoteId });
      contextMenu.style.display = 'none';
    });
  });
}

document.addEventListener('click', (e) => {
  const contextMenu = document.getElementById('contextMenu');
  if (!contextMenu.contains(e.target)) {
    contextMenu.style.display = 'none';
  }
});
