
-- drop table favourites
-- drop table users
-- drop table song
-- drop table album
-- drop table artist

-- Ini file sql belum pasti, soalnya masih ada yg mau gw tanya ke dosen

-- CREATE
CREATE TABLE users (
	userID BIGSERIAL PRIMARY KEY,
	"name" VARCHAR(100),
	username VARCHAR(100) UNIQUE,
	email VARCHAR(100) UNIQUE,
	password_hash VARCHAR(255), -- karena hash dipanjangin
	"role" VARCHAR(6) NOT NULL CHECK (role IN ('member', 'admin'))
	-- member gausah jadi role
);

CREATE TABLE artist (
	artistID BIGSERIAL PRIMARY KEY,
	artistName VARCHAR(100),
	artistProfile TEXT
);

CREATE TABLE album (
	albumID BIGSERIAL PRIMARY KEY,
	albumTitle VARCHAR(100),
	album_art TEXT,
	artistID BIGINT REFERENCES artist(artistID) ON DELETE CASCADE
	-- ON DELETE CASCADE biar kalo ngehapus artist, album juga ikut kehapus
);

CREATE TABLE song (
	songID BIGSERIAL PRIMARY KEY,
	title VARCHAR(100),
	albumID BIGINT REFERENCES album(albumID) ON DELETE CASCADE
	-- ini ntar pas dicoding, kalau mau nambahin lagu, tampilin pilihan album title nya yang sudah ada
	-- atau bikin opsi bikin album baru pas nambahin lagu?
);

CREATE TABLE favourites(
	favouritesID BIGSERIAL PRIMARY KEY,
	userID BIGINT REFERENCES users(userID) ON DELETE CASCADE,
	songID BIGINT REFERENCES song(songID) ON DELETE CASCADE,

	-- Lagu terfavorit didasarkan kepada banyaknya member yang menambahkan lagu tersebut pada koleksinya pada minggu tersebut
	-- jadi harus add timestamp saat penambahan lagu, buat track yang minggu ini top
	added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

	-- biar ga duplikat lagu
	UNIQUE(userID, songID)
);

-- NOTE : kata AI buat poster top 10 gausah disimpen di DB, bisa upload ke file projek spring nya lgs



-- INSERT
INSERT INTO users ("name", username, email, password_hash, "role") VALUES 
('Alexander Constantijn', 'alexcon', 'alexcon@gmail.com', 'hashedpass', 'member'),
('Alexander Stanislaus', 'alexstan', 'alexstan@gmail.com', 'hashedpass', 'member'),
('Alfonsus Nugraha', 'alfon', 'alfon@gmail.com', 'hashedpass', 'member'),
('Bima Rahmadani', 'bima', 'bima@gmail.com', 'hashedpass', 'member'),
('Michael Audric', 'mike', 'mike@gmail.com', 'hashedpass', 'admin'),
('Arnold Ronan', 'arnold', 'arnold@gmail.com', 'hashedpass', 'admin'),
('Azriel Adrien', 'azriel', 'azriel@gmail.com', 'hashedpass', 'admin'),
('Jefry Krisman', 'jefry', 'jefry@gmail.com', 'hashedpass', 'admin');

INSERT INTO artist (artistName) VALUES
('Travis Scott'), ('Billie Eilish'), ('Metallica'), ('Shawn Mendes'), ('Ed Sheeran');

INSERT INTO album (albumTitle, artistID) VALUES
('ASTROWORLD', '1'), ('Happier Then Ever', '2'), ('Master Of Puppets', '3'), ('Deluxe', '4');

INSERT INTO song (title, albumID) VALUES
('STARGAZING', '1'), ('SICKO MODE', '1'), ('NO BYSTANDERS', '1'), ('SKELETONS', '1'),
('Getting Older', '2'), ('my future', '2'), ('Oxytocin', '2'), ('GOLDWING', '2'),
('Battery', '3'), ('Welcome Home', '3'), ('Lepper Messiah', '3'), ('Orion', '3'),
('Castle on the Hill', '4'), ('Shape of You', '4'), ('Perfect', '4'), ('Happier', '4');

INSERT INTO favourites (userID, songID) VALUES 
('1', '1'), ('1', '2'), ('1', '3'), 
('2', '4'), ('2', '5'), ('2', '6'), ('2', '7'),
('3', '5'), ('3', '7'), ('3', '9'), ('3', '11'),
('4', '12'), ('4', '13'), ('4', '14'), ('4', '5'), ('4', '4'), ('4', '3'),
('6', '10'), ('6', '1'), ('6', '2'), ('6', '3'),
('7', '9'), ('7', '11'),
('8', '9'), ('8', '10'), ('8', '12'), ('8', '13');

'''
DELETE FROM favourites WHERE favouritesID >= 29;
DELETE FROM song WHERE songID >= 17;
DELETE FROM album WHERE albumID >= 5;
DELETE FROM artist WHERE artistID >= 6;

-- ============================================
-- STEP 2: RESET sequences dengan benar
-- ============================================
SELECT setval('artist_artistid_seq', (SELECT COALESCE(MAX(artistID), 0) FROM artist));
SELECT setval('album_albumid_seq', (SELECT COALESCE(MAX(albumID), 0) FROM album));
SELECT setval('song_songid_seq', (SELECT COALESCE(MAX(songID), 0) FROM song));
SELECT setval('favourites_favouritesid_seq', (SELECT COALESCE(MAX(favouritesID), 0) FROM favourites));
'''

-- ============================================
-- INSERT ARTIST (10 artist sesuai permintaan)
-- ============================================
INSERT INTO artist (artistName, artistProfile) VALUES
('AC/DC', 'Australian rock band formed in 1973'),
('Bon Jovi', 'American rock band formed in 1983'),
('Eagles', 'American rock band formed in 1971'),
('Ed Sheeran', 'English singer-songwriter'),
('Michael Jackson', 'American singer, songwriter, dancer'),
('Nirvana', 'American rock band formed in 1987'),
('Pink Floyd', 'English rock band formed in 1965'),
('Queen', 'British rock band formed in 1970'),
('Slank', 'Indonesian rock band formed in 1983'),
('The Beatles', 'English rock band formed in 1960');

-- ============================================
-- INSERT ALBUM (20 album)
-- ============================================
INSERT INTO album (albumTitle, artistID) VALUES
-- AC/DC albums
('Back in Black', 6),
('Highway to Hell', 6),

-- Bon Jovi albums
('Slippery When Wet', 7),
('Keep the Faith', 7),

-- Eagles albums
('Hotel California', 8),
('Their Greatest Hits (1971–1975)', 8),

-- Michael Jackson albums
('Thriller', 9),
('Bad', 9),

-- Nirvana albums
('Nevermind', 10),
('In Utero', 10),

-- Pink Floyd albums
('The Dark Side of the Moon', 11),
('The Wall', 11),

-- Queen albums
('A Night at the Opera', 12),
('Greatest Hits', 12),

-- Slank albums
('Slank XII', 13),
('Generasi Biru', 13),

-- The Beatles albums
('Abbey Road', 14),
('The Beatles (White Album)', 14);

-- ============================================
-- STEP 5: INSERT SONG (lanjut dari ID 17)
-- ============================================
INSERT INTO song (title, albumID) VALUES
-- AC/DC - Back in Black
('Back in Black', 5),
('Hells Bells', 5),
('Shoot to Thrill', 5),
('You Shook Me All Night Long', 5),

-- AC/DC - Highway to Hell
('Highway to Hell', 6),
('Girls Got Rhythm', 6),
('Walk All Over You', 6),
('Touch Too Much', 6),

-- Bon Jovi - Slippery When Wet
('Livin'' on a Prayer', 7),
('You Give Love a Bad Name', 7),
('Wanted Dead or Alive', 7),
('Never Say Goodbye', 7),

-- Bon Jovi - Keep the Faith
('Keep the Faith', 8),
('Bed of Roses', 8),
('In These Arms', 8),
('I''ll Sleep When I''m Dead', 8),

-- Eagles - Hotel California
('Hotel California', 9),
('New Kid in Town', 9),
('Life in the Fast Lane', 9),
('Wasted Time', 9),

-- Eagles - Their Greatest Hits
('Take It Easy', 10),
('Desperado', 10),
('Tequila Sunrise', 10),
('Best of My Love', 10),

-- Michael Jackson - Thriller
('Billie Jean', 11),
('Beat It', 11),
('Thriller', 11),
('Human Nature', 11),

-- Michael Jackson - Bad
('Bad', 12),
('The Way You Make Me Feel', 12),
('Man in the Mirror', 12),
('Smooth Criminal', 12),

-- Nirvana - Nevermind
('Smells Like Teen Spirit', 13),
('Come as You Are', 13),
('Lithium', 13),
('In Bloom', 13),

-- Nirvana - In Utero
('Heart-Shaped Box', 14),
('Rape Me', 14),
('Dumb', 14),
('All Apologies', 14),

-- Pink Floyd - The Dark Side of the Moon
('Money', 15),
('Time', 15),
('Us and Them', 15),
('Brain Damage', 15),

-- Pink Floyd - The Wall
('Another Brick in the Wall, Pt. 2', 16),
('Comfortably Numb', 16),
('Hey You', 16),
('Mother', 16),

-- Queen - A Night at the Opera
('Bohemian Rhapsody', 17),
('You''re My Best Friend', 17),
('Love of My Life', 17),
('''39', 17),

-- Queen - Greatest Hits
('We Will Rock You', 18),
('We Are the Champions', 18),
('Don''t Stop Me Now', 18),
('Somebody to Love', 18),

-- Slank - Slank XII
('Kutunggu Kau di Pasar Minggu', 19),
('Terlalu Manis', 19),
('Mawar Merah', 19),
('Ibu', 19),

-- Slank - Generasi Biru
('Generasi Biru', 20),
('Mawar Merah', 20),
('Balikin', 20),
('Ku Tak Bisa', 20),

-- The Beatles - Abbey Road
('Come Together', 21),
('Something', 21),
('Here Comes the Sun', 21),
('Octopus''s Garden', 21),

-- The Beatles - White Album
('While My Guitar Gently Weeps', 22),
('Blackbird', 22),
('Helter Skelter', 22),
('Dear Prudence', 22);

-- ============================================
-- STEP 6: INSERT FAVOURITES (lanjut dari ID 29)
-- ============================================
INSERT INTO favourites (userID, songID) VALUES 
-- User 1 (Alexander Constantijn)
(1, 17), (1, 25), (1, 33), (1, 41), (1, 49),
-- User 2 (Alexander Stanislaus)  
(2, 18), (2, 26), (2, 34), (2, 42), (2, 50),
-- User 3 (Alfonsus Nugraha)
(3, 19), (3, 27), (3, 35), (3, 43), (3, 51),
-- User 4 (Bima Rahmadani)
(4, 20), (4, 28), (4, 36), (4, 44), (4, 52),
-- User 5 (admin)
(5, 21), (5, 29), (5, 37), (5, 45), (5, 53),
-- User 6 (admin)
(6, 22), (6, 30), (6, 38), (6, 46), (6, 54),
-- User 7 (admin)
(7, 23), (7, 31), (7, 39), (7, 47), (7, 55),
-- User 8 (admin)
(8, 24), (8, 32), (8, 40), (8, 48), (8, 56);