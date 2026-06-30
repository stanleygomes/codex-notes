const vscode = acquireVsCodeApi();
let activeDateFilter = null;
let filterFavorites = false;

const searchBox = document.getElementById('searchBox');
const sortSelect = document.getElementById('sortSelect');
const filterFavBtn = document.getElementById('filterFav');
const dateFilter = document.getElementById('dateFilter');

searchBox.addEventListener('input', () => {
  vscode.postMessage({ command: 'search', query: searchBox.value });
});

sortSelect.addEventListener('change', () => {
  vscode.postMessage({ command: 'sort', sortType: sortSelect.value });
});

filterFavBtn.addEventListener('click', () => {
  filterFavorites = !filterFavorites;
  filterFavBtn.classList.toggle('active', filterFavorites);
  vscode.postMessage({ command: 'filterFavorites', filterFavorites });
});

dateFilter.addEventListener('change', () => {
  vscode.postMessage({
    command: 'filterDate',
    dateFilter: dateFilter.value || null,
  });
});
