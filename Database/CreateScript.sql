
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

