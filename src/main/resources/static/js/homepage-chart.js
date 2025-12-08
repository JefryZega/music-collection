// 1. Ambil judul lagu dari card
const songCards = document.querySelectorAll(".songs-card p");
const songLabels = Array.from(songCards).map(card => card.textContent.trim());

// 2. Untuk sekarang, buat angka acak 10–100 (dummy)
const songCounts = songLabels.map(() => Math.floor(Math.random() * 90) + 10);

// 3. Render chart
const ctx = document.getElementById("top10Chart");

new Chart(ctx, {
    type: "bar",
    data: {
        labels: songLabels,
        datasets: [{
            label: "Top 10 Songs",
            data: songCounts,
            backgroundColor: "#3E96F4"
        }]
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
            y: { beginAtZero: true },
            x: {}
        }
    }
});
