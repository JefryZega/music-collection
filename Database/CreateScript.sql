
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


-- ============================================
-- 3. INSERT USERS (SAMA)
-- ============================================
INSERT INTO users ("name", username, email, password_hash, "role") VALUES 
('Alexander Constantijn', 'alexcon', 'alexcon@gmail.com', 'hashedpass', 'member'),
('Alexander Stanislaus', 'alexstan', 'alexstan@gmail.com', 'hashedpass', 'member'),
('Alfonsus Nugraha', 'alfon', 'alfon@gmail.com', 'hashedpass', 'member'),
('Bima Rahmadani', 'bima', 'bima@gmail.com', 'hashedpass', 'member'),
('Michael Audric', 'mike', 'mike@gmail.com', 'hashedpass', 'admin'),
('Arnold Ronan', 'arnold', 'arnold@gmail.com', 'hashedpass', 'admin'),
('Azriel Adrien', 'azriel', 'azriel@gmail.com', 'hashedpass', 'admin'),
('Jefry Krisman', 'jefry', 'jefry@gmail.com', 'hashedpass', 'admin');

-- ============================================
-- 4. INSERT ARTIST (15 ARTIST SEKALIGUS)
-- ============================================
INSERT INTO artist (artistName, artistProfile) VALUES
('Travis Scott', 'American rapper and record producer'),
('Billie Eilish', 'American singer-songwriter'),
('Metallica', 'American heavy metal band'),
('Shawn Mendes', 'Canadian singer-songwriter'),
('Ed Sheeran', 'English singer-songwriter'),
('AC/DC', 'Australian rock band formed in 1973'),
('Bon Jovi', 'American rock band formed in 1983'),
('Eagles', 'American rock band formed in 1971'),
('Michael Jackson', 'American singer, songwriter, dancer'),
('Nirvana', 'American rock band formed in 1987'),
('Pink Floyd', 'English rock band formed in 1965'),
('Queen', 'British rock band formed in 1970'),
('Slank', 'Indonesian rock band formed in 1983'),
('The Beatles', 'English rock band formed in 1960'),
('Coldplay', 'British rock band formed in 1996');

-- ============================================
-- 5. INSERT ALBUM (DENGAN ARTISTID YANG BENAR)
-- ============================================
-- Travis Scott (artistID 1)
INSERT INTO album (albumTitle, artistID) VALUES ('ASTROWORLD', 1);

-- Billie Eilish (artistID 2)
INSERT INTO album (albumTitle, artistID) VALUES ('Happier Then Ever', 2);

-- Metallica (artistID 3)
INSERT INTO album (albumTitle, artistID) VALUES ('Master Of Puppets', 3);

-- Shawn Mendes (artistID 4)
INSERT INTO album (albumTitle, artistID) VALUES ('Deluxe', 4);

-- Ed Sheeran (artistID 5) - tambah album baru
INSERT INTO album (albumTitle, artistID) VALUES ('Divide', 5);

-- AC/DC (artistID 6)
INSERT INTO album (albumTitle, artistID) VALUES ('Back in Black', 6);
INSERT INTO album (albumTitle, artistID) VALUES ('Highway to Hell', 6);

-- Bon Jovi (artistID 7)
INSERT INTO album (albumTitle, artistID) VALUES ('Slippery When Wet', 7);
INSERT INTO album (albumTitle, artistID) VALUES ('Keep the Faith', 7);

-- Eagles (artistID 8)
INSERT INTO album (albumTitle, artistID) VALUES ('Hotel California', 8);
INSERT INTO album (albumTitle, artistID) VALUES ('Their Greatest Hits (1971–1975)', 8);

-- Michael Jackson (artistID 9)
INSERT INTO album (albumTitle, artistID) VALUES ('Thriller', 9);
INSERT INTO album (albumTitle, artistID) VALUES ('Bad', 9);

-- Nirvana (artistID 10)
INSERT INTO album (albumTitle, artistID) VALUES ('Nevermind', 10);
INSERT INTO album (albumTitle, artistID) VALUES ('In Utero', 10);

-- Pink Floyd (artistID 11)
INSERT INTO album (albumTitle, artistID) VALUES ('The Dark Side of the Moon', 11);
INSERT INTO album (albumTitle, artistID) VALUES ('The Wall', 11);

-- Queen (artistID 12)
INSERT INTO album (albumTitle, artistID) VALUES ('A Night at the Opera', 12);
INSERT INTO album (albumTitle, artistID) VALUES ('Greatest Hits', 12);

-- Slank (artistID 13)
INSERT INTO album (albumTitle, artistID) VALUES ('Slank XII', 13);
INSERT INTO album (albumTitle, artistID) VALUES ('Generasi Biru', 13);

-- The Beatles (artistID 14)
INSERT INTO album (albumTitle, artistID) VALUES ('Abbey Road', 14);
INSERT INTO album (albumTitle, artistID) VALUES ('The Beatles (White Album)', 14);

-- Coldplay (artistID 15) - tambah 2 album
INSERT INTO album (albumTitle, artistID) VALUES ('Parachutes', 15);
INSERT INTO album (albumTitle, artistID) VALUES ('A Rush of Blood to the Head', 15);

-- ============================================
-- 6. INSERT SONG (DENGAN ALBUMID YANG BENAR)
-- ============================================
-- Album 1: ASTROWORLD (Travis Scott)
INSERT INTO song (title, albumID) VALUES 
('STARGAZING', 1), ('SICKO MODE', 1), ('NO BYSTANDERS', 1), ('SKELETONS', 1);

-- Album 2: Happier Then Ever (Billie Eilish)
INSERT INTO song (title, albumID) VALUES 
('Getting Older', 2), ('my future', 2), ('Oxytocin', 2), ('GOLDWING', 2);

-- Album 3: Master Of Puppets (Metallica)
INSERT INTO song (title, albumID) VALUES 
('Battery', 3), ('Welcome Home', 3), ('Lepper Messiah', 3), ('Orion', 3);

-- Album 4: Deluxe (Shawn Mendes)
INSERT INTO song (title, albumID) VALUES 
('Castle on the Hill', 4), ('Shape of You', 4), ('Perfect', 4), ('Happier', 4);

-- Album 5: Divide (Ed Sheeran)
INSERT INTO song (title, albumID) VALUES 
('Shape of You', 5), ('Castle on the Hill', 5), ('Perfect', 5), ('Galway Girl', 5);

-- Album 6: Back in Black (AC/DC)
INSERT INTO song (title, albumID) VALUES 
('Back in Black', 6), ('Hells Bells', 6), ('Shoot to Thrill', 6), ('You Shook Me All Night Long', 6);

-- Album 7: Highway to Hell (AC/DC)
INSERT INTO song (title, albumID) VALUES 
('Highway to Hell', 7), ('Girls Got Rhythm', 7), ('Walk All Over You', 7), ('Touch Too Much', 7);

-- Album 8: Slippery When Wet (Bon Jovi)
INSERT INTO song (title, albumID) VALUES 
('Livin'' on a Prayer', 8), ('You Give Love a Bad Name', 8), ('Wanted Dead or Alive', 8), ('Never Say Goodbye', 8);

-- Album 9: Keep the Faith (Bon Jovi)
INSERT INTO song (title, albumID) VALUES 
('Keep the Faith', 9), ('Bed of Roses', 9), ('In These Arms', 9), ('I''ll Sleep When I''m Dead', 9);

-- Album 10: Hotel California (Eagles)
INSERT INTO song (title, albumID) VALUES 
('Hotel California', 10), ('New Kid in Town', 10), ('Life in the Fast Lane', 10), ('Wasted Time', 10);

-- Album 11: Their Greatest Hits (Eagles)
INSERT INTO song (title, albumID) VALUES 
('Take It Easy', 11), ('Desperado', 11), ('Tequila Sunrise', 11), ('Best of My Love', 11);

-- Album 12: Thriller (Michael Jackson)
INSERT INTO song (title, albumID) VALUES 
('Billie Jean', 12), ('Beat It', 12), ('Thriller', 12), ('Human Nature', 12);

-- Album 13: Bad (Michael Jackson)
INSERT INTO song (title, albumID) VALUES 
('Bad', 13), ('The Way You Make Me Feel', 13), ('Man in the Mirror', 13), ('Smooth Criminal', 13);

-- Album 14: Nevermind (Nirvana)
INSERT INTO song (title, albumID) VALUES 
('Smells Like Teen Spirit', 14), ('Come as You Are', 14), ('Lithium', 14), ('In Bloom', 14);

-- Album 15: In Utero (Nirvana)
INSERT INTO song (title, albumID) VALUES 
('Heart-Shaped Box', 15), ('Rape Me', 15), ('Dumb', 15), ('All Apologies', 15);

-- Album 16: The Dark Side of the Moon (Pink Floyd)
INSERT INTO song (title, albumID) VALUES 
('Money', 16), ('Time', 16), ('Us and Them', 16), ('Brain Damage', 16);

-- Album 17: The Wall (Pink Floyd)
INSERT INTO song (title, albumID) VALUES 
('Another Brick in the Wall, Pt. 2', 17), ('Comfortably Numb', 17), ('Hey You', 17), ('Mother', 17);

-- Album 18: A Night at the Opera (Queen)
INSERT INTO song (title, albumID) VALUES 
('Bohemian Rhapsody', 18), ('You''re My Best Friend', 18), ('Love of My Life', 18), ('''39', 18);

-- Album 19: Greatest Hits (Queen)
INSERT INTO song (title, albumID) VALUES 
('We Will Rock You', 19), ('We Are the Champions', 19), ('Don''t Stop Me Now', 19), ('Somebody to Love', 19);

-- Album 20: Slank XII (Slank)
INSERT INTO song (title, albumID) VALUES 
('Kutunggu Kau di Pasar Minggu', 20), ('Terlalu Manis', 20), ('Mawar Merah', 20), ('Ibu', 20);

-- Album 21: Generasi Biru (Slank)
INSERT INTO song (title, albumID) VALUES 
('Generasi Biru', 21), ('Mawar Merah', 21), ('Balikin', 21), ('Ku Tak Bisa', 21);

-- Album 22: Abbey Road (The Beatles)
INSERT INTO song (title, albumID) VALUES 
('Come Together', 22), ('Something', 22), ('Here Comes the Sun', 22), ('Octopus''s Garden', 22);

-- Album 23: White Album (The Beatles)
INSERT INTO song (title, albumID) VALUES 
('While My Guitar Gently Weeps', 23), ('Blackbird', 23), ('Helter Skelter', 23), ('Dear Prudence', 23);

-- Album 24: Parachutes (Coldplay)
INSERT INTO song (title, albumID) VALUES 
('Yellow', 24), ('Trouble', 24), ('Shiver', 24), ('Don''t Panic', 24);

-- Album 25: A Rush of Blood to the Head (Coldplay)
INSERT INTO song (title, albumID) VALUES 
('Clocks', 25), ('The Scientist', 25), ('In My Place', 25), ('Warning Sign', 25);

-- ============================================
-- 7. INSERT FAVOURITES (RANDOM UNTUK TESTING)
-- ============================================
-- User 1: 5 lagu random
INSERT INTO favourites (userID, songID) VALUES 
(1, 1), (1, 10), (1, 25), (1, 40), (1, 55);

-- User 2: 5 lagu random  
INSERT INTO favourites (userID, songID) VALUES 
(2, 5), (2, 15), (2, 30), (2, 45), (2, 60);

-- User 3: 5 lagu random
INSERT INTO favourites (userID, songID) VALUES 
(3, 8), (3, 20), (3, 35), (3, 50), (3, 65);

-- User 4: 5 lagu random
INSERT INTO favourites (userID, songID) VALUES 
(4, 12), (4, 28), (4, 42), (4, 58), (4, 72);

-- User 5 (admin): 5 lagu random
INSERT INTO favourites (userID, songID) VALUES 
(5, 3), (5, 18), (5, 33), (5, 48), (5, 75);

-- User 6 (admin): 5 lagu random
INSERT INTO favourites (userID, songID) VALUES 
(6, 7), (6, 22), (6, 37), (6, 52), (6, 80);

-- User 7 (admin): 5 lagu random
INSERT INTO favourites (userID, songID) VALUES 
(7, 14), (7, 26), (7, 41), (7, 56), (7, 85);

-- User 8 (admin): 5 lagu random
INSERT INTO favourites (userID, songID) VALUES 
(8, 9), (8, 24), (8, 39), (8, 54), (8, 90);