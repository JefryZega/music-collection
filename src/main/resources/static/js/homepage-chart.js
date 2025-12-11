document.addEventListener("DOMContentLoaded", function() {
    const titleElements = document.querySelectorAll(".song-title-data");
    const songLabels = Array.from(titleElements).map(el => el.textContent.trim());
    const likeElements = document.querySelectorAll(".song-likes-data");
    const songCounts = Array.from(likeElements).map(el => parseInt(el.value));
    const ctx = document.getElementById("top10Chart");
    
    // Cek dulu datanya ada gak, biar ga error chart kosong
    if(songLabels.length === 0) return; 

    new Chart(ctx, {
        type: "bar",
        data: {
            labels: songLabels, 
            datasets: [{
                label: "Weekly Likes",
                data: songCounts, 
                backgroundColor: "#3E96F4",
                borderRadius: 5
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: { 
                    beginAtZero: true,
                    ticks: {
                        stepSize: 1 // buletin angka
                    }
                },
                x: {
                    ticks: {
                        color: 'black' 
                    }
                }
            },
            plugins: {
                legend: {
                    labels: { color: 'black' } 
                }
            }
        }
    });
});