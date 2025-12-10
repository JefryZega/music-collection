// FUNGSI UNTUK TAMBAH LAGU
addButton = document.getElementById('add-song-button');
popupFormAddSong = document.getElementById('popup-add-song');
cancelAddSong = document.getElementById('cancel-button'); 

addButton.addEventListener('click', () => {
    const disolayPopUp = window.getComputedStyle(popupFormAddSong).display;
    if (disolayPopUp === 'none') {
        popupFormAddSong.style.display = 'flex';
    } 
});

cancelAddSong.addEventListener('click', () => {
    popupFormAddSong.style.display = 'none';
});