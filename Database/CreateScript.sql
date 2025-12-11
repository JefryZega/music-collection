DROP TABLE IF EXISTS favourites CASCADE;
DROP TABLE IF EXISTS song CASCADE;
DROP TABLE IF EXISTS album CASCADE;
DROP TABLE IF EXISTS artist CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- CREATE
CREATE TABLE users (
    userID BIGSERIAL PRIMARY KEY,
    "name" VARCHAR(100),
    username VARCHAR(100) UNIQUE,
    email VARCHAR(100) UNIQUE,
    password_hash VARCHAR(255),
    "role" VARCHAR(20) NOT NULL CHECK (role IN ('member', 'admin')) 
);

CREATE TABLE artist (
    artistID BIGSERIAL PRIMARY KEY,
    artistName VARCHAR(100),
    artistProfile VARCHAR(255) -- ganti ini jadi VARCHAR
);

CREATE TABLE album (
    albumID BIGSERIAL PRIMARY KEY,
    albumTitle VARCHAR(100),
    album_art VARCHAR(255), -- ganti ini jadi VARCHAR
    artistID BIGINT REFERENCES artist(artistID) ON DELETE CASCADE
);

CREATE TABLE song (
    songID BIGSERIAL PRIMARY KEY,
    title VARCHAR(100),
    albumID BIGINT REFERENCES album(albumID) ON DELETE CASCADE
);

CREATE TABLE favourites(
    favouritesID BIGSERIAL PRIMARY KEY,
    userID BIGINT REFERENCES users(userID) ON DELETE CASCADE,
    songID BIGINT REFERENCES song(songID) ON DELETE CASCADE,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(userID, songID)
);

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

INSERT INTO artist (artistName, artistProfile) VALUES
('Travis Scott', '/assets/img/artist-profile/artist-travis.jpg'),      -- done
('Billie Eilish', '/assets/img/artist-profile/artist-billie.png'),    -- done
('Metallica', '/assets/img/artist-profile/artist-metallica.jpg'),     -- done
('Shawn Mendes', '/assets/img/artist-profile/artist-shawn.png'),      -- done
('Ed Sheeran', '/assets/img/artist-profile/artist-ed-sheeran.jpg'),   -- done
('AC/DC', '/assets/img/artist-profile/artist-acdc.jpeg'),             -- done
('Bon Jovi', '/assets/img/artist-profile/artist-bonjovi.jpg'),        -- done
('Eagles', '/assets/img/artist-profile/artist-eagles.jpg'),           -- done
('Michael Jackson', '/assets/img/artist-profile/artist-michael-jackson.jpg'), -- done
('Nirvana', '/assets/img/artist-profile/artist-nirvana.jpeg'),        -- done
('Pink Floyd', '/assets/img/artist-profile/artist-pink-floyd.jpeg'),  -- done
('Queen', '/assets/img/artist-profile/artist-queen.jpg'),             -- done
('Slank', '/assets/img/artist-profile/artist-slank.jpeg'),            -- done
('The Beatles', '/assets/img/artist-profile/artist-the-beatles.jpg'); -- done

INSERT INTO album (albumTitle, album_art, artistID) VALUES
('ASTROWORLD', '/assets/img/album-art/album-astroworld.jpeg', 1), 		-- done
('Happier Than Ever', '/assets/img/album-art/album-happier.jpg', 2),   -- done
('Master Of Puppets', '/assets/img/album-art/album-master.png', 3), 	-- done
('Deluxe', '/assets/img/album-art/album-deluxe.png', 4),				-- done 
('Back in Black', '/assets/img/album-art/album-acdc.png', 6), 			-- done
('Highway to Hell', '/assets/img/album-art/album-acdc.png', 6), 		-- done
('Slippery When Wet', '/assets/img/album-art/album-bonjovi-1.jpg', 7), -- done
('Keep the Faith', '/assets/img/album-art/album-bonjovi-2.jpeg', 7), 	-- done
('Hotel California', '/assets/img/album-art/album-eagles-1.jpg', 8), 	-- done
('Their Greatest Hits', '/assets/img/album-art/album-eagles-2.png', 8), -- done
('Thriller', '/assets/img/album-art/album-michael-jackson-1.png', 9),	-- done
('Bad', '/assets/img/album-art/album-michael-jackson-2.png', 9),		-- done
('Nevermind', '/assets/img/album-art/album-nirvana-1.jpg', 10),		-- done
('In Utero', '/assets/img/album-art/album-nirvana-2.jpeg', 10),		-- done
('The Dark Side of the Moon', '/assets/img/album-art/album-pink-floyd-1.jpeg', 11), -- done
('The Wall', '/assets/img/album-art/album-pink-floyd-2.jpeg', 11),		-- done
('A Night at the Opera', '/assets/img/album-art/album-queen-1.jpg', 12), -- done
('Greatest Hits', '/assets/img/album-art/album-queen-2.jpg', 12),		-- done
('Slank XII', '/assets/img/album-art/album-slank.jpeg', 13),			-- done
('Generasi Biru', '/assets/img/album-art/album-slank.jpeg', 13),		-- done
('Abbey Road', '/assets/img/album-art/album-the-beatles-1.png', 14),	-- done
('The Beatles (White Album)', '/assets/img/album-art/album-the-beatles-2.png', 14);	-- done


INSERT INTO song (title, albumID) VALUES
-- Travis Scott
('STARGAZING', 1), ('SICKO MODE', 1), ('NO BYSTANDERS', 1), ('SKELETONS', 1),

-- Billie Eilish
('Getting Older', 2), ('my future', 2), ('Oxytocin', 2), ('GOLDWING', 2),

-- (Metallica
('Battery', 3), ('Welcome Home', 3), ('Lepper Messiah', 3), ('Orion', 3),

-- Shawn Mendes
('Castle on the Hill', 4), ('Shape of You', 4), ('Perfect', 4), ('Happier', 4),

-- AC/DC
('Back in Black', 5), ('Hells Bells', 5), ('Shoot to Thrill', 5), ('You Shook Me All Night Long', 5),
('Highway to Hell', 6), ('Girls Got Rhythm', 6), ('Walk All Over You', 6), ('Touch Too Much', 6),

-- Bon Jovi
('Livin'' on a Prayer', 7), ('You Give Love a Bad Name', 7), ('Wanted Dead or Alive', 7), ('Never Say Goodbye', 7),
('Keep the Faith', 8), ('Bed of Roses', 8), ('In These Arms', 8), ('I''ll Sleep When I''m Dead', 8),

-- Eagles
('Hotel California', 9), ('New Kid in Town', 9), ('Life in the Fast Lane', 9), ('Wasted Time', 9),
('Take It Easy', 10), ('Desperado', 10), ('Tequila Sunrise', 10), ('Best of My Love', 10),

-- Michael Jackson
('Billie Jean', 11), ('Beat It', 11), ('Thriller', 11), ('Human Nature', 11),
('Bad', 12), ('The Way You Make Me Feel', 12), ('Man in the Mirror', 12), ('Smooth Criminal', 12),

-- Nirvana 
('Smells Like Teen Spirit', 13), ('Come as You Are', 13), ('Lithium', 13), ('In Bloom', 13),
('Heart-Shaped Box', 14), ('Oh a Plain', 14), ('Dumb', 14), ('All Apologies', 14),

-- Pink Floyd
('Money', 15), ('Time', 15), ('Us and Them', 15), ('Brain Damage', 15),
('Another Brick in the Wall, Pt. 2', 16), ('Comfortably Numb', 16), ('Hey You', 16), ('Mother', 16),

-- Queen 
('Bohemian Rhapsody', 17), ('You''re My Best Friend', 17), ('Love of My Life', 17), ('Another One Bites the Dust', 17),
('We Will Rock You', 18), ('We Are the Champions', 18), ('Don''t Stop Me Now', 18), ('Somebody to Love', 18),

-- Slank
('Kutunggu Kau di Pasar Minggu', 19), ('Terlalu Manis', 19), ('Mawar Merah', 19), ('Ibu', 19),
('Generasi Biru', 20), ('Mawar Merah', 20), ('Balikin', 20), ('Ku Tak Bisa', 20),

-- Beatles
('Come Together', 21), ('Something', 21), ('Here Comes the Sun', 21), ('Octopus''s Garden', 21),
('While My Guitar Gently Weeps', 22), ('Blackbird', 22), ('Helter Skelter', 22), ('Dear Prudence', 22);


-- FAVOURITES (Buat top 10 weekly)
INSERT INTO favourites (userID, songID, added_at) VALUES 

-- Bohemian Rhapsody (Queen)
(1, 69, NOW() - INTERVAL '10 minutes'),
(2, 69, NOW() - INTERVAL '2 hours'),
(3, 69, NOW() - INTERVAL '1 day'),
(4, 69, NOW() - INTERVAL '2 days'),
(5, 69, NOW() - INTERVAL '3 days'),

-- Smells Like Teen Spirit (Nirvana)
(1, 53, NOW() - INTERVAL '15 minutes'),
(3, 53, NOW() - INTERVAL '5 hours'),
(6, 53, NOW() - INTERVAL '1 day'),
(7, 53, NOW() - INTERVAL '4 days'),

-- Hotel California (Eagles)
(2, 37, NOW() - INTERVAL '30 minutes'),
(4, 37, NOW() - INTERVAL '6 hours'),
(8, 37, NOW() - INTERVAL '5 days'),

-- Billie Jean (MJ)
(5, 45, NOW() - INTERVAL '1 hour'),
(6, 45, NOW() - INTERVAL '3 hours'),

-- Lagu Lama (Gak masuk chart > 7 hari)
(1, 1, NOW() - INTERVAL '1 month'), 
(1, 2, NOW() - INTERVAL '25 days'), 
(2, 4, NOW() - INTERVAL '1 month'), 
(3, 5, NOW() - INTERVAL '2 months'),

(1, 25, NOW() - INTERVAL '1 day'),  
(2, 26, NOW() - INTERVAL '2 days'), 
(3, 80, NOW() - INTERVAL '3 days'), 
(4, 81, NOW() - INTERVAL '12 hours'), 
(5, 88, NOW() - INTERVAL '4 days'), 
(6, 87, NOW() - INTERVAL '5 days'), 
(7, 25, NOW() - INTERVAL '6 days'), 
(8, 53, NOW() - INTERVAL '7 hours');