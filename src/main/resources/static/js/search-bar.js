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

        // ✅ GANTI ENDPOINT: pakai endpoint yang return albumID juga
        fetch(`/member/api/search-with-album?q=${query}`)
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
                        
                        // ✅ TAMPILKAN JUGA INFO ALBUM
                        div.innerHTML = `
                            <div class="song-main-info">
                                <span class="song-title">${song.title}</span>
                                <span class="artist-name">${song.artistName}</span>
                            </div>
                            <div class="song-album-info">
                                <small>Album: ${song.albumTitle || 'N/A'}</small>
                            </div>
                        `;
                        
                        div.onclick = () => {
                            searchInput.value = song.title;
                            searchResults.style.display = 'none';
                            
                            // ✅ REDIRECT KE ALBUM PAGE JIKA ADA albumID
                            if (song.albumID) {
                                window.location.href = `/album/${song.albumID}`;
                            } else {
                                console.warn("Song tidak punya albumID:", song);
                                // Alternatif: redirect ke song detail page
                                // window.location.href = `/song/${song.songID}`;
                            }
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