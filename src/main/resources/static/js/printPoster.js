// FUNGSI UNTUK MENCETAK POSTER
printPosterButton = document.getElementById('print-poster-button');
posterSection = document.getElementById('weekly-card');

printPosterButton.addEventListener('click' , () => {
    window.print();
});