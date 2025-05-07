-- Insertar sagas
INSERT INTO sagas (id, nombre, descripcion, fecha_inicio, fecha_fin, is_acabada, imagen) VALUES
('s001', 'El Señor de los Anillos', 'Trilogía épica de fantasía basada en los libros de J.R.R. Tolkien.', '2001-12-19', '2003-12-17', true, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVKgKemtNYpNTqlCMkl3DgptGwhulkT8U1Jg&s'),
('s002', 'Star Wars', 'Saga de space opera creada por George Lucas.', '1977-05-25', NULL, false, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcREHA9BWCRD8LBeUe498kzy7ciKcIfpXc7oXw&s'),
('s003', 'Harry Potter', 'Saga de fantasía basada en los libros de J.K. Rowling.', '2001-11-16', '2011-07-15', true, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTeYG5eD76ri_YGeKPIUDPODd5y-2pdBAjTKg&s'),
('s004', 'Marvel Cinematic Universe', 'Universo cinematográfico de superhéroes de Marvel.', '2008-05-02', NULL, false, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQKPxPpFm1zwYJMYLyk4OMYZKD-YSHvOB06SQ&s'),
('s005', 'James Bond', 'Serie de películas de espionaje basadas en Ian Fleming.', '1962-10-05', NULL, false, 'https://m.media-amazon.com/images/I/71T2aDKfmIL.jpg'),
('s006', 'El Padrino', 'Trilogía sobre la familia criminal Corleone.', '1972-03-24', '1990-12-25', true, 'https://image.tmdb.org/t/p/w500/r4gnMXoY1efvaolNDjn3nj4046S.jpg'),
('s007', 'Trilogía de Batman de Nolan', 'Trilogía oscura del Caballero Oscuro dirigida por Christopher Nolan.', '2005-06-15', '2012-07-20', true, 'https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg'),
('s008', 'Matrix', 'Saga de ciencia ficción sobre la realidad simulada.', '1999-03-31', '2021-12-22', false, 'https://m.media-amazon.com/images/M/MV5BNGE1YzI4NzMtZTUxNi00Y2I5LTg2MmQtODE0NThmYTFmMDk0XkEyXkFqcGc@._V1_.jpg'),
('s010', 'Universo de Tarantino', 'Películas conectadas en el mismo universo cinematográfico.', '1992-09-02', NULL, false, 'https://m.media-amazon.com/images/I/81UTs3sC5hL._AC_UF894,1000_QL80_.jpg'),
('s011', 'Universo de Nolan', 'Películas de Christopher Nolan con temas similares.', '1998-01-01', NULL, false, 'https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg'),
('s012', 'Indiana Jones', 'Aventuras del arqueólogo Indiana Jones', '1981-06-12', '2023-06-30', false, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS6y6M128p9P5KE_NVlnMUSWtmI-6LUiQCgng&s'),
('s013', 'Jurassic Park', 'Dinosaurios clonados causan caos', '1993-06-11', '2022-06-10', false, 'https://m.media-amazon.com/images/I/71X6YzwV0gL._AC_UF1000,1000_QL80_.jpg'),
('s014', 'Misión Imposible', 'Misiones de espionaje de Ethan Hunt', '1996-05-22', NULL, false, 'https://pics.filmaffinity.com/Misiaon_imposible_Sentencia_mortal_Parte_1-595394181-large.jpg'),
('s015', 'Fast & Furious', 'Carreras y crímenes internacionales', '2001-06-22', NULL, false, 'https://www.universalpictures.es/tl_files/content/movies/fast9/fast9_header_mobile.jpg');

-- Insertar tus producciones de ejemplo (películas)
INSERT INTO producciones (id, titulo, tipo, estreno, duracion, clasificacion_edad, sinopsis, informacion, imagen, saga_id, puntuacion) VALUES
('pr018', 'El Padrino', 'PELICULA', '1972-03-24', 175, 3, 'El patriarca de una familia mafiosa transfiere el control de su imperio clandestino a su hijo reacio.', 'Basada en la novela de Mario Puzo.', 'https://image.tmdb.org/t/p/w500/r4gnMXoY1efvaolNDjn3nj4046S.jpg', 's006', 8.6),
('pr019', 'El Caballero Oscuro', 'PELICULA', '2008-07-18', 152, 2, 'Batman tiene que aceptar la ayuda de un nuevo fiscal para combatir a un criminal anarquista conocido como el Joker.', 'Segunda película de la trilogía de Nolan.', 'https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg', 's007', 6.5),
('pr020', 'Pulp Fiction', 'PELICULA', '1994-10-14', 154, 4, 'Las vidas de dos sicarios, un boxeador, la esposa de un gánster y dos bandidos se entrelazan en cuatro historias de violencia y redención.', 'Ganadora de la Palma de Oro en Cannes.', 'https://m.media-amazon.com/images/I/81UTs3sC5hL._AC_UF894,1000_QL80_.jpg', 's010', 8.5),
('pr021', 'Forrest Gump', 'PELICULA', '1994-07-06', 142, 2, 'La presidencia de Kennedy y Johnson, Vietnam, Watergate y otros eventos históricos se desarrollan desde la perspectiva de un hombre de Alabama con un coeficiente intelectual de 75.', 'Ganadora de 6 premios Oscar.', 'https://media.es.wired.com/photos/6679bb407faf643236b6024c/2:3/w_576,h_864,c_limit/Forrest%20Gump.jpg', NULL, 6.5),
('pr022', 'Inception', 'PELICULA', '2010-07-16', 148, 2, 'Un ladrón que roba secretos corporativos a través del uso de la tecnología de compartir sueños es encargado con la tarea inversa de implantar una idea en la mente de un CEO.', 'Dirigida por Christopher Nolan.', 'https://m.media-amazon.com/images/M/MV5BMjExMjkwNTQ0Nl5BMl5BanBnXkFtZTcwNTY0OTk1Mw@@._V1_.jpg', 's011', 6.5),
('pr023', 'The Matrix', 'PELICULA', '1999-03-31', 136, 2, 'Un hacker aprende de misteriosos rebeldes sobre la verdadera naturaleza de su realidad y su papel en la guerra contra sus controladores.', 'Revolucionó los efectos visuales.', 'https://m.media-amazon.com/images/M/MV5BNGE1YzI4NzMtZTUxNi00Y2I5LTg2MmQtODE0NThmYTFmMDk0XkEyXkFqcGc@._V1_.jpg', 's008', 6.5),
('pr024', 'Titanic', 'PELICULA', '1997-12-19', 195, 2, 'Una aristócrata de 17 años se enamora de un artista amable pero pobre a bordo del lujoso e infortunado R.M.S. Titanic.', 'Ganadora de 11 premios Oscar.', 'https://pics.filmaffinity.com/Titanic-321994924-large.jpg', NULL, 6.5),
('pr025', 'Gladiator', 'PELICULA', '2000-05-05', 155, 3, 'Un ex general romano busca venganza contra el corrupto emperador que asesinó a su familia y lo envió a la esclavitud.', 'Ganadora de 5 premios Oscar.', 'https://m.media-amazon.com/images/I/51GA6V6VE1L._AC_UF894,1000_QL80_.jpg', NULL, 6.5),
('pr026', 'The Shawshank Redemption', 'PELICULA', '1994-09-23', 142, 3, 'Dos hombres encarcelados se vinculan durante varios años, encontrando consuelo y eventual redención a través de actos de decencia común.', 'Basada en la novela de Stephen King.', 'https://pics.filmaffinity.com/Cadena_perpetua-576140557-large.jpg', NULL, 6.5),
('pr027', 'The Godfather Part II', 'PELICULA', '1974-12-20', 202, 3, 'La temprana vida y carrera de Vito Corleone en 1920 es representada mientras su hijo, Michael, expande y refuerza su control sobre el sindicato del crimen familiar.', 'Secuela de El Padrino.', 'https://s3.amazonaws.com/nightjarprod/content/uploads/sites/192/2022/10/17140449/hek3koDUyRQk7FIhPXsa6mT2Zc3.jpg', 's006', 6.5),
('pr028', 'The Lord of the Rings: The Return of the King', 'PELICULA', '2003-12-17', 201, 2, 'Gandalf y Aragorn lideran el Mundo de los Hombres contra el ejército de Sauron para distraer su atención de Frodo y Sam mientras se acercan al Monte del Destino con el Anillo Único.', 'Tercera parte de la trilogía.', 'https://m.media-amazon.com/images/M/MV5BMTZkMjBjNWMtZGI5OC00MGU0LTk4ZTItODg2NWM3NTVmNWQ4XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg', 's001', 6.5),
('pr029', 'Interstellar', 'PELICULA', '2014-11-07', 169, 2, 'Un equipo de exploradores viaja a través de un agujero de gusano en el espacio en un intento por asegurar la supervivencia de la humanidad.', 'Dirigida por Christopher Nolan.', 'https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg', 's011', 6.5),
('pr030', 'Whiplash', 'PELICULA', '2014-10-15', 106, 3, 'Un joven baterista ambicioso se inscribe en un conservatorio de música donde su instructor abusivo no se detendrá ante nada para hacer que un estudiante alcance la grandeza.', 'Ganadora de 3 premios Oscar.', 'https://image.tmdb.org/t/p/w500/oPxnRhyAIzJKGUEdSiwTJQBa3NM.jpg', NULL, 6.5),
('pr031', 'La La Land', 'PELICULA', '2016-12-09', 128, 2, 'Mientras navegan por sus carreras en Los Ángeles, un pianista y una actriz se enamoran mientras intentan reconciliar sus aspiraciones para el futuro.', 'Ganadora de 6 premios Oscar.', 'https://image.tmdb.org/t/p/w500/uDO8zWDhfWwoFdKS4fzkUJt0Rf0.jpg', NULL, 6.5),
('pr032', 'Django Desencadenado', 'PELICULA', '2012-12-25', 165, 4, 'Con la ayuda de un cazarrecompensas alemán, un esclavo liberado se embarca en una misión para rescatar a su esposa de un brutal propietario de una plantación en Mississippi.', 'Dirigida por Quentin Tarantino.', 'https://image.tmdb.org/t/p/w500/7oWY8VDWW7thTzWh3OKYRkWUlD5.jpg', 's010', 6.5),
('pr033', 'Indiana Jones y los cazadores del arca perdida', 'PELICULA', '1981-06-12', 115, 2, 'El arqueólogo Indiana Jones busca el Arca de la Alianza', 'Dirigida por Steven Spielberg', 'https://m.media-amazon.com/images/M/MV5BNjEwNjY0ZTAtMjk0MS00MDhkLTkwYWUtMjM3ZmI2M2JhYjJhXkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg', 's012', 6.5),
('pr034', 'Jurassic Park', 'PELICULA', '1993-06-11', 127, 2, 'Un parque temático con dinosaurios clonados sale mal', 'Basada en la novela de Michael Crichton', 'https://i.pinimg.com/736x/51/ba/64/51ba64b2e61f820e0e86bdd2f4c6e92c.jpg', 's013', 6.5),
('pr035', 'Misión Imposible', 'PELICULA', '1996-05-22', 110, 2, 'Ethan Hunt es acusado de traición y debe limpiar su nombre', 'Primera película de la saga', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTYbruGRibbv4xHpmsYZM8x8QR2FfxUIR94WQ&s', 's014', 6.5),
('pr036', 'Fast & Furious', 'PELICULA', '2001-06-22', 106, 3, 'Un policía se infiltra en el mundo de las carreras callejeras', 'Inicio de la franquicia', 'https://www.universalpictures.es/tl_files/content/movies/fast9/fast9_header_mobile.jpg', 's015', 6.5),
('pr037', 'El silencio de los corderos', 'PELICULA', '1991-02-14', 118, 4, 'Una agente del FBI busca ayuda de un asesino en serie', 'Ganadora de 5 Oscars', 'https://m.media-amazon.com/images/I/61DMhNjjJ3L._AC_UF894,1000_QL80_.jpg', NULL, 6.5),
('pr038', 'El club de la lucha', 'PELICULA', '1999-10-15', 139, 4, 'Un oficinista y un vendedor forman un club de lucha subterráneo', 'Basada en la novela de Chuck Palahniuk', 'https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEjptGkW4UwD3BdMJMcAPCcuYD9U9M8GJD3QQ76FIanqoxspZk2q9sdKXSOIx7yiQsF5T-ZdkTHUM21_AEN6vH2OXucUbTnIhQ4CEUbMtWr7TZ-FIklXl5vrGg1fl7vib3E2of5DWCGmE3H3/s2048/Club-de-la-lucha.jpg', NULL, 6.5);

-- Insertar categorías para tus producciones
INSERT INTO produccion_categorias (produccion_id, categoria) VALUES
('pr018', 'DRAMA'), ('pr018', 'CRIMEN'),
('pr019', 'ACCION'), ('pr019', 'CRIMEN'), ('pr019', 'DRAMA'),
('pr020', 'DRAMA'), ('pr020', 'CRIMEN'),
('pr021', 'DRAMA'), ('pr021', 'ROMANCE'),
('pr022', 'ACCION'), ('pr022', 'CIENCIA_FICCION'),
('pr023', 'ACCION'), ('pr023', 'CIENCIA_FICCION'),
('pr024', 'DRAMA'), ('pr024', 'ROMANCE'),
('pr025', 'ACCION'), ('pr025', 'AVENTURA'), ('pr025', 'DRAMA'),
('pr026', 'DRAMA'),
('pr027', 'DRAMA'), ('pr027', 'CRIMEN'),
('pr028', 'AVENTURA'), ('pr028', 'DRAMA'), ('pr028', 'FANTASIA'),
('pr029', 'AVENTURA'), ('pr029', 'DRAMA'), ('pr029', 'CIENCIA_FICCION'),
('pr030', 'DRAMA'), ('pr030', 'MUSICAL'),
('pr031', 'COMEDIA'), ('pr031', 'DRAMA'), ('pr031', 'MUSICAL'), ('pr031', 'ROMANCE'),
('pr033', 'AVENTURA'), ('pr033', 'ACCION'),
('pr034', 'AVENTURA'), ('pr034', 'CIENCIA_FICCION'),
('pr035', 'ACCION'), ('pr035', 'SUSPENSE'),
('pr036', 'ACCION'), ('pr036', 'CRIMEN'),
('pr037', 'SUSPENSE'), ('pr037', 'CRIMEN'), ('pr037', 'DRAMA'),
('pr038', 'DRAMA'), ('pr038', 'SUSPENSE'),
('pr032', 'DRAMA'), ('pr032', 'WESTERN');

-- Insertar profesionales adicionales (actores y directores de tus películas)
INSERT INTO profesionales (id, nombre, fecha_nacimiento, lugar_nacimiento, biografia, foto, fecha_inicio) VALUES
('p001', 'Tom Hanks', '1956-07-09', 'Concord, California, USA', 'Dos veces ganador del Oscar, conocido por Forrest Gump y Toy Story.', 'https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcRMbEBRIFN_A3mQS7etzyoVo7UGJ8Xr3xMS1KmMaujuH7qDKttQdWs74OoYRgIj3C7OR0YRzTbjsSLL0VMXXLy_ug', '1978-01-01'),
('p003', 'Christopher Nolan', '1970-07-30', 'London, England', 'Director aclamado por Inception, The Dark Knight y Dunkirk.', 'https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Christopher_Nolan_Cannes_2018.jpg/640px-Christopher_Nolan_Cannes_2018.jpg', '1998-01-01'),
('p005', 'Leonardo DiCaprio', '1974-11-11', 'Los Angeles, California, USA', 'Ganador del Oscar por The Revenant.', 'https://encrypted-tbn2.gstatic.com/licensed-image?q=tbn:ANd9GcSwLp-COysT7ukjE2mk6WglJk2dHvB5SI3JAjfUyUYFl7KNo6VuHu_hCeE6b9v0vgm-u6ZMm2PWqEv1S_g', '1989-01-01'),
('p006', 'Quentin Tarantino', '1963-03-27', 'Knoxville, Tennessee, USA', 'Director conocido por Pulp Fiction y Kill Bill.', 'https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcQLFccL-uOxuyzMNc8uiorY9ok9Um2s5JKegklQvCrgggv1LmUOPvjeUJXiu9xTKKHoYd96H5NDa5CuEM28EjukbZDlc9NaCJtCyKdX9-o', '1987-01-01'),
('p011', 'Marlon Brando', '1924-04-03', 'Omaha, Nebraska, USA', 'Actor legendario conocido por El Padrino y Un tranvía llamado Deseo.', 'https://upload.wikimedia.org/wikipedia/commons/thumb/5/53/Marlon_Brando_publicity_for_One-Eyed_Jacks.png/960px-Marlon_Brando_publicity_for_One-Eyed_Jacks.png', '1944-01-01'),
('p012', 'Al Pacino', '1940-04-25', 'New York City, USA', 'Icono del cine ganador de un Oscar por Esencia de mujer.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3-uOWsjpJ16aFTW9pvfZiZdu-4omB9xFNQE9DyKN0fJlK4Au84qzLvE1ppyXxrABJOuqZfkzbkh-1SfitvrCYD9MtSCrdnGKPVnhs6w', '1967-01-01'),
('p013', 'James Caan', '1940-03-26', 'New York City, USA', 'Actor conocido por El Padrino y Misery.', 'https://www.lavanguardia.com/peliculas-series/images/profile/1940/3/w300/bGyOCCOIgcIyKjOGLoXoyp0XWHf.jpg', '1961-01-01'),
('p014', 'Francis Ford Coppola', '1939-04-07', 'Detroit, Michigan, USA', 'Aclamado director de El Padrino y Apocalypse Now.', 'https://upload.wikimedia.org/wikipedia/commons/4/41/Francis_Ford_Coppola.jpg', '1962-01-01'),
('p015', 'Christian Bale', '1974-01-30', 'Haverfordwest, Wales', 'Actor versátil conocido por El Caballero Oscuro y El luchador.', 'https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcQOdncgz6XwkeU5omE8IOCU7emrzvTWKYR4O1CfZw5MnJwC8G0YZe6oJEWBVS0TOR2qB3MaO3CjvpePnX41umfcE1T6MbnjPO1yF3wRZBE', '1982-01-01'),
('p016', 'Heath Ledger', '1979-04-04', 'Perth, Australia', 'Actor ganador del Oscar por su papel del Joker.', 'https://m.media-amazon.com/images/M/MV5BMTI2NTY0NzA4MF5BMl5BanBnXkFtZTYwMjE1MDE0._V1_FMjpg_UX1000_.jpg', '1992-01-01'),
('p017', 'Aaron Eckhart', '1968-03-12', 'Cupertino, California, USA', 'Actor conocido por El Caballero Oscuro y Thank You for Smoking.', 'https://www.lavanguardia.com/peliculas-series/images/all/profile/1968/3/6383/w1280/6rI3cYhdxOaOFSRB4C29MalpwDC.jpg', '1992-01-01'),
('p018', 'John Travolta', '1954-02-18', 'Englewood, New Jersey, USA', 'Actor de Pulp Fiction y Fiebre del sábado noche.', 'https://es.web.img2.acsta.net/pictures/18/05/15/15/20/5209194.jpg', '1969-01-01'),
('p019', 'Uma Thurman', '1970-04-29', 'Boston, Massachusetts, USA', 'Actriz conocida por Pulp Fiction y Kill Bill.', 'https://upload.wikimedia.org/wikipedia/commons/4/42/Uma_Thurman_Cannes_2017_%28cropped%29.jpg', '1985-01-01'),
('p020', 'Samuel L. Jackson', '1948-12-21', 'Washington, D.C., USA', 'Prolífico actor conocido por Pulp Fiction y Avengers.', 'https://www.lavanguardia.com/peliculas-series/images/profile/1948/12/w300/nCJJ3NVksYNxIzEHcyC1XziwPVj.jpg', '1972-01-01'),
('p021', 'Robin Wright', '1966-04-08', 'Dallas, Texas, USA', 'Actriz conocida por Forrest Gump y House of Cards.', 'https://upload.wikimedia.org/wikipedia/commons/d/d9/Robin_Wright_Cannes_2017_%28cropped%29.jpg', '1983-01-01'),
('p022', 'Gary Sinise', '1955-03-17', 'Blue Island, Illinois, USA', 'Actor conocido por Forrest Gump y CSI: NY.', 'https://upload.wikimedia.org/wikipedia/commons/thumb/9/90/Gary_Sinise_2011_%28cropped%29.jpg/250px-Gary_Sinise_2011_%28cropped%29.jpg', '1982-01-01'),
('p023', 'Robert Zemeckis', '1952-05-14', 'Chicago, Illinois, USA', 'Director de Forrest Gump y Regreso al futuro.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTCAjFSuumds-l6ijrvhig2yBfq40-2dgziuD-5khAR-7C-RvjRE4QOGsNJDbvC6h4No-GqyT_ftG8IVmgJbQHwJdb3YApwoOAm0JgX4As', '1978-01-01'),
('p024', 'Joseph Gordon-Levitt', '1981-02-17', 'Los Angeles, California, USA', 'Actor conocido por Inception y 500 Days of Summer.', 'https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcRNSAiomuqhFFFjpcJxirnpAdJf5zsf72Zej8XqWXCVEQadJEjQ6ndnMCPfyz2PWUep-_YHgGQN6zH7oWZs5sKIe6KzG-DkEmAx5s52QQ', '1988-01-01'),
('p025', 'Ellen Page', '1987-02-21', 'Halifax, Canada', 'Actriz conocida por Inception y Juno.', 'https://cdn.britannica.com/41/249341-050-E5F7039C/Actor-Elliot-Page-2022.jpg', '1997-01-01'),
('p026', 'Keanu Reeves', '1964-09-02', 'Beirut, Lebanon', 'Actor de Matrix y John Wick.', 'https://www.lavanguardia.com/peliculas-series/images/all/profile/1964/9/6384/w1280/kEoUZKEG7dzbCESDjd0CKAN1r0n.jpg', '1984-01-01'),
('p027', 'Laurence Fishburne', '1961-07-30', 'Augusta, Georgia, USA', 'Actor conocido por Matrix y CSI.', 'https://m.media-amazon.com/images/M/MV5BMTc0NjczNDc1MV5BMl5BanBnXkFtZTYwMDU0Mjg1._V1_FMjpg_UX1000_.jpg', '1972-01-01'),
('p028', 'Carrie-Anne Moss', '1967-08-21', 'Burnaby, Canada', 'Actriz conocida por Matrix y Jessica Jones.', 'https://www.lavanguardia.com/peliculas-series/images/profile/1967/8/w1280/xD4jTA3KmVp5Rq3aHcymL9DUGjD.jpg', '1989-01-01'),
('p029', 'Lana Wachowski', '1965-06-21', 'Chicago, Illinois, USA', 'Directora de Matrix y Sense8.', 'https://es.web.img3.acsta.net/pictures/16/03/09/16/29/317444.jpg', '1995-01-01'),
('p030', 'Lilly Wachowski', '1967-12-29', 'Chicago, Illinois, USA', 'Directora de Matrix y Sense8.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSwf6WavP7dycZro5Ra7BlTz5u1r76_KvziYA_rMsW4_AtxM8tkhgPEoM37PEHrLBHXbcY&usqp=CAU', '1995-01-01'),
('p031', 'Kate Winslet', '1975-10-05', 'Reading, England', 'Actriz ganadora del Oscar por The Reader.', 'https://es.web.img2.acsta.net/pictures/15/09/15/10/01/065591.jpg', '1991-01-01'),
('p032', 'Billy Zane', '1966-02-24', 'Chicago, Illinois, USA', 'Actor conocido por Titanic y The Phantom.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQaMTksb-H0cCaClX92y74SiPT5RjiF4rQj6SSkM-QkobLB_gRtZjtyVLaf0cUHT5IwP66q8AIKyDohDAPVSOKWpt3j-9p1-kTppV4-d-w', '1985-01-01'),
('p033', 'James Cameron', '1954-08-16', 'Kapuskasing, Canada', 'Director de Titanic y Avatar.', 'https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcSgACGReEEHBDtdjNcDAnSjUb93eTbC-bLdiD2xYkq0j7gw5lqPj4-w3mwNqdoRTM9tmxlPbilOE-wyyaBtyqmaI0WdliPao3iMVODLzrU', '1978-01-01'),
('p034', 'Russell Crowe', '1964-04-07', 'Wellington, New Zealand', 'Actor ganador del Oscar por Gladiator.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRkhqkS4g6Q2dOd4ysYiWHSkRjdN-0nmweAi4YRV9BOfj60taz2o8c02IpJbsWXamObtrbDu6G-uJxJ9u9v5p8A6RGx8kMUeuW7PFZM1pY', '1986-01-01'),
('p035', 'Joaquin Phoenix', '1974-10-28', 'San Juan, Puerto Rico', 'Actor ganador del Oscar por Joker.', 'https://upload.wikimedia.org/wikipedia/commons/thumb/d/dc/Joaquin_Phoenix-64908_%28cropped%29.jpg/960px-Joaquin_Phoenix-64908_%28cropped%29.jpg', '1982-01-01'),
('p036', 'Connie Nielsen', '1965-07-03', 'Frederikshavn, Denmark', 'Actriz conocida por Gladiator y Wonder Woman.', 'https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcSAV2_re78oggyB-cJcubmDpxiXBahReEedwRmcDyQ2FTPd2aNQZ_yRGSoMQgGEb4CgvfB1AJ07DNkQ8pwSBHvnmw', '1984-01-01'),
('p037', 'Ridley Scott', '1937-11-30', 'South Shields, England', 'Director de Gladiator y Alien.', 'https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcSuG95F4a_pTAFPT2Gv9Q6FDNaCN6Lind0EdyQptYczLG_IimGhN0ZyhTE32SiHEPn5idMrpFOMbWqXmPikEl6smcjGMaoxRcF61aWPkw', '1965-01-01'),
('p038', 'Tim Robbins', '1958-10-16', 'West Covina, California, USA', 'Actor ganador del Oscar por Mystic River.', 'https://pics.filmaffinity.com/061091445454557-nm_200.jpg', '1981-01-01'),
('p039', 'Morgan Freeman', '1937-06-01', 'Memphis, Tennessee, USA', 'Actor legendario conocido por The Shawshank Redemption.', 'https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcQOm06uoUBPWKgKeW8r7abDSzYgJe7jOFgC4S99qqNBdloMoEq0CoIr0y25_B85m-89vL_qHy-JvusFqa2FlZt3zpo1CxnjbbOYmoa24OA', '1964-01-01'),
('p040', 'Bob Gunton', '1945-11-15', 'Santa Monica, California, USA', 'Actor conocido por The Shawshank Redemption.', 'https://www.lavanguardia.com/peliculas-series/images/profile/1945/11/w1280/rr2KDCKK4t0f5YhZibCpLCAsJxc.jpg', '1981-01-01'),
('p041', 'Frank Darabont', '1959-01-28', 'Montbéliard, France', 'Director de The Shawshank Redemption.', 'https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcS5CoFUGXXmLNf3gqX7Emc4wG7e36Qa2C7PH2fIfWYvDF0JNBzOwXoMiGaz68_ShQMUDMxQhoJgBsuYWaXjSCQAag', '1981-01-01'),
('p042', 'Robert De Niro', '1943-08-17', 'New York City, USA', 'Actor legendario ganador de dos Oscars.', 'https://upload.wikimedia.org/wikipedia/commons/5/58/Robert_De_Niro_Cannes_2016.jpg', '1963-01-01'),
('p043', 'Robert Duvall', '1931-01-05', 'San Diego, California, USA', 'Actor ganador del Oscar por Tender Mercies.', 'https://m.media-amazon.com/images/M/MV5BMjk1MjA2Mjc2MF5BMl5BanBnXkFtZTcwOTE4MTUwMg@@._V1_FMjpg_UX1000_.jpg', '1952-01-01'),
('p044', 'Elijah Wood', '1981-01-28', 'Cedar Rapids, Iowa, USA', 'Actor conocido por El Señor de los Anillos.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQZmP7arb0NFBcfM9CzwYZAO9OFPGabibMWvTxfX_MshIOTruxjlhFe-xccfLsNqFxZ5n_zU5uLnC9uLicS9fg7qJv03OigKeKzVLfHB7g', '1989-01-01'),
('p045', 'Viggo Mortensen', '1958-10-20', 'New York City, USA', 'Actor conocido por El Señor de los Anillos.', 'https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcR0Or-ciiIGW12vQqvEqY3lMNXs3jDUnFHuknUQtgNaHP3kiOeASOFEXZ0tlmHDXW8_Qyn8Odi6quQBrMJ7-lVLDEz6KS3t-cznA1G6kFY', '1984-01-01'),
('p046', 'Ian McKellen', '1939-05-25', 'Burnley, England', 'Actor conocido por El Señor de los Anillos y X-Men.', 'https://www.lavanguardia.com/peliculas-series/images/profile/1939/5/w1280/5cnnnpnJG6TiYUSS7qgJheUZgnv.jpg', '1961-01-01'),
('p047', 'Peter Jackson', '1961-10-31', 'Pukerua Bay, New Zealand', 'Director de El Señor de los Anillos.', 'https://www.lavanguardia.com/peliculas-series/images/profile/1961/10/w1280/bNc908d59Ba8VDNr4eCcm4G1cR.jpg', '1987-01-01'),
('p048', 'Matthew McConaughey', '1969-11-04', 'Uvalde, Texas, USA', 'Actor ganador del Oscar por Dallas Buyers Club.', 'https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcSBwIVNbnqI0gOoarz5z06bRqBtZz-5-6AVpHfAq7CzVCyQyp7TxVbRh93sNt2Q1edPCM4UBx5KqoBRNQCctMCVqXbyfhPJ0IJZT03ODQ', '1991-01-01'),
('p049', 'Anne Hathaway', '1982-11-12', 'Brooklyn, New York, USA', 'Actriz ganadora del Oscar por Los Miserables.', 'https://upload.wikimedia.org/wikipedia/commons/thumb/8/87/Anne_Hathaway-68408_%28cropped%29.jpg/1200px-Anne_Hathaway-68408_%28cropped%29.jpg', '1999-01-01'),
('p050', 'Jessica Chastain', '1977-03-24', 'Sacramento, California, USA', 'Actriz nominada al Oscar por Zero Dark Thirty.', 'https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTd2KZyvJEYTBZ3RpbzaKlWZ5HuUUFIZcOtvoIaG7wsrKiGjWcEfO8ghm9UVaYQR9eifIaOZVl6TtQQ9sAscuQzzesavJKDD4K6oCX391M', '2004-01-01'),
('p051', 'Miles Teller', '1987-02-20', 'Downingtown, Pennsylvania, USA', 'Actor conocido por Whiplash y Top Gun: Maverick.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQn66z5CeEv4T1uUfqn7y3FNWgOvx60oSwMLEAxTYxFMK_x96xJ3idxCCGPocMazO8SfCm3wZV1oCQtGbLWhSOWFc4DSo3CtKDKi3tJ7HU', '2004-01-01'),
('p052', 'J.K. Simmons', '1955-01-09', 'Detroit, Michigan, USA', 'Actor ganador del Oscar por Whiplash.', 'https://encrypted-tbn2.gstatic.com/licensed-image?q=tbn:ANd9GcRZu-8-Q_milyQS2gNSCfeyjGbHixCEsYKL5OinguX4dbw4SYAHVELbza1RVQ2AiNs51j-iFcPmK-OBiEw', '1986-01-01'),
('p053', 'Paul Reiser', '1957-03-30', 'New York City, USA', 'Actor conocido por Whiplash y Aliens.', 'https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTfCDLCS7AnIv2zwkLYlRWTJY7UFr6zjGm4ynstaNp0wb3cCRh4cmjgzS8Z_kSI9HYWJHflKD7dfA5j4iwldOrIlw', '1982-01-01'),
('p054', 'Damien Chazelle', '1985-01-19', 'Providence, Rhode Island, USA', 'Director de Whiplash y La La Land.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRq6nmuoxlUN3nWglmWFqlXklO4EYSQygBdR70y_ftYsi3dWmxKOJa57BBVVdQi9FFAsWllXJ-n0AatgWRZ07a_MZJnYLw2v8nhcjOrbu4', '2009-01-01'),
('p055', 'Ryan Gosling', '1980-11-12', 'London, Ontario, Canada', 'Actor conocido por La La Land y Drive.', 'https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcQL8QrJGO7NfdJCBwsONox-rI1E3hKvrchgcSVCQQSWbm7VjV6A9lAo_qwvnJmn4CLFd7-T3qGjDP9Tor81xBItUQ', '1993-01-01'),
('p056', 'Emma Stone', '1988-11-06', 'Scottsdale, Arizona, USA', 'Actriz ganadora del Oscar por La La Land.', 'https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQmPUAcjEoVIrrIVMI3U3k0IHDcYC8Ul3yx3q9tzU4Rm29dtyMlZ9IqsFJ5dbDS7n9fTBtUW2pKszb4rh-1Ge1WfsKvRVGUeMFqip5moQ', '2004-01-01'),
('p057', 'John Legend', '1978-12-28', 'Springfield, Ohio, USA', 'Cantante y actor conocido por La La Land.', 'https://upload.wikimedia.org/wikipedia/commons/thumb/f/fc/John_Legend_2019_by_Glenn_Francis_%28cropped%29.jpg/1200px-John_Legend_2019_by_Glenn_Francis_%28cropped%29.jpg', '1998-01-01'),
('p058', 'Jamie Foxx', '1967-12-13', 'Terrell, Texas, USA', 'Actor ganador del Oscar por Ray.', 'https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcTKWAFtOdXxO8e8gwLuV3W4x5UAvgmuCxMBkGSndvEPr0SNXLfFhjw7hQ0A3MwY4Z2o-u9LXVlTtG6URXdEq5yYmDFU-tVT_P3fqKwiDR8', '1989-01-01'),
('p059', 'Christoph Waltz', '1956-10-04', 'Vienna, Austria', 'Actor ganador de dos Oscars por Django y Bastardos sin gloria.', 'https://www.lavanguardia.com/peliculas-series/images/all/profile/1956/10/27319/w1280/2Hhztd4mUEV9Y25rfkXDwzL9QI9.jpg', '1977-01-01'),
('p060', 'Harrison Ford', '1942-07-13', 'Chicago, Illinois, USA', 'Conocido por Indiana Jones y Star Wars', 'https://m.media-amazon.com/images/M/MV5BMTY4Mjg0NjIxOV5BMl5BanBnXkFtZTcwMTM2NTI3MQ@@._V1_.jpg', '1966-01-01'),
('p061', 'Steven Spielberg', '1946-12-18', 'Cincinnati, Ohio, USA', 'Director de ET y Jurassic Park', 'https://m.media-amazon.com/images/M/MV5BMTY1NjAzNzE1MV5BMl5BanBnXkFtZTYwNTk0ODc0._V1_.jpg', '1968-01-01'),
('p062', 'Laura Dern', '1967-02-10', 'Los Angeles, California, USA', 'Actriz ganadora del Oscar', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRA6woSlHuu8oZTg2pgrutLs6LPmuBWr7gMgsBEGkM2Wj-zCQMuQ_1-982INF_7YQ9ALxvIEbQYgCClxdbe-l9Bng', '1985-01-01'),
('p063', 'Tom Cruise', '1962-07-03', 'Syracuse, New York, USA', 'Actor de Misión Imposible', 'https://m.media-amazon.com/images/M/MV5BMTk1MjM3NTU5M15BMl5BanBnXkFtZTcwMTMyMjAyMg@@._V1_.jpg', '1981-01-01'),
('p064', 'Vin Diesel', '1967-07-18', 'Alameda County, California, USA', 'Actor de Fast & Furious', 'https://m.media-amazon.com/images/M/MV5BMjExNzA4MDYxN15BMl5BanBnXkFtZTcwOTI1MDAxOQ@@._V1_.jpg', '1990-01-01'),
('p065', 'Jodie Foster', '1962-11-19', 'Los Angeles, California, USA', 'Actriz ganadora del Oscar', 'https://hips.hearstapps.com/hmg-prod/images/gettyimages-2201633622-67e694c422665.jpg?crop=0.667xw:1.00xh;0.207xw,0&resize=640:*', '1968-01-01'),
('p066', 'Anthony Hopkins', '1937-12-31', 'Port Talbot, Wales', 'Actor ganador del Oscar', 'https://m.media-amazon.com/images/M/MV5BMTg5ODk1NTc5Ml5BMl5BanBnXkFtZTYwMjAwOTI4._V1_.jpg', '1960-01-01'),
('p067', 'David Fincher', '1962-08-28', 'Denver, Colorado, USA', 'Director de Seven y Zodiac', 'https://pics.filmaffinity.com/078315252655922-nm_200.jpg', '1984-01-01');

-- Insertar participaciones para tus producciones
INSERT INTO participaciones (id, profesional_id, produccion_id, rol, papel, created_at, updated_at) VALUES
-- El Padrino
('pa011', 'p011', 'pr018', 'ACTOR', 'Don Vito Corleone', NOW(), NOW()),
('pa012', 'p012', 'pr018', 'ACTOR', 'Michael Corleone', NOW(), NOW()),
('pa013', 'p013', 'pr018', 'ACTOR', 'Sonny Corleone', NOW(), NOW()),
('pa014', 'p014', 'pr018', 'DIRECTOR', NULL, NOW(), NOW()),

-- El Caballero Oscuro
('pa015', 'p015', 'pr019', 'ACTOR', 'Bruce Wayne / Batman', NOW(), NOW()),
('pa016', 'p016', 'pr019', 'ACTOR', 'Joker', NOW(), NOW()),
('pa017', 'p017', 'pr019', 'ACTOR', 'Harvey Dent', NOW(), NOW()),
('pa018', 'p003', 'pr019', 'DIRECTOR', NULL, NOW(), NOW()),

-- Pulp Fiction
('pa019', 'p018', 'pr020', 'ACTOR', 'Vincent Vega', NOW(), NOW()),
('pa020', 'p019', 'pr020', 'ACTOR', 'Mia Wallace', NOW(), NOW()),
('pa021', 'p020', 'pr020', 'ACTOR', 'Jules Winnfield', NOW(), NOW()),
('pa022', 'p006', 'pr020', 'DIRECTOR', NULL, NOW(), NOW()),

-- Forrest Gump
('pa023', 'p001', 'pr021', 'ACTOR', 'Forrest Gump', NOW(), NOW()),
('pa024', 'p021', 'pr021', 'ACTOR', 'Jenny Curran', NOW(), NOW()),
('pa025', 'p022', 'pr021', 'ACTOR', 'Teniente Dan Taylor', NOW(), NOW()),
('pa026', 'p023', 'pr021', 'DIRECTOR', NULL, NOW(), NOW()),

-- Inception
('pa027', 'p005', 'pr022', 'ACTOR', 'Dom Cobb', NOW(), NOW()),
('pa028', 'p024', 'pr022', 'ACTOR', 'Arthur', NOW(), NOW()),
('pa029', 'p025', 'pr022', 'ACTOR', 'Ariadne', NOW(), NOW()),
('pa030', 'p003', 'pr022', 'DIRECTOR', NULL, NOW(), NOW()),

-- The Matrix
('pa031', 'p026', 'pr023', 'ACTOR', 'Neo', NOW(), NOW()),
('pa032', 'p027', 'pr023', 'ACTOR', 'Morpheus', NOW(), NOW()),
('pa033', 'p028', 'pr023', 'ACTOR', 'Trinity', NOW(), NOW()),
('pa034', 'p029', 'pr023', 'DIRECTOR', NULL, NOW(), NOW()),
('pa035', 'p030', 'pr023', 'DIRECTOR', NULL, NOW(), NOW()),

-- Titanic
('pa036', 'p005', 'pr024', 'ACTOR', 'Jack Dawson', NOW(), NOW()),
('pa037', 'p031', 'pr024', 'ACTOR', 'Rose DeWitt Bukater', NOW(), NOW()),
('pa038', 'p032', 'pr024', 'ACTOR', 'Cal Hockley', NOW(), NOW()),
('pa039', 'p033', 'pr024', 'DIRECTOR', NULL, NOW(), NOW()),

-- Gladiator
('pa040', 'p034', 'pr025', 'ACTOR', 'Máximo Décimo Meridio', NOW(), NOW()),
('pa041', 'p035', 'pr025', 'ACTOR', 'Cómodo', NOW(), NOW()),
('pa042', 'p036', 'pr025', 'ACTOR', 'Lucila', NOW(), NOW()),
('pa043', 'p037', 'pr025', 'DIRECTOR', NULL, NOW(), NOW()),

-- The Shawshank Redemption
('pa044', 'p038', 'pr026', 'ACTOR', 'Andy Dufresne', NOW(), NOW()),
('pa045', 'p039', 'pr026', 'ACTOR', 'Ellis Boyd "Red" Redding', NOW(), NOW()),
('pa046', 'p040', 'pr026', 'ACTOR', 'Warden Norton', NOW(), NOW()),
('pa047', 'p041', 'pr026', 'DIRECTOR', NULL, NOW(), NOW()),

-- The Godfather Part II
('pa048', 'p012', 'pr027', 'ACTOR', 'Michael Corleone', NOW(), NOW()),
('pa049', 'p042', 'pr027', 'ACTOR', 'Vito Corleone', NOW(), NOW()),
('pa050', 'p043', 'pr027', 'ACTOR', 'Tom Hagen', NOW(), NOW()),
('pa051', 'p014', 'pr027', 'DIRECTOR', NULL, NOW(), NOW()),

-- The Lord of the Rings: The Return of the King
('pa052', 'p044', 'pr028', 'ACTOR', 'Frodo Baggins', NOW(), NOW()),
('pa053', 'p045', 'pr028', 'ACTOR', 'Aragorn', NOW(), NOW()),
('pa054', 'p046', 'pr028', 'ACTOR', 'Gandalf', NOW(), NOW()),
('pa055', 'p047', 'pr028', 'DIRECTOR', NULL, NOW(), NOW()),

-- Interstellar
('pa056', 'p048', 'pr029', 'ACTOR', 'Cooper', NOW(), NOW()),
('pa057', 'p049', 'pr029', 'ACTOR', 'Amelia Brand', NOW(), NOW()),
('pa058', 'p050', 'pr029', 'ACTOR', 'Murph', NOW(), NOW()),
('pa059', 'p003', 'pr029', 'DIRECTOR', NULL, NOW(), NOW()),

-- Whiplash
('pa060', 'p051', 'pr030', 'ACTOR', 'Andrew Neiman', NOW(), NOW()),
('pa061', 'p052', 'pr030', 'ACTOR', 'Terence Fletcher', NOW(), NOW()),
('pa062', 'p053', 'pr030', 'ACTOR', 'Jim Neiman', NOW(), NOW()),
('pa063', 'p054', 'pr030', 'DIRECTOR', NULL, NOW(), NOW()),

-- La La Land
('pa064', 'p055', 'pr031', 'ACTOR', 'Sebastian', NOW(), NOW()),
('pa065', 'p056', 'pr031', 'ACTOR', 'Mia', NOW(), NOW()),
('pa066', 'p057', 'pr031', 'ACTOR', 'Keith', NOW(), NOW()),
('pa067', 'p054', 'pr031', 'DIRECTOR', NULL, NOW(), NOW()),

-- Django Desencadenado
('pa068', 'p058', 'pr032', 'ACTOR', 'Django', NOW(), NOW()),
('pa069', 'p059', 'pr032', 'ACTOR', 'Dr. King Schultz', NOW(), NOW()),
('pa070', 'p005', 'pr032', 'ACTOR', 'Calvin Candie', NOW(), NOW()),
('pa071', 'p006', 'pr032', 'DIRECTOR', NULL, NOW(), NOW()),

-- Indiana Jones
('pa072', 'p001', 'pr033', 'ACTOR', 'Indiana Jones', NOW(), NOW()),
('pa079', 'p060', 'pr033', 'ACTOR', 'Indiana Jones', NOW(), NOW()),
('pa080', 'p061', 'pr033', 'DIRECTOR', NULL, NOW(), NOW()),

-- Jurassic Park
('pa073', 'p001', 'pr034', 'ACTOR', 'Dr. Alan Grant', NOW(), NOW()),
('pa081', 'p062', 'pr034', 'ACTOR', 'Dr. Ellie Sattler', NOW(), NOW()),
('pa082', 'p061', 'pr034', 'DIRECTOR', NULL, NOW(), NOW()),

-- Misión Imposible
('pa074', 'p020', 'pr035', 'ACTOR', 'Luther Stickell', NOW(), NOW()),
('pa083', 'p063', 'pr035', 'ACTOR', 'Ethan Hunt', NOW(), NOW()),

-- Fast & Furious
('pa075', 'p018', 'pr036', 'ACTOR', 'Dominic Toretto', NOW(), NOW()),
('pa084', 'p064', 'pr036', 'ACTOR', 'Dominic Toretto', NOW(), NOW()),

-- El silencio de los corderos
('pa076', 'p012', 'pr037', 'ACTOR', 'Jack Crawford', NOW(), NOW()),
('pa085', 'p065', 'pr037', 'ACTOR', 'Clarice Starling', NOW(), NOW()),
('pa086', 'p066', 'pr037', 'ACTOR', 'Hannibal Lecter', NOW(), NOW()),

-- El club de la lucha
('pa077', 'p005', 'pr038', 'ACTOR', 'Tyler Durden', NOW(), NOW()),
('pa078', 'p039', 'pr038', 'ACTOR', 'Narrador', NOW(), NOW()),
('pa087', 'p067', 'pr038', 'DIRECTOR', NULL, NOW(), NOW());

-- Insertar ubicaciones de filmación para tus producciones
INSERT INTO ubicaciones (id, nombre, latitud, longitud) VALUES
('u006', 'Nueva York, USA', 40.7128, -74.0060),
('u007', 'Chicago, USA', 41.8781, -87.6298),
('u008', 'Los Ángeles, USA', 34.0522, -118.2437),
('u009', 'Londres, UK', 51.5074, -0.1278),
('u010', 'París, Francia', 48.8566, 2.3522),
('u011', 'Montreal, Canadá', 45.5017, -73.5673),
('u012', 'Sydney, Australia', -33.8688, 151.2093),
('u013', 'Alabama, USA', 32.3182, -86.9023),
('u014', 'Savannah, Georgia, USA', 32.0809, -81.0912),
('u015', 'Mississippi, USA', 32.3547, -89.3985),
('u016', 'Túnez', 33.8869, 9.5375),
('u017', 'Hawaii, USA', 19.8968, -155.5828),
('u018', 'Costa Rica', 9.7489, -83.7534),
('u019', 'Praga, República Checa', 50.0755, 14.4378),
('u020', 'Los Ángeles, USA', 34.0522, -118.2437),
('u021', 'Pittsburgh, Pennsylvania, USA', 40.4406, -79.9959),
('u022', 'Wilmington, North Carolina, USA', 34.2257, -77.9447);

-- Insertar rodajes con el modelo actual
INSERT INTO rodajes (id, produccion_id, ubicacion_id, notas, created_at, updated_at) VALUES
-- El Padrino (pr018)
('r001', 'pr018', 'u006', 'Escenas en Little Italy, Nueva York', NOW(), NOW()),
('r002', 'pr018', 'u007', 'Escenas de la boda y exteriores en Chicago', NOW(), NOW()),

-- El Caballero Oscuro (pr019)
('r003', 'pr019', 'u008', 'Escenas principales en Los Ángeles', NOW(), NOW()),
('r004', 'pr019', 'u009', 'Escenas del hospital y persecuciones en Londres', NOW(), NOW()),

-- Inception (pr022)
('r005', 'pr022', 'u010', 'Escenas del sueño en París', NOW(), NOW()),
('r006', 'pr022', 'u011', 'Escenas del laberinto y hotel en Montreal', NOW(), NOW()),

-- The Matrix (pr023)
('r007', 'pr023', 'u012', 'Filmación principal en Sydney', NOW(), NOW()),

-- Forrest Gump (pr021)
('r008', 'pr021', 'u013', 'Escenas de la infancia en Alabama', NOW(), NOW()),
('r009', 'pr021', 'u014', 'Escenas de la carrera y parque en Savannah', NOW(), NOW()),

-- Django Unchained (pr032)
('r010', 'pr032', 'u015', 'Plantación y escenas del oeste en Mississippi', NOW(), NOW()),

-- Indiana Jones (pr033)
('r011', 'pr033', 'u016', 'Escenas del desierto en Túnez', NOW(), NOW()),
('r012', 'pr033', 'u017', 'Escenas de la jungla en Hawaii', NOW(), NOW()),

-- Jurassic Park (pr034)
('r013', 'pr034', 'u018', 'Escenas de la isla en Costa Rica', NOW(), NOW()),

-- Misión Imposible (pr035)
('r014', 'pr035', 'u019', 'Escenas de la embajada en Praga', NOW(), NOW()),

-- Fast & Furious (pr036)
('r015', 'pr036', 'u020', 'Carreras callejeras en Los Ángeles', NOW(), NOW()),

-- El silencio de los corderos (pr037)
('r016', 'pr037', 'u021', 'Escenas del FBI en Pittsburgh', NOW(), NOW()),

-- El club de la lucha (pr038)
('r017', 'pr038', 'u022', 'Escenas de la casa y oficinas en Wilmington', NOW(), NOW());

-- Insertar imágenes para los rodajes (tabla rodaje_imagenes)
INSERT INTO rodaje_imagenes (rodaje_id, imagen_url) VALUES
-- Imágenes para El Padrino en NY
('r001', 'https://example.com/padrino_ny1.jpg'),
('r001', 'https://example.com/padrino_ny2.jpg'),
('r001', 'https://example.com/padrino_ny3.jpg'),

-- Imágenes para El Padrino en Chicago
('r002', 'https://example.com/padrino_chicago1.jpg'),
('r002', 'https://example.com/padrino_chicago2.jpg'),

-- Imágenes para El Caballero Oscuro en LA
('r003', 'https://example.com/batman_la1.jpg'),
('r003', 'https://example.com/batman_la2.jpg'),

-- Imágenes para Inception en París
('r005', 'https://example.com/inception_paris1.jpg'),
('r005', 'https://example.com/inception_paris2.jpg'),

-- Imágenes para The Matrix en Sydney
('r007', 'https://example.com/matrix_sydney1.jpg'),
('r007', 'https://example.com/matrix_sydney2.jpg'),

-- Imágenes para Forrest Gump en Alabama
('r008', 'https://example.com/gump_alabama1.jpg'),

-- Imágenes para Django en Mississippi
('r010', 'https://example.com/django_mississippi1.jpg'),
('r010', 'https://example.com/django_mississippi2.jpg'),

-- Imágenes para Indiana Jones en Túnez
('r011', 'https://example.com/indiana_tunis1.jpg'),

-- Imágenes para Jurassic Park en Costa Rica
('r013', 'https://example.com/jurassic_costa_rica1.jpg'),

-- Imágenes para Misión Imposible en Praga
('r014', 'https://example.com/mi_praga1.jpg'),

-- Imágenes para El club de la lucha en Wilmington
('r017', 'https://example.com/fight_club_wilmington1.jpg'),
('r017', 'https://example.com/fight_club_wilmington2.jpg');

-- Insertar usuarios
INSERT INTO usuarios (id, nombre, apellido, email, username, password, rol, telefono, fecha_nacimiento, created_at, avatar) VALUES
('11111111-1111-1111-1111-111111111111', 'Admin', 'Admin', 'admin@geofilm.com', 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'ADMINISTRADOR', '600000000', '1980-01-01', NOW(), 'admin_avatar.jpg'),
('22222222-2222-2222-2222-222222222222', 'Usuario', 'Normal', 'user@geofilm.com', 'user', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'USUARIO', '611111111', '1990-05-15', NOW(), 'user_avatar.jpg'),
('33333333-3333-3333-3333-333333333333', 'María', 'García', 'maria@geofilm.com', 'maria', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'USUARIO', '622222222', '1995-08-20', NOW(), 'maria_avatar.jpg'),
('44444444-4444-4444-4444-444444444444', 'Mario', 'de Domingo Álvarez', 'wolverine@admin.com', 'wolverineAdmin', '$2a$12$hdb/kuYrrChi4PtOP2AFh.zx9hC7AmduUv4EBELRMWzGz63d/viUm', 'ADMINISTRADOR', '600000000', '1980-01-01', NOW(), 'https://sm.ign.com/t/ign_latam/screenshot/default/wolver_9stu.1280.jpg'),
('55555555-5555-5555-5555-555555555555', 'Mario', 'de Domingo Álvarez', 'wolverine@user.com', 'wolverineUser', '$2a$12$hdb/kuYrrChi4PtOP2AFh.zx9hC7AmduUv4EBELRMWzGz63d/viUm', 'USUARIO', '681013487', '1980-01-01', NOW(), 'https://sm.ign.com/t/ign_latam/screenshot/default/wolver_9stu.1280.jpg');

-- Insertar reviews
-- INSERT INTO reviews (id, usuario_id, produccion_id, comentario, puntuacion) VALUES
