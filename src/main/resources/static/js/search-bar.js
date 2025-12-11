document.addEventListener("DOMContentLoaded", function() {
    const searchInput = document.getElementById('search'); 

    const searchResults = document.getElementById('searchResults');
    const searchContainer = document.getElementById('search-container');

    if (!searchInput || !searchResults) return;

    // Event saat mengetik
    searchInput.addEventListener('input', function() {
        const query = this.value.trim();

        if (query.length === 0) {
            searchResults.style.display = 'none';
            searchResults.innerHTML = '';
            return;
        }

        fetch(`/member/api/search?q=${query}`)
            .then(response => {
                if (!response.ok) throw new Error("Gagal mengambil data");
                return response.json();
            })
            .then(data => {
                searchResults.innerHTML = ''; 
                
                if (data.length > 0) {
                    data.forEach(song => {
                        const div = document.createElement('div');
                        div.classList.add('search-item');
                        
                        div.innerHTML = `
                            <span class="song-title">${song.title}</span>
                            <span class="artist-name">${song.artistName}</span>
                        `;
                        
                        div.onclick = () => {
                            searchInput.value = song.title;
                            searchResults.style.display = 'none';
                            
                            // Redirect ke halaman detail lagu (belum)
                            // window.location.href = '/song/detail/' + song.songID;
                        };
                        
                        searchResults.appendChild(div);
                    });
                } else {
                    const div = document.createElement('div');
                    div.classList.add('not-found');
                    div.innerText = "Lagu tidak ditemukan";
                    searchResults.appendChild(div);
                }
                
                searchResults.style.display = 'block';
            })
            .catch(err => {
                console.error("Error fetching search:", err);
                searchResults.style.display = 'none';
            });
    });

    // klik di luar area search untuk menutup dropdown
    document.addEventListener('click', function(e) {
        if (searchContainer && !searchContainer.contains(e.target)) {
            searchResults.style.display = 'none';
        }
    });
});