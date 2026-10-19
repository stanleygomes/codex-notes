let currentNoteId = null;

function calculateMenuCoordinate(
  cursorPos,
  menuSize,
  viewportSize,
  padding = 4,
) {
  if (cursorPos + menuSize <= viewportSize - padding) {
    return Math.max(padding, cursorPos);
  }

  const flippedPos = cursorPos - menuSize;
  if (flippedPos >= padding) {
    return flippedPos;
  }

  return Math.max(padding, viewportSize - menuSize - padding);
}

function positionContextMenu(contextMenu, x, y) {
  const left = calculateMenuCoordinate(
    x,
    contextMenu.offsetWidth,
    window.innerWidth,
  );
  const top = calculateMenuCoordinate(
    y,
    contextMenu.offsetHeight,
    window.innerHeight,
  );

  contextMenu.style.left = `${left}px`;
  contextMenu.style.top = `${top}px`;
}

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
  contextMenu.style.visibility = 'hidden';
  contextMenu.style.display = 'block';
  positionContextMenu(contextMenu, x, y);
  contextMenu.style.visibility = 'visible';

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
